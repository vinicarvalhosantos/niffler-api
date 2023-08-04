package br.com.santos.vinicius.nifflerapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NifflerApplication {

    public static void main(String[] args) {
        SpringApplication.run(NifflerApplication.class, args);
    }

}
