package cat.itacademy.s05.t01.n01.blackjack.config;
//import io.swagger.v3.oas.annotations.OpenAPIDefinition;
//import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
/*@OpenAPIDefinition(
        info = @Info(title = "Blackjack API", version = "1.0", description = "API for managing Blackjack game operations"),
        servers = @Server(url = "http://localhost:8080")
) */
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
               // .info(new io.swagger.v3.oas.models.info.Info().title("Blackjack API").version("1.0"));
                .info(new Info().title("Blackjack API").version("1.0"));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("blackjack")
                .pathsToMatch("/game/**", "/player/**", "/score/**")
                .build();
    }
}

