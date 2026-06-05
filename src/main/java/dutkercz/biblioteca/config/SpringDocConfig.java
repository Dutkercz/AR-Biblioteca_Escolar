package dutkercz.biblioteca.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                              .title("Desafio AR - Biblioteca Escolar")
                              .description("Documentação da API - Biblioteca Escolar")
                              .contact(new Contact()
                                               .name("Cristian Tiago Dutkercz Rosa")
                                               .email("cristian.rosa@...")
                                               .url("https://github.com/Dutkercz/AR-Biblioteca_Escolar")));
    }
}
