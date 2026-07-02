package me.ger_tret.goblin_stock_exchange.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI goblinOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Goblin Stock Exchange API")
                        .description("Official trading terminal for the underworld market. Satirical finance at its finest.")
                        .version("v1.0.0")
                        .contact(new Contact().name("German Tretyakevich").url("https://github.com/ger-tret")));
    }
}