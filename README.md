# AuthzForce RESTful PDP
RESTful PDP API Implementation compliant REST Profile of XACML 3.0. This is minimalist compared to [AuthzForce server project](http://github.com/authzforce/server) as it does not provide multi-tenant PDP/PAP but only a single PDP (per instance). Therefore, this is more suitable for microservices, or, more generally, simple applications requiring only a PDP per instance.

In particular, the project provides the following (Maven groupId:artifactId):
* `org.ow2.authzforce:authzforce-ce-restful-pdp-cxf-spring-boot-server`: an Apache-CXF-based (JAX-RS) RESTful PDP service, packaged as a Spring Boot application, that you can run from the command-line (CLI), or use in other ways allowed by Spring Boot framework. 
* `org.ow2.authzforce:authzforce-ce-restful-pdp-jaxrs`: a pure JAX-RS implementation of a PDP service, that you can reuse as a library with any JAX-RS framework, especially other than Apache CXF, to provide your own custom RESTful PDP service.
