package com.nisshoku.mgnt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.any())
                .build()
                .apiInfo(appInfo())
                .securitySchemes(Collections.singletonList(basicAuth()));
    }

    private ApiInfo appInfo() {

        Contact contact = new Contact("Takahiro Hayami", "https://github.com/Taka-kun",
                "uchiha.takahiro@gmail.com");

        return new ApiInfo(
                "Nisshoku Management Project",
                "Manage Employees, Projects and Projects' tasks",
                "1.0",
                "Explore and have fun",
                contact,
                "Apache License Version 2.0",
                "https://www.apache.org/licenses/LICENSE-2.0",
                Collections.emptyList()
        );
    }

    private BasicAuth basicAuth() {
        return new BasicAuth("basic_auth");
    }
}
