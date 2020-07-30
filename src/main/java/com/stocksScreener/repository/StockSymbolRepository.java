package com.stocksScreener.repository;

import com.stocksScreener.model.StockSymbol;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockSymbolRepository extends MongoRepository<StockSymbol, String> {
    @Query(value = "{ $or: [ { '_id' : {$regex:?0, $options:'i'} }, { 'name' : {$regex:?0, $options:'i'} } ] }")
    List<StockSymbol> findSymbol(String pattern);
}
