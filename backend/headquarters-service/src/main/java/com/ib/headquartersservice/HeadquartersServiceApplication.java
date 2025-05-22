package com.ib.headquartersservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HeadquartersServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HeadquartersServiceApplication.class, args);
    }

}
