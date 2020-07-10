package com.stocksScreener.service;

import org.influxdb.InfluxDB;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.net.URISyntaxException;
import java.util.Map;

@Service
public class pollingService {

    private final WebClient webClient;
    private final InfluxDB influxDB;

    @Autowired
    public pollingService(WebClient.Builder webClientBuilder, @Qualifier("createDBConnection") InfluxDB influxDB) {
        this.webClient = webClientBuilder.build();
        this.influxDB = influxDB;
    }

    public Mono<Map<String, Map<String, Float>>> testRestCall() throws URISyntaxException {
        //"https://sandbox.iexapis.com/stable/stock/market/batch?token=Tpk_18dfe6cebb4f41ffb219b9680f9acaf2&symbols=aapl,tsla&types=price"

        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("http")
                        .host("localhost")
                        .port(6000)
                        .path("stable/stock/market/batch/")
                        .queryParam("token", "Tpk_18dfe6cebb4f41ffb219b9680f9acaf2")
                        .queryParam("symbols", "aapl,tsla")
                        .queryParam("types", "price")
                        .build())
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Map<String, Float>>>() {
                });
    }

    @Scheduled(fixedRate = 5000)
    public void pollIex() throws URISyntaxException {

        Mono<Map<String, Map<String, Float>>> result = this.testRestCall();
        Map<String, Map<String, Float>> map = result.block();
        BatchPoints batchPoints = BatchPoints.builder().build();

        if (map != null) {
            for (Map.Entry<String, Map<String, Float>> stock : map.entrySet()) {
                String symbol = stock.getKey();
                Float price = stock.getValue().get("price");

                Point point = Point.measurement(symbol)
                        .addField("price", price)
                        .build();
                batchPoints.point(point);
            }
            influxDB.write(batchPoints);
        }
    }
}
