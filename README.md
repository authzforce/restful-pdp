# AuthzForce RESTful PDP
RESTful PDP API implementation, compliant with REST Profile of XACML 3.0. This is minimalist compared to [AuthzForce server project](http://github.com/authzforce/server) as it does not provide multi-tenant PDP/PAP but only a single PDP (per instance). Therefore, this is more suitable for microservices, or, more generally, simple applications requiring only one PDP per instance.

In particular, the project provides the following (Maven groupId:artifactId):
* `org.ow2.authzforce:authzforce-ce-restful-pdp-cxf-spring-boot-server`: a fully executable RESTful XACML PDP server (runnnable from the command-line), packaged as a [Spring Boot application](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment-install.html).
* `org.ow2.authzforce:authzforce-ce-restful-pdp-jaxrs`: pure JAX-RS implementation of a PDP service, that you can reuse as a library with any JAX-RS framework, especially other than Apache CXF, to provide your own custom RESTful PDP service.


## Features
### XACML PDP engine
See [AuthzForce Core features](https://github.com/authzforce/core/tree/release-10.1.0#features) for the XACML PDP engine's features.

### REST API
* Conformance with [REST Profile of XACML v3.0 Version 1.0](http://docs.oasis-open.org/xacml/xacml-rest/v1.0/xacml-rest-v1.0.html)
* Supported data formats, aka content types: 
	
	* `application/xacml+xml`: XACML 3.0/XML content, as defined by [RFC 7061](https://tools.ietf.org/html/rfc7061), for XACML Request/Response only;
	* `application/xml`: same as `application/xacml+xml`;
	* `application/xacml+json`: XACML 3.0/JSON Request/Response, as defined by [XACML v3.0 - JSON Profile Version 1.0](http://docs.oasis-open.org/xacml/xacml-json-http/v1.0/xacml-json-http-v1.0.html);
	* `application/json`: same as `application/xacml+json`.

## Limitations
See [AuthzForce Core limitations](https://github.com/authzforce/core/tree/release-10.1.0#limitations).

## System requirements
Java (JRE) 8 or later.


## Versions
See the [change log](CHANGELOG.md) following the *Keep a CHANGELOG* [conventions](http://keepachangelog.com/).

## License
See the [license file](LICENSE).

## Getting started
Get the [latest executable jar](http://central.maven.org/maven2/org/ow2/authzforce/authzforce-ce-restful-pdp-cxf-spring-boot-server/) from Maven Central with groupId/artifactId = `org.ow2.authzforce`/`authzforce-ce-restful-pdp-cxf-spring-boot-server`.

Copy the content of [that folder](cxf-spring-boot-server/src/test/cli) to the same directory and run the executable from that directory as follows:

```
$ ./authzforce-ce-restful-pdp-cxf-spring-boot-server-1.2.0.jar
```

If it refuses to start because the TCP listening port is already used (by some other server on the system), you can change that port in file `application.properties` copied previously: uncomment and change `server.port` property value to something else (default is 8080).

You know the embedded server is up and running when you see something like this (if and only if the logger for Spring classes is at least in INFO level, according to Logback configuration file mentioned down below) :
```
... Tomcat started on port(s): 8080 (http)
```

(You can change logging verbosity by modifying the Logback configuration file `logback.xml` copied previously.)

Now you can make a XACML request from a different terminal (install command `curl` if you don't have it already on your system):

```
$ curl --header "Content-Type: application/xacml+json" --data @IIA001/Request.json --request POST http://localhost:8080/services/pdp
```

You should get a XACML/JSON response such as:

```
{"Response":[{"Decision":"Permit"}]}
```


## Extensions
If you are missing features in AuthzForce, you can extend it with various types of plugins (without changing the existing code), as described on AuthzForce Core's [wiki](https://github.com/authzforce/core/wiki/Extensions).

In order to use them, put the extension JAR(s) into an `extensions` folder in the same directory as the executable jar, already present if you followed the previous *Getting started* section. If the extension(s) use XML configuration (e.g. AttributeProvider), add the schema import into `pdp-ext.xsd` (import namespace only, do not specify schema location) and schema namespace-to-location mapping into `catalog.xml`. Then run the executable as follows:

```
$ java -Dloader.path=extensions -jar authzforce-ce-restful-pdp-cxf-spring-boot-server-1.2.0.jar
```

## Support
If you are experiencing any issue with this project, please report it on the [OW2 Issue Tracker](https://jira.ow2.org/browse/AUTHZFORCE/). Select component `RESTFUL-PDP` when creating the issue.
Please include as much information as possible; the more we know, the better the chance of a quicker resolution:

* Software version
* Platform (OS and JDK)
* Stack traces generally really help! If in doubt include the whole thing; often exceptions get wrapped in other exceptions and the exception right near the bottom explains the actual error, not the first few lines at the top. It's very easy for us to skim-read past unnecessary parts of a stack trace.
* Log output can be useful too; sometimes enabling DEBUG logging can help;
* Your code & configuration files are often useful.

If you wish to contact the developers for other reasons, use [AuthzForce contact mailing list](http://scr.im/azteam).

## Vulnerability reporting
If you want to report a vulnerability, you must do so on the [OW2 Issue Tracker](https://jira.ow2.org/browse/AUTHZFORCE/) with *Security Level* set to **Private**. Then, if the AuthzForce team can confirm it, they will change it to **Public** and set a fix version.

## Contributing
See [CONTRIBUTING.md](CONTRIBUTING.md).
