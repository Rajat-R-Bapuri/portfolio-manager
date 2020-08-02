package com.stocksScreener.service;

import com.stocksScreener.model.StockSymbol;
import com.stocksScreener.repository.StockSymbolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
            return stockSymbolRepository.findSymbol(".*" + searchKey + ".*", PageRequest.of(0, 7));
        }
        return new ArrayList<>();
    }

    @Cacheable(value = "popularSymbols")
    public List<StockSymbol> getPopularSymbols() {
        return stockSymbolRepository.findAllByPopularityGreaterThanOrderByPopularityDesc(0, PageRequest.of(0, 7));
    }

    public void updateSymbolPopularity(String symbol, boolean op){
        System.out.println("updateSymbolPopularity");
        Query query = new Query(Criteria.where("_id").is(symbol));
        Update update = new Update();
        if (op) {
            System.out.println("Up");
            update.inc("popularity", 1);
        } else {
            System.out.println("Down");
            update.inc("popularity", -1);
        }

        mongoOperations.findAndModify(query, update, StockSymbol.class);
    }
}
