/**
 * Copyright (C) 2012-2018 THALES.
 *
 * This file is part of AuthzForce CE.
 *
 * AuthzForce CE is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AuthzForce CE is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AuthzForce CE.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.ow2.authzforce.rest.pdp.cxf.springboot.test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;

import org.apache.cxf.jaxrs.client.WebClient;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ow2.authzforce.jaxrs.util.JsonRiJaxrsProvider;
import org.ow2.authzforce.rest.pdp.cxf.springboot.CxfJaxrsPdpSpringBootApp;
import org.ow2.authzforce.xacml.json.model.LimitsCheckingJSONObject;
import org.ow2.authzforce.xacml.json.model.XacmlJsonUtils;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Test for CXF/JAX-RS-based REST profile implementation using XACML JSON Profile for payloads
 * 
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CxfJaxrsPdpSpringBootApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
public class XacmlRestProfileJaxRsTest
{
	private static final int MAX_JSON_STRING_LENGTH = 100;

	/*
	 * Max number of child elements - key-value pairs or items - in JSONObject/JSONArray
	 */
	private static final int MAX_JSON_CHILDREN_COUNT = 100;

	private static final int MAX_JSON_DEPTH = 10;

	@BeforeClass
	public static void setup()
	{
		System.setProperty("javax.xml.accessExternalSchema", "http,file");
		// TODO: copy policies directory to maven target dir
		// maybe not needed
	}

	@LocalServerPort
	private int port;

	// @Autowired
	// private TestRestTemplate restTemplate;

	@Test
	public void testPdpRequest() throws IOException
	{
		// Request body
		final String reqLocation = "src/test/resources/IIA001/Request.json";
		try (InputStream reqIn = new FileInputStream(reqLocation))
		{
			final JSONObject jsonRequest = new LimitsCheckingJSONObject(reqIn, MAX_JSON_STRING_LENGTH, MAX_JSON_CHILDREN_COUNT, MAX_JSON_DEPTH);
			if (!jsonRequest.has("Request"))
			{
				throw new IllegalArgumentException("Invalid XACML JSON Request file: " + reqLocation + ". Expected root key: \"Request\"");
			}

			XacmlJsonUtils.REQUEST_SCHEMA.validate(jsonRequest);

			// expected response
			final String respLocation = "src/test/resources/IIA001/Response.json";
			try (final InputStream respIn = new FileInputStream(respLocation))
			{
				final JSONObject expectedResponse = new LimitsCheckingJSONObject(respIn, MAX_JSON_STRING_LENGTH, MAX_JSON_CHILDREN_COUNT, MAX_JSON_DEPTH);
				if (!expectedResponse.has("Response"))
				{
					throw new IllegalArgumentException("Invalid XACML JSON Response file: " + respLocation + ". Expected root key: \"Response\"");
				}

				XacmlJsonUtils.RESPONSE_SCHEMA.validate(expectedResponse);

				// send request
				final WebClient client = WebClient.create("http://localhost:" + port + "/services", Collections.singletonList(new JsonRiJaxrsProvider()));
				final JSONObject actualResponse = client.path("pdp").type("application/xacml+json").accept("application/xacml+json").post(jsonRequest, JSONObject.class);

				// check response
				Assert.assertTrue(expectedResponse.similar(actualResponse));
			}
		}
	}
}
