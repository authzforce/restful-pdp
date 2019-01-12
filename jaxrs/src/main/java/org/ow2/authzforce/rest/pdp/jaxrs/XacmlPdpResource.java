/**
 * Copyright 2012-2019 THALES.
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
package org.ow2.authzforce.rest.pdp.jaxrs;

import java.io.IOException;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import oasis.names.tc.xacml._3_0.core.schema.wd_17.Request;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Response;

import org.json.JSONObject;
import org.ow2.authzforce.core.pdp.api.io.PdpEngineInoutAdapter;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;

/**
 * Root resource for the PDP
 */
@Path("pdp")
public class XacmlPdpResource
{
	private final PdpEngineInoutAdapter<Request, Response> xmlPdpEngineAdapter;
	private final PdpEngineInoutAdapter<JSONObject, JSONObject> jsonPdpEngineAdapter;

	/**
	 * Constructs from PDP engine supporting XACML/XML and XACML JSON Profile
	 * 
	 * @param pdpConf
	 *            PDP engine configuration
	 * @throws java.lang.IllegalArgumentException
	 *             if {@code pdpConf.getXacmlExpressionFactory() == null || pdpConf.getRootPolicyProvider() == null}
	 * @throws java.io.IOException
	 *             error closing {@code pdpConf.getRootPolicyProvider()} when static resolution is to be used
	 * 
	 */
	public XacmlPdpResource(final PdpEngineConfiguration pdpConf) throws IllegalArgumentException, IOException
	{
		final PdpBundle pdpBundle = new PdpBundle(pdpConf, true);
		this.xmlPdpEngineAdapter = pdpBundle.getXacmlJaxbIoAdapter();
		this.jsonPdpEngineAdapter = pdpBundle.getXacmlJsonIoAdapter();
	}

	/**
	 * Evaluates XACML/XML Request
	 * 
	 * @param request
	 *            XACML/XML Request
	 * 
	 * @return XACML/XML Response
	 */
	@POST
	@Produces({ "application/xml", "application/xacml+xml" })
	@Consumes({ "application/xml", "application/xacml+xml" })
	public Response evaluateXml(final Request request)
	{
		return xmlPdpEngineAdapter.evaluate(request);
	}

	/**
	 * Evaluates XACML/JSON Request according to JSON Profile of XACML 3.0
	 * 
	 * @param request
	 *            XACML/JSON Request
	 * 
	 * @return XACML/JSON Response
	 */
	@POST
	@Produces({ "application/json", "application/xacml+json" })
	@Consumes({ "application/json", "application/xacml+json" })
	public JSONObject evaluateJson(final JSONObject request)
	{
		return jsonPdpEngineAdapter.evaluate(request);
	}
}
