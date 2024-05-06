package ru.slevyns.testtask.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(title = "Test", description = "Документация REST API", version = "1.0"),
        servers = @Server(url = "/", description = "Default Server URL")
)
public class OpenApiConfig {
}
