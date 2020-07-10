package com.stocksScreener.controller;

import com.stocksScreener.model.StockDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/stocks")
@CrossOrigin("*")
public class StockDetailsController {

    private final WebClient webClient;
    private final String iexHost;
    private final String iexScheme;
    private final String iexPublicToken;

    public StockDetailsController(WebClient.Builder webClientBuilder,
                                  @Value("${iex.host}") String iexHost,
                                  @Value("${iex.scheme}") String iexScheme,
                                  @Value("${iex.publicToken}") String iexPublicToken) {
        this.webClient = webClientBuilder.build();
        this.iexHost = iexHost;
        this.iexScheme = iexScheme;
        this.iexPublicToken = iexPublicToken;
    }

    @GetMapping("/{symbol}/company")
    public Mono<StockDetails> getCompanyDetails(@PathVariable("symbol") String symbol) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(this.iexScheme)
                        .host(this.iexHost)
                        .path("stable/stock" + symbol + "/company")
                        .queryParam("token", this.iexPublicToken)
                        .build())
                .retrieve()
                .bodyToMono(StockDetails.class);

    }
}
