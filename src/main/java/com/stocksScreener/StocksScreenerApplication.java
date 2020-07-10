package com.stocksScreener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StocksScreenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(StocksScreenerApplication.class, args);
    }

}
