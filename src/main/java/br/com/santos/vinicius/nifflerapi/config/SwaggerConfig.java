package br.com.santos.vinicius.nifflerapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SwaggerConfig {

    @Value("${niffler.api.development.url}")
    String developmentUrl;

    @Value("${niffler.api.stage.url}")
    String stageUrl;

    @Value("${niffler.api.production.url}")
    String productionUrl;

    @Bean
    public OpenAPI myOpenApi() {
        Server developmentServer = new Server();
        developmentServer.setUrl(developmentUrl);
        developmentServer.setDescription("Niffler API Development");

        Server stageServer = new Server();
        stageServer.setUrl(stageUrl);
        stageServer.setDescription("Niffler API Staged");

        Server productionServer = new Server();
        productionServer.setUrl(productionUrl);
        productionServer.setDescription("Niffler API Production");

        Contact contact = new Contact();
        contact.setEmail("viniciuscarvalhomine@gmail.com");
        contact.setName("Vinicius Santos");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Niffler API Staged")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage the business rules to the fidelity twitch stream points.")
                .termsOfService("https://www.viniciuscsantos.com.br/terms")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(developmentServer, stageServer, productionServer));

    }

}
