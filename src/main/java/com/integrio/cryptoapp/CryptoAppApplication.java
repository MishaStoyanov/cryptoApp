package com.integrio.cryptoapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan(
        {
                "com.integrio.cryptoapp.models"
        }
)
@ComponentScan(
        {
                "com.integrio.cryptoapp.bot",
                "com.integrio.cryptoapp.config",
                "com.integrio.cryptoapp.services"
        }
)
@EnableJpaRepositories(
        basePackages = "com.integrio.cryptoapp.repositories",
        basePackageClasses = {
                JpaRepository.class,
                JpaSpecificationExecutor.class
        }
)
@EnableTransactionManagement
@EnableScheduling
public class CryptoAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(CryptoAppApplication.class, args);
    }

}
