/*
 * Copyright 2012-2022 THALES.
 *
 * This file is part of AuthzForce CE.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ow2.authzforce.rest.pdp.jaxrs.test;

import org.apache.cxf.endpoint.Server;
import org.apache.cxf.ext.logging.LoggingFeature;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.lifecycle.SingletonResourceProvider;
import org.json.JSONObject;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;
import org.ow2.authzforce.jaxrs.util.JsonRiJaxrsProvider;
import org.ow2.authzforce.rest.pdp.jaxrs.XacmlPdpResource;
import org.ow2.authzforce.xacml.json.model.LimitsCheckingJSONObject;
import org.ow2.authzforce.xacml.json.model.XacmlJsonUtils;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test for CXF/JAX-RS-based REST profile implementation using XACML JSON Profile for payloads
 *
 */
public class XacmlRestProfileJaxRsTest
{
	protected static final Random PRNG = new SecureRandom();
	private final static String ENDPOINT_ADDRESS = "http://localhost:" + new AtomicInteger(9000 + PRNG.nextInt(1000)) + "/";

	private static final int MAX_JSON_STRING_LENGTH = 100;

	/*
	 * Max number of child elements - key-value pairs or items - in JSONObject/JSONArray
	 */
	private static final int MAX_JSON_CHILDREN_COUNT = 100;

	private static final int MAX_JSON_DEPTH = 10;

	private static Server server;

	private static void startServer(String pdpConfigLocation) throws Exception
	{
		final PdpEngineConfiguration pdpConf = PdpEngineConfiguration.getInstance(pdpConfigLocation, "src/test/resources/catalog.xml", "src/test/resources/pdp-ext.xsd");
		/*
		 * See also http://cxf.apache.org/docs/secure-jax-rs-services.html
		 */
		final JAXRSServerFactoryBean sf = new JAXRSServerFactoryBean();
		sf.setResourceClasses(XacmlPdpResource.class);
		sf.setResourceProvider(XacmlPdpResource.class, new SingletonResourceProvider(new XacmlPdpResource(pdpConf)));
		// add custom providers if any
		sf.setProviders(Collections.singletonList(new JsonRiJaxrsProvider()));
		final LoggingFeature loggingFeature = new LoggingFeature();
		loggingFeature.setPrettyLogging(true);
		loggingFeature.setVerbose(true);
		sf.setFeatures(Collections.singletonList(loggingFeature));
		sf.setAddress(ENDPOINT_ADDRESS);

		server = sf.create();
	}

	@Parameters("pdp_config_location")
	@BeforeClass
	public static void initialize(@Optional("src/test/resources/pdp.xml") String pdpConfigLocation) throws Exception
	{
		startServer(pdpConfigLocation);
	}

	@AfterClass
	public static void destroy()
	{
		server.stop();
		server.destroy();
	}

	@Test
	public void testPdpRequest() throws IOException
	{
		// Request body
		final String reqLocation = "src/test/resources/IIA001/Request.json";
		final JSONObject jsonRequest = new LimitsCheckingJSONObject(new FileReader(reqLocation, StandardCharsets.UTF_8), MAX_JSON_STRING_LENGTH, MAX_JSON_CHILDREN_COUNT, MAX_JSON_DEPTH);
		if (!jsonRequest.has("Request"))
		{
			throw new IllegalArgumentException("Invalid XACML JSON Request file: " + reqLocation + ". Expected root key: \"Request\"");
		}

		XacmlJsonUtils.REQUEST_SCHEMA.validate(jsonRequest);

		// expected response
		final String respLocation = "src/test/resources/IIA001/Response.json";
		final JSONObject expectedResponse = new LimitsCheckingJSONObject(new FileReader(respLocation, StandardCharsets.UTF_8), MAX_JSON_STRING_LENGTH, MAX_JSON_CHILDREN_COUNT, MAX_JSON_DEPTH);
		if (!expectedResponse.has("Response"))
		{
			throw new IllegalArgumentException("Invalid XACML JSON Response file: " + respLocation + ". Expected root key: \"Response\"");
		}

		XacmlJsonUtils.RESPONSE_SCHEMA.validate(expectedResponse);

		// send request
		final WebClient client = WebClient.create(ENDPOINT_ADDRESS, Collections.singletonList(new JsonRiJaxrsProvider()));
		final JSONObject actualResponse = client.path("pdp").type("application/xacml+json").accept("application/xacml+json").post(jsonRequest, JSONObject.class);

		// check response
		Assert.assertTrue(expectedResponse.similar(actualResponse), "JSON response does not match expected one.");
	}
}
