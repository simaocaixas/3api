package com.github.Garden.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Tree Api",
                version = "v1",
                contact = @Contact(
                        name = "Simão Caixas",
                        email = "simaocaixas@gmail.com",
                        url = "https://github.com/simaocaixas/3api"
                )
        ),
        security =
                {
                        @SecurityRequirement(name = "bearerAuth")
                })

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.COOKIE
)
public class OpenApiConfiguration {

}
