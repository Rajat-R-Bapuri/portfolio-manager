package com.stocksScreener.repository;

import com.stocksScreener.model.StockSymbol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockSymbolRepository extends MongoRepository<StockSymbol, String> {
    List<StockSymbol> findAllBySymbolContaining(String searchKey);
}
