package br.com.beautystyle.agendamento.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.GroupedOpenApi;
import org.springdoc.core.SpringDocUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalTime;

@Configuration
public class SwaggerConfigurations {

    @Bean
    public GroupedOpenApi costumerApi() {
        return GroupedOpenApi.builder()
                .group("Customer end points")
                .pathsToMatch("/event/by_customer", "/event/by_customer/*", "/job/available/*",
                        "/company/available", "/auth")
                .build();
    }

    @Bean
    public GroupedOpenApi professionalApi() {
        return GroupedOpenApi.builder()
                .group("Professional end points")
                .pathsToMatch("/auth", "/customer", "/customer/*", "/event/by_professional", "/event/by_professional/*",
                        "/category/*", "/category", "/company", "/job", "/job/*", "/report", "/report/*")
                .build();
    }

    @Bean
    public OpenAPI docApiRest() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .info(new Info().title("Beauty Style")
                        .description("Agendamento de Horários")
                        .version("v2.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("SpringShop Wiki Documentation")
                        .url("https://springshop.wiki.github.org/docs"))

                .components(new Components()
                        .addSecuritySchemes(securitySchemeName, new SecurityScheme()
                                .name(securitySchemeName)
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .in(SecurityScheme.In.HEADER)
                                .bearerFormat("JWT")));

    }

    static {
        SpringDocUtils.getConfig().replaceWithSchema(LocalTime.class,
                new StringSchema().example("00:00").pattern("d{2}:d{2}"));
    }
}
