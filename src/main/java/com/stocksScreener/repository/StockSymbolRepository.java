package com.stocksScreener.repository;

import com.stocksScreener.model.StockSymbol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StockSymbolRepository extends PagingAndSortingRepository<StockSymbol, String> {
    @Query(value = "{ $or: [ { '_id' : {$regex:?0, $options:'i'} }, { 'name' : {$regex:?0, $options:'i'} } ] }")
    List<StockSymbol> findSymbol(String pattern, Pageable pageable);

    List<StockSymbol> findAllByPopularityGreaterThanOrderByPopularityDesc(long popularity, Pageable pageable);
}
