/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package se.inera.odp.camel.proxy;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * A simple Camel REST DSL route with Swagger API documentation.
 */
@Component
public class CamelRouter extends RouteBuilder {
    
    @Autowired
    private Environment env;
    
    @Value("${camel.component.servlet.mapping.context-path}")
    private String contextPath;

    @Override
    public void configure() throws Exception {

    	onException(Exception.class).handled(true);
        
        // this can also be configured in application.properties
        restConfiguration()
            .component("servlet")
            .bindingMode(RestBindingMode.json)
            .dataFormatProperty("prettyPrint", "true")
            .enableCORS(false)
            .port(env.getProperty("server.port", "8083"))
            .contextPath(contextPath.substring(0, contextPath.length() - 2))
            // turn on swagger api-doc
            .apiContextPath("/v2/api-docs")
            .apiProperty("api.title", "ODP API")
            .apiProperty("api.version", "1.0.0");


        rest("/kik/v1").description("KIK REST service")
            .consumes("application/json")
            .produces("application/json")
            
            .get("/{id}").description("Find kvalitetsindikatorer")
            .toD("http4://localhost:8085/api/get/kvalitetsindikatorer/kik-v1-${header.id}?bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
        ;

        rest("/kik/v2").description("KIK REST service")
        .consumes("application/xml")
        .produces("application/json")

        .post("/codes").responseMessage().code(200).message("OK").endResponseMessage()
        .to("direct:springboot")
        ;

        from("direct:springboot").streamCaching()
        .to("http4://localhost:8086/api/kik/v1/codes?bridgeEndpoint=true&copyHeaders=true&connectionClose=true")
        .log("Response from Spring Boot microservice: ${body}")
        .convertBodyTo(String.class)
        .end();
        
        rest("/swagger")
        .produces("text/html")
        .get("/index.html")
        .responseMessage().code(200).message("Swagger UI").endResponseMessage()
        .to("direct://get/swagger/ui/path");

        from("direct://get/swagger/ui/path")
        .routeId("SwaggerUI")
        .setBody().simple("resource:classpath:/swagger/index.html");
        
        // @formatter:on
    }

}
