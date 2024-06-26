# Change log
All notable changes to this project are documented in this file following the [Keep a CHANGELOG](http://keepachangelog.com) conventions. 

Issues reported on [GitHub](https://github.com/authzforce/core/issues) are referenced in the form of `[GH-N]`, where N is the issue number. Issues reported on [OW2](https://jira.ow2.org/browse/AUTHZFORCE/) are mentioned in the form of `[OW2-N]`, where N is the issue number.


## 7.1.0
### Added
- The service's base URL path can now be fully customized via `cxf.jaxrs.server.path` property in the `application.yml` configuration file, i.e. set to a different path from the default (`/services/pdp`). See the `docker/pdp/conf/application.yml` for an example.
- Possibility to enable another JSON-based Authorization API (for `Content-Type/Accept = application/json`) on the REST API, such as AuthZEN (see [AuthzForce AuthZEN](https://github.com/authzforce/authzen) for an example ), in addition to the XACML/JSON Profile.

### Fixed 
- Upgraded dependencies to fix CVEs:
  - spring-boot-dependencies: 3.1.11
  - Apache CXF: 4.0.4


## 7.0.0
### Changed
- Upgraded parent project authzforce-ce-parent: 9.1.0
  - **Migrated to Java 17 as the minimum required JRE version from now on** 
  - **Jakara XML Binding API (JAXB): 4.0** (javax.xml.bind.* packages/classes replaced with jakarta.xml.bind.*)
  - Upgraded Apache CXF dependencies (cxf-*): 4.0.3
  - Upgraded authzforce-ce-xacml-model, authzforce-ce-pdp-ext-model, authzforce-ce-xmlns-model: 9.1.0
- Upgraded dependency authzforce-ce-jaxrs-utils: 3.0.0
  - **Jakarta RESTful Web Services API (JAX-RS): 3.0.0** (javax.ws.rs.* packages/classes replaced with jakarta.ws.rs.*) 
  - authzforce-ce-xacml-json-model: 4.1.0 
- Upgraded AuthzForce Core dependencies (authzforce-ce-core-pdp-*): 21.0.1
  - authzforce-ce-core-pdp-api: 22.0.0
- Upgraded dependency snakeyaml: 2.2
- Upgraded Spring-Boot-Dependencies (due to CVEs): 3.1.8

### Fixed
- [AuthzForce Core - GH-83](https://github.com/authzforce/core/issues/83): NoSuchElementException thrown when the rule combining algorithm is permit-unless-deny and there is no Deny rule but at least one Permit rule with Obligation/Advice.
- [AuthzForce Core - GH-92](https://github.com/authzforce/core/issues/92) Deny-overrides rule combining algorithm - Missing obligations in case of multiple Permit Rules and no Deny Rule (only the Obligations from the first Permit Rule were returned).


## 6.1.0
### Added
- authzforce/core#69 : Support for XACML `<StatusDetail>` / `<MissingAttributeDetail>`s, returned when missing named Attribute(s) in AttributeDesignator/AttributeSelector expressions, and may be returned by custom PDP extensions as well. See the example of [custom RequestPreprocessor](https://github.com/authzforce/core/blob/release-20.3.0/pdp-testutils/src/test/java/org/ow2/authzforce/core/pdp/testutil/test/CustomTestRequestPreprocessorFactory.java) (PDP extension) adding AttributeId/Category to [custom AttributeValues](https://github.com/authzforce/core/blob/release-20.3.0/pdp-testutils/src/test/java/org/ow2/authzforce/core/pdp/testutil/test/TestExtensibleSimpleValue.java) (PDP extension) and the [custom function](pdp-testutils/src/test/java/org/ow2/authzforce/core/pdp/testutil/test/TestExtensibleSimpleValueEqualFunction.java) (PDP extension) using this info to throw a standard `missing-attribute` error with `<MissingAttributeDetail>` inside a `<StatusDetail>` element; and also the [example of XACML response](https://github.com/authzforce/core/blob/release-20.3.0/pdp-testutils/src/test/resources/custom/CustomRequestPreproc/response.xml) and [PDP configuration](https://github.com/authzforce/core/blob/release-20.3.0/pdp-testutils/src/test/resources/custom/CustomRequestPreproc/pdp.xml).
- `BaseXacmlJaxbRequestPreprocessor` and `SingleDecisionXacmlJaxbRequestPreprocessor` classes improved (new constructor arg: `customNamedAttributeParser`) to allow XACML/XML RequestPreprocessor extensions to customize the parsing of named Attributes with minimal effort.
- Policy / Rule evaluation optimization: if the Rule's Condition is always False, then the Rule is always NotApplicable as per section 7.11 of XACML 3.0, therefore skip the Rule.

### Fixed
- CVEs by upgrading dependencies:
    - authzforce-ce-parent: 8.5.0
    - authzforce-ce-xacml-model, authzforce-ce-pdp-ext-model, authzforce-ce-xmlns-model: 8.5.0
    - authzforce-ce-core-pdp-engine, authzforce-ce-core-pdp-io-xacml-json: 20.3.1
    - authzforce-ce-core-pdp-api: 21.4.0
    - authzforce-ce-xacml-json-model: 3.0.5
    - authzforce-ce-jaxrs-utils: 2.0.4
    - javax.mail -> jakarta.mail: 1.6.7
    - org.json:json: 20230227
    - Apache CXF version: 3.6.1
    - Spring Boot Starter: 2.6.14
    - Spring Core: 5.3.29
    - SLF4j: 1.7.36
    - Saxon-HE: 12.3
    - guava: 32.1.2-jre
    - org.everit.json.schema, renamed everit-json-schema: 1.14.2
    - jaxb2-basics-runtime: 0.13.1
    - jaxb-runtime: 2.3.3
    - logback-classic: 1.2.12
- authzforce/core#73 : Exception thrown when a Rule's Condition always returns False.


## 6.0.1
### Fixed
- Dockerfile (JAR version)


## 6.0.0
### Added
- New feature: XPath variables in AttributeSelectors' and `xPathExpression` `AttributeValues`s' XPath expressions can now be defined by XACML VariableDefinitions (variable name used as XACML VariableId), which means XACML Variables can be used as XPath variables there.

### Changed
- Upgraded dependencies:
  - `authzforce-ce-core-pdp-engine` to 20.0.0
  - `authzforce-ce-core-pdp-api` to 21.1.1

      - Changed Datatype extension interface (`AttributeValueFactory`) in support of the new feature above:

          - `getInstance(...)` `XPathCompiler` parameter replaced with `Optional<XPathCompilerProxy>`, where XPathCompilerProxy is a immutable version of `XPathCompiler` class with extra methods; the parameter is optional because XPath support may be disabled by PDP configuration or missing Policy(Set)Defaults/XPathVersion in XACML Policy(Set)
          - `Datatype` interface: added `ItemType getXPathItemType()` method used to declare Variable types on Saxon XPath evaluator when compiling XPath expressions with variables
          - `AttributeValue` must now implement `getXdmItem()` to return a XPath-compatible (XDM) value to be used as variables in XPath expressions, in order to support the new Feature mentioned above.


## 5.0.0
### Changed
- **PDP configuration XML schema changed: follow [AuthzForce Core migration instructions](https://github.com/authzforce/core/blob/develop/MIGRATION.md#migration-from-version-17x-to-18x) to migrate your old PDP configuration(s) (`pdp.xml`) to the new schema.**
  - Target namespace changed to `http://authzforce.github.io/core/xmlns/pdp/8`
  - `useStandardDatatypes` replaced with `standardDatatypesEnabled`;
  - `useStandardFunctions` replaced with `standardFunctionsEnabled`
  - `useStandardCombiningAlgorithms` replaced with `standardCombiningAlgorithmsEnabled`
  - `enableXPath` replaced with `xPathEnabled`
  - `standardEnvAttributeSource` replaced with `standardAttributeProvidersEnabled` and new `attributeProvider` type `StdEnvAttributeProviderDescriptor`. More info in [AuthzForce Core README](https://github.com/authzforce/core#providing-current-datetime-current-date-and-current-time-attributes).
  - `pdp/@version` attribute changed from required to optional with default value `8.1`
- Parent project `authzforce-ce-parent` upgraded to 8.2.1:
- Dependencies upgraded:
    - `authzforce-ce-core-pdp-engine`/`authzforce-ce-core-pdp-io-xacml-json`: 19.0.0
    - `authzforce-ce-core-pdp-api`: 20.0.0
    - `authzforce-ce-jaxrs-utils`: 2.0.3
    - `authzforce-ce-xacml-json-model`: 3.0.4
    - Saxon-HE: 10.6
    - Guava: 31.0
    - Apache CXF: 3.5.0
    - Spring Boot: 2.6.3
    - Spring Core: 5.2.14
    - SLF4J: 1.7.32
    - `jaxb2-basics-runtime`: 0.12.0
    - `javax.mail`: 1.6.2
    - `tomcat-embed-core`: 9.0.58

- API changes:

    -  For better support of XACML standard Multiple Decision Profile, request evaluation methods of the following PDP extensions now take an extra optional parameter (`Optional<EvaluationContext>`) for the Multiple Decision Request context: `CombiningAlg`, `Function`, `NamedAttributeProvider`, `PolicyProvider`.

### Added
- XACML JSON Profile feature: support for JSON Objects in XACML/JSON Attribute Values (linked to issue authzforce/server#61 ), allowing for complex structures (JSON objects) as data types
- Support for `<VariableReference>` equivalent in `<Target>`/`<Match>` elements: this feature is a workaround for a limitation in XACML schema which is not allowing Variables (`<VariableReference>`) in `Match` elements; i.e. the feature allows policy writers to use an equivalent of `<VariableReference>`s in `<Match>` elements (without changing the XACML schema) through a special kind of `<AttributeDesignator>` (in a specific `Category`, and `AttributeId` is used as `VariableId`). More info in [AuthzForce Core README](https://github.com/authzforce/core#using-variables-variablereference-in-targetmatch).

### Fixed
- Loading XACML/JSON schemas offline (linked to issue authzforce/server#64)
- CVE-2021-22118, CVE-2021-22696 and CVE-2021-3046


## 4.0.1
### Fixed
- Dockerfile
- Upgraded spring-boot-maven-plugin: 2.3.5.RELEASE


## 4.0.0
### Changed
- Upgraded supported JRE to **Java 11** (LTS). Java 8 no longer supported.
- upgraded dependency authzforce-ce-core: 17.0.0
- upgraded dependency authzforce-ce-jaxrs-utils: 2.0.0
- Upgraded dependency Apache CXF to 3.4.1
- upgraded dependency authzforce parent project to 8.0.0

### Fixed
- Fixed CVE on tomcat embedded: upgraded to 9.0.41


## 3.1.0
### Changed
- [GH-6] : upgraded Spring Boot dependency: 2.1.13.RELEASE
- Upgraded dependencies cxf-* (Apache CXF): 3.3.6
- Upgraded dependencies authzforce-ce-core-pdp-engine / authzforce-ce-core-pdp-io-xacml-json : 15.2.0
  - authzforce-ce-core-pdp-api: 16.3.0
  - authzforce-ce-xacml-json-model: 2.3.0
  - org.everit.json.schema: 1.12.1
- Upgraded dependency authzforce-ce-jaxrs-utils: 1.5.0
- Upgraded parent authzforce-ce-parent: 7.6.0
- Optimization: disabled unnecessary Spring Boot auto-configuration classes

### Added
- Docker Compose file (with sample configuration files) used to release the Docker image (on Docker hub)


## 3.0.0
### Changed
**XML namespaces in PDP configuration files must be updated according to [migration guide](https://github.com/authzforce/core/blob/develop/MIGRATION.md).**
- Dependencies authzforce-ce-core* version: 15.1.0:

  - Dependency authzforce-ce-core-pdp-api version changed to 16.2.0.
  - PDP configuration has been simplified: `rootPolicyRef` element becomes optional (if undefined, the PDP gets the root policy via the PolicyProvider's new method `getCandidateRootPolicy()`).
  - PDP configuration XSD versioning has been simplified: 

    - Simplified namespace (removed minor version) to `http://authzforce.github.io/core/xmlns/pdp/7` 
    - Schema version set to `7.1` (removed patch version).


## 2.0.0
### Changed
- Maven parent project version: 7.5.1
- Dependencies authzforce-ce-core* version: 14.0.1:
	- Changed **PDP configuration format** (XML Schema 'pdp.xsd'): v7.0.0 (more info in [migration guide](https://github.com/authzforce/core/blob/develop/MIGRATION.md) ):
	  - Replaced 'refPolicyProvider' and 'rootPolicyProvider' XML elements with 'policyProvider' and 'rootPolicyRef'.
	  - StaticRootPolicyProvider and StaticRefPolicyProvider XML types replaced by one StaticPolicyProvider type.
	- Added support for **Multiple Decision Profile when used with XACML/JSON Profile** (JSON input)
	- Fixed [issue](https://github.com/authzforce/core/issues/42): invalid JSON Response on client or internal error (Status not OK)
	- Fixed CVEs
- Dependency authzforce-ce-jaxrs-utils version: 1.4.0:
  - Upgraded dependency `authzforce-ce-xacml-json-model` version: 2.2.0

### Fixed
- XACML REST Profile compliance: PDP server returned 500 instead of expected 400 for invalid XACML/JSON
Request


## 1.6.0
### Changed
- Maven parent project version: 7.5.0
- Dependencies authzforce-ce-core* version: 13.3.0
- Dependency authzforce-ce-jaxrs-utils version: 1.3.0
- Changed indirect dependency versions:
	- authzforce-ce-core-pdp-api: 15.3.0
	- authzforce-ce-xacml-json-model: 2.1.0
	- Spring: 4.3.18 (fixes CVE)
	- Guava: 24.1.1-jre
  	- jaxb2-basics: 1.11.1
  	- mailapi replaced with javax.mail-api: 1.6.0
- Copyright company name

### Added
- Indirect dependency: javax.mail 1.6.0 (mail-api implementation for XACML RFC822Name support)
- Feature: 
	- DefaultEnvironmentProperties#replacePlaceholders() method now supports replacement of system properties and environment variables enclosed with ${ } (in addition to PARENT_DIR property); and a default value (separated from the property name by '!') if the property is undefined. Therefore, PDP extensions such as Attribute and Policy Providers can accept placeholders for system properties and environment variables in their string configuration parameters (as part of PDP configuration) and perform placeholder replacements with their factory method's input EnvironmentProperties.
	- In particular, 'policyLocation' elements in PDP's Policy Providers configuration now supports (not only PARENT_DIR property but also) system properties and environment variables (enclosed between '${...}') with default value (separated from property name by '!') if the property/variable is undefined.


## 1.5.0
### Changed
- authzforce-ce-parent (parent project) version: 7.4.0 -> upgraded CXF: 3.2.5 (fixed #3)
- Dependency authzforce-ce-core version: 13.2.0

### Fixed
- #3: CVE-2018-1305, CVE-2018-1304 


## 1.4.0
### Changed
- authzforce-ce-parent (parent project) version: 7.3.0
- Dependency authzforce-ce-core version: 13.1.0
- Dependency authzforce-ce-jaxrs-utils version: 1.2.0

### Fixed
- Temporary workaround CVE-2018-1305, CVE-2018-1304 (affecting CXF 3.2.4 -> spring boot 1.5.10) until issue fixed in Apache CXF: forcing Spring Boot version 1.5.11 


## 1.3.0
### Changed
- authzforce-ce-parent version: 7.2.0
- authzforce-ce-core version: 13.0.0

### Added
- Possibility to load PDP extensions (new configuration files `pdp-ext.xsd`, `catalog.xml`). See README for instructions.
- JAX-RS logging feature in `cxf-pdp-service.xml` to permit HTTP request/response logging

### Removed
- CXF swagger dependency

### Fixed
- Fixed file `application.properties` to use customized version of file `cxf-pdp-service.xml` (JAX-RS server configuration) in same folder


## 1.2.0
### Added
- Support for loading PDP configuration files from inside JARs using `classpath:`-prefixed URLs

### Changed
- Upgraded dependencies:
	- authzforce-ce-core: 10.1.0 -> 10.2.0


## 1.1.0
### Changed
- Added license headers to source code
- Upgraded parent project (authzforce-ce-parent): 7.0.0 -> 7.1.0
- Upgraded dependencies:
	- authzforce-ce-core: 10.0.0 -> 10.1.0
		- authzforce-ce-xacml-json-model: 1.0.0 -> 1.1.0
			- org.everit.json.schema: 1.6.0 -> 1.6.1
			- guava: 21.0 -> 22.0
			- json: 20170516 -> 20171018
		- authzforce-ce-core-pdp-api: 12.0.0 -> 12.1.0
			- guava: 21.0 -> 22.0
	- authzforce-ce-jaxrs-utils:-> 1.0.0 -> 1.1.0
- Changed default logback configuration, based on [Spring Boot documentation, §76.1](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html#howto-configure-logback-for-logging)


## 1.0.0
Initial GitHub release

