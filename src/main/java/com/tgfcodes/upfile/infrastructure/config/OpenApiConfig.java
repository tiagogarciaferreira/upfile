package com.tgfcodes.upfile.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    private static final String SECURITY_SCHEME_NAME = "BearerAuth";

    @Value("${spring.mvc.apiversion.default}")
    private String apiVersion;

    @Bean
    public OpenAPI enterpriseOpenAPI() {
        return new OpenAPI()
                .info(buildApiInfo())
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .components(new Components().addSecuritySchemes(SECURITY_SCHEME_NAME, buildSecurityScheme()))
                .externalDocs(new ExternalDocumentation()
                        .description("Project repository")
                        .url("https://github.com/tiagogarciaferreira/upfile")
                );
    }

    private Info buildApiInfo() {
        return new Info()
                .title("Up File API")
                .version(apiVersion)
                .description("REST API - File upload using Java Spring with storage in MinIO(S3).")
                .contact(new Contact()
                        .name("Tiago Garcia Ferreira")
                        .url("https://github.com/tiagogarciaferreira"))
                .license(new License()
                        .name("MIT License")
                        .url("https://opensource.org/licenses/MIT"))
                .termsOfService("https://github.com/tiagogarciaferreira/upfile");
    }

    private SecurityScheme buildSecurityScheme() {
        return new SecurityScheme()
                .name(SECURITY_SCHEME_NAME)
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .description("Provide the JWT token issued by the Identity Provider (IdP).");
    }
}