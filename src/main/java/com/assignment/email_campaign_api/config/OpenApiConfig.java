package com.assignment.email_campaign_api.config;


import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI emailCampaignOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Email Campaign Management API")
                        .description("REST API for managing email campaigns")
                        .version("1.0")
                        .contact(new Contact()
                                .name("M V Elangovan")
                                .email("elangovanmv45@gmail.com")))
                .externalDocs(new ExternalDocumentation()
                        .description("Project Documentation"));
    }
}