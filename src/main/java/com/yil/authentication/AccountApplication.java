package com.yil.authentication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = {"com.yil"})
@OpenAPIDefinition(info = @Info(title = "ERP Authentication Api", version = "1.0", description = "ERP Information"))
@SpringBootApplication
public class AccountApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(AccountApplication.class, args);
        context.start();
    }

}
