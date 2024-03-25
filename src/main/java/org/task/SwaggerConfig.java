package org.task;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 * Configuration class for Swagger documentation.
 * This class sets up the Swagger Docket bean, which is used to configure the Swagger documentation for the API.
 */
@Configuration
public class SwaggerConfig {

    /**
     * Creates a Docket bean for Swagger documentation.
     * This bean is configured to select all APIs within the base package "org.task.controllers"
     * and to include only those paths that match the pattern "/api/**".
     *
     * @return The configured Docket bean.
     */
    @Bean
    public Docket api() {
        return new Docket(DocumentationType.OAS_30)
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.task.controllers"))
                .paths(PathSelectors.ant("/api/**"))
                .build()
                .apiInfo(apiInfo());
    }

    /**
     * Creates an ApiInfo object for the Swagger documentation.
     * This object contains the title, description, version, and contact information for the API.
     *
     * @return The configured ApiInfo object.
     */
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Test task for Spring Academy")
                .description("CRUD application for a products warehouse")
                .version("1.0.0")
                .contact(new Contact(
                        "Evgeniy Ryabov",
                        "https://github.com/shakowsx",
                        "shakowsx@gmail.com"))
                .build();
    }
}