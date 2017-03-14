package com.ai.api.config;

import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by Henry Yue on 2016/6/15 0015.
 */

@EnableSwagger2
public class ApplicationSwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
		        .apiInfo(new ApiInfoBuilder().title("AsiaInspection - API Documentation").build())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.ai.api.controller"))
		        .paths(PathSelectors.any())
                .build();
    }
}
