/**
 * Copyright (C) 2012-2017 Thales Services SAS.
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
