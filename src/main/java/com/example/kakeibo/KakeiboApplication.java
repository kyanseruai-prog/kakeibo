package com.example.kakeibo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class KakeiboApplication {

    public static void main(String[] args) {
        SpringApplication.run(KakeiboApplication.class, args);
    }
}
