package com.stocksScreener.service;

import com.stocksScreener.model.StockSymbol;
import com.stocksScreener.repository.StockSymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames={"StockSymbols"})
public class StockSymbolService {
    private final StockSymbolRepository stockSymbolRepository;

    @Autowired
    public StockSymbolService(StockSymbolRepository stockSymbolRepository) {
        this.stockSymbolRepository = stockSymbolRepository;
    }

    public void addSymbols(List<StockSymbol> stockSymbols) {
        stockSymbolRepository.saveAll(stockSymbols);
    }

    @Cacheable
    public List<StockSymbol> getSymbols(String searchKey) {
        return stockSymbolRepository.findAllBySymbolContaining(searchKey);
    }
}
