/**
 * Copyright 2012-2018 Thales Services SAS.
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

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
import org.ow2.authzforce.xacml.json.model.Xacml3JsonUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

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

	private static void startServer() throws Exception
	{
		final PdpEngineConfiguration pdpConf = PdpEngineConfiguration.getInstance("src/test/resources/pdp.xml");
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

	@BeforeClass
	public static void initialize() throws Exception
	{
		startServer();
	}

	@AfterClass
	public static void destroy() throws Exception
	{
		server.stop();
		server.destroy();
	}

	@Test
	public void testPdpRequest() throws FileNotFoundException
	{
		// Request body
		final String reqLocation = "src/test/resources/IIA001/Request.json";
		final JSONObject jsonRequest = new LimitsCheckingJSONObject(new FileInputStream(reqLocation), MAX_JSON_STRING_LENGTH, MAX_JSON_CHILDREN_COUNT, MAX_JSON_DEPTH);
		if (!jsonRequest.has("Request"))
		{
			throw new IllegalArgumentException("Invalid XACML JSON Request file: " + reqLocation + ". Expected root key: \"Request\"");
		}

		Xacml3JsonUtils.REQUEST_SCHEMA.validate(jsonRequest);

		// expected response
		final String respLocation = "src/test/resources/IIA001/Response.json";
		final JSONObject expectedResponse = new LimitsCheckingJSONObject(new FileInputStream(respLocation), MAX_JSON_STRING_LENGTH, MAX_JSON_CHILDREN_COUNT, MAX_JSON_DEPTH);
		if (!expectedResponse.has("Response"))
		{
			throw new IllegalArgumentException("Invalid XACML JSON Response file: " + respLocation + ". Expected root key: \"Response\"");
		}

		Xacml3JsonUtils.RESPONSE_SCHEMA.validate(expectedResponse);

		// send request
		final WebClient client = WebClient.create(ENDPOINT_ADDRESS, Collections.singletonList(new JsonRiJaxrsProvider()));
		final JSONObject actualResponse = client.path("pdp").type("application/xacml+json").accept("application/xacml+json").post(jsonRequest, JSONObject.class);

		// check response
		Assert.assertTrue(expectedResponse.similar(actualResponse), "JSON response does not match expected one.");
	}
}
