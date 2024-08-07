package com.github.supercoding.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@OpenAPIDefinition(
        info = @Info(title = "taeyoung's api",
        description = "taeyoung's api 명세서",
        version = "v1")
)
@RequiredArgsConstructor
@Configuration
public class SwaggerConfig {
    @Bean
    public GroupedOpenApi groupedOpenApi() {
        String[] paths = {"com.github.supercoding.web.controller"};
        return GroupedOpenApi.builder()
                .group("taeyoung API")
                .pathsToMatch(paths)
                .build();
    }
}
