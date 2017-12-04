# Change log
All notable changes to this project are documented in this file following the [Keep a CHANGELOG](http://keepachangelog.com) conventions. 

Issues reported on [GitHub](https://github.com/authzforce/core/issues) are referenced in the form of `[GH-N]`, where N is the issue number. Issues reported on [OW2](https://jira.ow2.org/browse/AUTHZFORCE/) are mentioned in the form of `[OW2-N]`, where N is the issue number.


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

