package br.com.vinicius.santos.nifflerapi;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableRabbit
@SpringBootApplication
public class NifflerApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(NifflerApiApplication.class, args);
    }

}
