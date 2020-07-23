package com.stocksScreener.service;

import com.stocksScreener.model.StockSymbol;
import com.stocksScreener.repository.StockSymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockSymbolService {
    private final StockSymbolRepository stockSymbolRepository;

    @Autowired
    public StockSymbolService(StockSymbolRepository stockSymbolRepository, MongoOperations mongoOperations) {
        this.stockSymbolRepository = stockSymbolRepository;
    }

    public void addSymbols(List<StockSymbol> stockSymbols){
        stockSymbolRepository.saveAll(stockSymbols);
    }

    public List<StockSymbol> getSymbols(String searchKey){
        return stockSymbolRepository.findAllBySymbolContaining(searchKey);
    }
}
