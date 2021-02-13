# Change log
All notable changes to this project are documented in this file following the [Keep a CHANGELOG](http://keepachangelog.com) conventions. 

Issues reported on [GitHub](https://github.com/authzforce/core/issues) are referenced in the form of `[GH-N]`, where N is the issue number. Issues reported on [OW2](https://jira.ow2.org/browse/AUTHZFORCE/) are mentioned in the form of `[OW2-N]`, where N is the issue number.


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

