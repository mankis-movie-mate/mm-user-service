package cz.cvut.moviemate.userservice.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Value("${springdoc.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .addServersItem(new Server().url(serverUrl))
                .components(new Components().addSecuritySchemes("Bearer Authentication", createAPIKeyScheme()))
                .info(apiInfo());
    }

    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    private Info apiInfo() {
        return new Info()
                .title("Authentication Service Api Doc")
                .version("1.0.0")
                .description("HTTP APIs to manage app users.")
                .contact(new Contact().name("manki1337"));
    }
}
