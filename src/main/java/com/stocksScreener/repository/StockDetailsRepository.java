package com.stocksScreener.repository;

import com.stocksScreener.model.StockDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockDetailsRepository extends MongoRepository<StockDetails, String> {
}
