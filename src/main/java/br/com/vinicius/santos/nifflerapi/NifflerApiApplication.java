package br.com.vinicius.santos.nifflerapi;

import br.com.vinicius.santos.nifflerlib.NifflerLibApplication;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.context.annotation.ComponentScan;

@EnableRabbit
@SpringBootApplication
//@ComponentScan({"br.com.vinicius.santos.nifflerlib"})
@EntityScan({"br.com.vinicius.santos.nifflerlib"})
@EnableJpaRepositories({"br.com.vinicius.santos.nifflerlib"})
@NifflerLibApplication
public class NifflerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NifflerApiApplication.class, args);
    }

}
