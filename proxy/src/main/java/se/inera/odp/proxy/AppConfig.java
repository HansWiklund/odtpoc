package se.inera.odp.proxy;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

@Configuration
public class AppConfig {
	
	/*
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
						
				.route("path_route", r -> r.path("/api/kik/v1/{r}").and().method(HttpMethod.GET)
						.filters(f -> f.rewritePath("/api/kik/v1/(?<r>.*)", "/api/get/kvalitetsindikatorer/kik-v1-${r}"))
						.uri("http://localhost:8085"))
				.route("path_route", r -> r.path("/api/kik/v1/{r}").and().method(HttpMethod.POST)
						.filters(f -> f.rewritePath("/api/kik/v1/(?<r>.*)", "/api/save"))
						.uri("http://localhost:8085"))
				
				.route("path_route", r -> r.path("/api/kik/v1/{r}/{id}").and().method(HttpMethod.DELETE)
						.filters(f -> f.rewritePath("/api/kik/v1/(?<r>.*)", "/api/delete"))
						.uri("http://localhost:8085"))
						
			.build();
	}
*/
}
