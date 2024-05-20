/*
 * Copyright 2012-2024 THALES.
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

import oasis.names.tc.xacml._3_0.core.schema.wd_17.Request;
import oasis.names.tc.xacml._3_0.core.schema.wd_17.Response;
import org.json.JSONObject;
import org.ow2.authzforce.core.pdp.api.CloseablePdpEngine;
import org.ow2.authzforce.core.pdp.api.DecisionRequestPreprocessor;
import org.ow2.authzforce.core.pdp.api.DecisionResultPostprocessor;
import org.ow2.authzforce.core.pdp.api.io.BaseXacmlJaxbResultPostprocessor;
import org.ow2.authzforce.core.pdp.api.io.PdpEngineInoutAdapter;
import org.ow2.authzforce.core.pdp.api.value.AttributeValueFactoryRegistry;
import org.ow2.authzforce.core.pdp.impl.BasePdpEngine;
import org.ow2.authzforce.core.pdp.impl.PdpEngineConfiguration;
import org.ow2.authzforce.core.pdp.impl.io.PdpEngineAdapters;
import org.ow2.authzforce.core.pdp.impl.io.SingleDecisionXacmlJaxbRequestPreprocessor;
import org.ow2.authzforce.core.pdp.io.xacml.json.BaseXacmlJsonRequestPreprocessor;
import org.ow2.authzforce.core.pdp.io.xacml.json.BaseXacmlJsonResultPostprocessor;
import org.ow2.authzforce.core.pdp.io.xacml.json.SingleDecisionXacmlJsonRequestPreprocessor;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Bundle containing the PDP engine with associated adapters
 *
 */
public final class PdpBundle
{
	private final CloseablePdpEngine engine;
	private final PdpEngineInoutAdapter<Request, Response> xacmlJaxbIoAdapter;
	private final PdpEngineInoutAdapter<JSONObject, JSONObject> xacmlJsonIoAdapter;
	private final PdpEngineInoutAdapter<JSONObject, JSONObject> defaultJsonIoAdapter;

	/**
	 * Constructs a PDP engine from {@code pdpConf} and I/O adapters plugged to it
	 * 
	 * @param pdpConf
	 *            PDP engine configuration
	 * @throws java.lang.IllegalArgumentException
	 *             if {@code pdpConf.getXacmlExpressionFactory() == null || pdpConf.getRootPolicyProvider() == null}
	 * @throws java.io.IOException
	 *             error closing {@code pdpConf.getRootPolicyProvider()} when static resolution is to be used
	 */
	public PdpBundle(final PdpEngineConfiguration pdpConf) throws IllegalArgumentException, IOException
	{
		this.engine = new BasePdpEngine(pdpConf);
		// did not throw exception, so valid

		/*
		 * PDP input/output adapters
		 */
		final Map<Class<?>, Entry<DecisionRequestPreprocessor<?, ?>, DecisionResultPostprocessor<?, ?>>> ioProcChains = pdpConf.getInOutProcChains();
		final int clientReqErrVerbosityLevel = pdpConf.getClientRequestErrorVerbosityLevel();
		final AttributeValueFactoryRegistry attValFactoryRegistry = pdpConf.getAttributeValueFactoryRegistry();
		final boolean isStrictAttIssuerMatchEnabled = pdpConf.isStrictAttributeIssuerMatchEnabled();
		final boolean isXpathEnabled = pdpConf.isXPathEnabled();

		this.xacmlJaxbIoAdapter = PdpEngineAdapters.newInoutAdapter(Request.class, Response.class, engine, ioProcChains, extraPdpFeatures -> SingleDecisionXacmlJaxbRequestPreprocessor.LaxVariantFactory.INSTANCE.getInstance(attValFactoryRegistry, isStrictAttIssuerMatchEnabled, isXpathEnabled,
				extraPdpFeatures), () -> new BaseXacmlJaxbResultPostprocessor(clientReqErrVerbosityLevel));

		final Entry<DecisionRequestPreprocessor<?, ?>, DecisionResultPostprocessor<?, ?>> jsonIoProcChain = ioProcChains.get(JSONObject.class);

		// Default In-out adapter for JSON requests if any defined by configuration (may be XACML/JSON Profile's one or OpenID AuthZEN for instance)
		this.defaultJsonIoAdapter = jsonIoProcChain == null ? null: PdpEngineAdapters.newInoutAdapter(JSONObject.class, JSONObject.class, engine, ioProcChains, extraPdpFeatures -> SingleDecisionXacmlJsonRequestPreprocessor.LaxVariantFactory.INSTANCE.getInstance(attValFactoryRegistry, isStrictAttIssuerMatchEnabled, isXpathEnabled,
				extraPdpFeatures), () -> new BaseXacmlJsonResultPostprocessor(clientReqErrVerbosityLevel));

		// In-out adapter for XACML/JSON Profile's requests if defined by configuration
		this.xacmlJsonIoAdapter = defaultJsonIoAdapter != null && jsonIoProcChain.getKey() instanceof BaseXacmlJsonRequestPreprocessor ? defaultJsonIoAdapter: null;

	}

	/**
	 * Get the underlying common PDP engine used by the adapters
	 * 
	 * @return the common PDP engine
	 */
	public CloseablePdpEngine getEngine()
	{
		return this.engine;
	}

	/**
	 * Get the PDP engine adapter for XACML 3.0/XML (core specification) input/output
	 * 
	 * @return the XACML/XML I/O adapter
	 */
	public PdpEngineInoutAdapter<Request, Response> getXacmlJaxbIoAdapter()
	{
		return xacmlJaxbIoAdapter;
	}

	/**
	 * Get the PDP engine adapter for XACML 3.0/JSON (JSON Profile of XACML 3.0) input/output
	 *
	 * @return the XACML/JSON I/O adapter or null if not supported
	 */
	public PdpEngineInoutAdapter<JSONObject, JSONObject> getXacmlJsonIoAdapter()
	{
		return xacmlJsonIoAdapter;
	}

	/**
	 * Get the default PDP engine adapter for JSON input/output
	 * 
	 * @return the I/O adapter or null if JSON input not supported at all
	 */
	public PdpEngineInoutAdapter<JSONObject, JSONObject> getDefaultJsonIoAdapter()
	{
		return defaultJsonIoAdapter;
	}
}
