package pl.futurecollars.invoicing.config;

import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfiguration {

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
            .addOpenApiCustomiser(openApi -> {
                Info info = new Info();
                info.setTitle("Invoicing System");
                info.setDescription("Invoicing system API");
                info.setVersion("0.0.1");
                openApi.setInfo(info);
            })
            .group("invoicing-system")
            .pathsToMatch("/**")
            .build();
    }
}
