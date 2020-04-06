package io.github.victoriholc.hoteljavaapi.util.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Implements the necessary settings for using Swagger as an API documentation tool.
 *  
 * @author Victor Freitas
 * @since 03/04/2020
 */
@Configuration
@Profile({"dev", "prod"})
@EnableSwagger2
public class SwaggerConfiguration {

	/**
	 * Configurates the endpoints mapped in the documentation.
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 *  
	 * @return <code>Docket</code> object
	 */
	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2).select()
				.apis(RequestHandlerSelectors.basePackage("io.github.victoriholc.hoteljavaapi.controller"))
				.paths(PathSelectors.any()).build()
				.apiInfo(this.apiInfo());
				
	}
	
	/**
	 * Configurates the informations about the API. 
	 * 
	 * @author Victor Freitas
	 * @since 03/04/2020
	 * 
	 * @return <code>ApiInfo</code> object
	 */
	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Hotel Java API")
				.description("Hotel Java API - Endpoint's documentation").version("0.0.1")
				.build();
	}
}
