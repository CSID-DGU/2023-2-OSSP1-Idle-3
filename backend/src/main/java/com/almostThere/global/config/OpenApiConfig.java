package com.almostThere.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    // http://localhost:9999/swagger-ui/index.html
    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
                .title("거의 다왔어")
                .version("v1.0.0")
                .description("Almost There Api");

        return new OpenAPI()
                .components(new Components())
                .info(info);
    }
}