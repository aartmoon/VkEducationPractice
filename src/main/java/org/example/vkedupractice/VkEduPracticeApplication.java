package org.example.vkedupractice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class VkEduPracticeApplication {

    public static void main(String[] args) {
        SpringApplication.run(VkEduPracticeApplication.class, args);
    }

}
/*
запускаем на h2:
mvn spring-boot:run

запускаем на postgresql
docker-compose up -d
 */