package com.ecommerce.product.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Microservices E-commerce API - Porduct MS",
                version = "0.0.1",
                description = "API REST (microservice) of a simple e-commerce to handle products.",
                contact = @Contact(
                        name = "MFTech",
                        email = "support@mftech.com"
                )
        ),
        servers = {
                @Server(url = "http://localhost:8081", description = "Local environment - PRODUCT MS")
        }
)
public class OpenApiConfig {
}
