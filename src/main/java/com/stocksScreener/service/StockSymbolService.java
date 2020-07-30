package com.stocksScreener.service;

import com.stocksScreener.model.StockSymbol;
import com.stocksScreener.repository.StockSymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig(cacheNames = {"StockSymbols"})
public class StockSymbolService {
    private final StockSymbolRepository stockSymbolRepository;
    private final MongoOperations mongoOperations;

    @Autowired
    public StockSymbolService(StockSymbolRepository stockSymbolRepository,
                              MongoOperations mongoOperations) {
        this.stockSymbolRepository = stockSymbolRepository;
        this.mongoOperations = mongoOperations;
    }

    public void addSymbols(List<StockSymbol> stockSymbols) {
        stockSymbolRepository.saveAll(stockSymbols);
    }

    @Cacheable
    public List<StockSymbol> getSymbols(String searchKey) {
        if (searchKey.matches("[a-zA-Z]*")) {
            return stockSymbolRepository.findSymbol(".*" + searchKey + ".*");
        }
        return new ArrayList<>();
    }
}
