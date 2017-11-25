package org.ow2.authzforce.rest.pdp.cxf.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@EnableAutoConfiguration
// @PropertySource("classpath:application.properties")
@ImportResource("${pdp.service.conf}")
public class CxfJaxrsPdpSpringBootApp
{

	public static void main(final String[] args)
	{
		/*
		 * Allow use of http:// schema locations in XML catalogs for AuthzForce schemas
		 */
		System.setProperty("javax.xml.accessExternalSchema", "http,file");
		SpringApplication.run(CxfJaxrsPdpSpringBootApp.class, args);
	}

	// @Value("${pdp.service.conf}")
	// private URL serverConf;

	// @Autowired
	// private JAXRSServerFactoryBean tazService;
	//
	// @Bean
	// public Server rsServer()
	// {
	// // final SpringBusFactory bf = new SpringBusFactory();
	// // final Bus bus = bf.createBus(serverConf);
	// return tazService.create();
	// }
}
