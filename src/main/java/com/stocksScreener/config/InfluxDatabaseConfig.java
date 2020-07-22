package com.stocksScreener.config;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfluxDatabaseConfig {


    private final String dbUrl;
    private final String databaseName;

    public InfluxDatabaseConfig(@Value("${influxdb.url}") String dbUrl, @Value("${influxdb.dbName}") String databaseName) {
        this.dbUrl = dbUrl;
        this.databaseName = databaseName;
    }

    @Bean
    public InfluxDB createDBConnection() {
        InfluxDB influxDB = InfluxDBFactory.connect(this.dbUrl);
        influxDB.setDatabase(this.databaseName);
        return influxDB;
    }
}
