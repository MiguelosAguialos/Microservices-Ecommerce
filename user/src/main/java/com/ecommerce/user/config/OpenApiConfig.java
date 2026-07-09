package com.ecommerce.user.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Microservices E-commerce API - User MS",
                version = "0.0.1",
                description = "API REST (microservice) de um e-commerce simples para gerenciamento de usuarios.",
                contact = @Contact(
                        name = "MFTech",
                        email = "support@mftech.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8082", description = "Ambiente local - USER MS")
        }
)
public class OpenApiConfig {
}
