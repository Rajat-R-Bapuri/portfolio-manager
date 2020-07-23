package com.stocksScreener.controller;

import com.stocksScreener.model.StockDetails;
import com.stocksScreener.service.StockDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequestMapping("/stocks")
public class StockDetailsController {

    private final WebClient webClient;
    private final String iexHost;
    private final String iexScheme;
    private final String iexPublicToken;

    private final StockDetailsService stockDetailsService;

    @Autowired
    public StockDetailsController(
            StockDetailsService stockDetailsService,
            WebClient.Builder webClientBuilder,
            @Value("${iex.host}") String iexHost,
            @Value("${iex.scheme}") String iexScheme,
            @Value("${iex.publicToken}") String iexPublicToken) {
        this.webClient = webClientBuilder.build();
        this.iexHost = iexHost;
        this.iexScheme = iexScheme;
        this.iexPublicToken = iexPublicToken;
        this.stockDetailsService = stockDetailsService;
    }

    @GetMapping("/{symbol}/company")
    public Mono<StockDetails> getCompanyDetails(@PathVariable("symbol") String symbol) {
        // TODO check if symbol is in database
        Optional<StockDetails> stockDetails = stockDetailsService.getStockDetails(symbol);
        Mono<StockDetails> stockDetailsMono;

        if(stockDetails.isPresent()){
            stockDetailsMono = Mono.just(stockDetails.get());
        }else{
            stockDetailsMono = fetchFromIex(symbol);
            stockDetailsService.insertStockDetails(stockDetailsMono);
        }

        return stockDetailsMono;
    }

    public Mono<StockDetails> fetchFromIex(String symbol) {
        return this.webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .scheme(this.iexScheme)
                        .host(this.iexHost)
                        .path("stable/stock/" + symbol + "/company")
                        .queryParam("token", this.iexPublicToken)
                        .build())
                .retrieve()
                .bodyToMono(StockDetails.class);
    }
}
