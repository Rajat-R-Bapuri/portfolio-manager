package com.stocksScreener.service;

import com.stocksScreener.model.StockDetails;
import com.stocksScreener.repository.StockDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class StockDetailsService {
    private final StockDetailsRepository stockDetailsRepository;

    @Autowired
    public StockDetailsService(StockDetailsRepository stockDetailsRepository) {
        this.stockDetailsRepository = stockDetailsRepository;
    }

    public Optional<StockDetails> getStockDetails(String symbol) {
        return stockDetailsRepository.findById(symbol);
    }

    public void insertStockDetails(Mono<StockDetails> stockDetails) {
        stockDetails.subscribe(stockDetailsRepository::save);
    }

    public void insertStockDetails(StockDetails stockDetails) {
        stockDetailsRepository.save(stockDetails);
    }
}
