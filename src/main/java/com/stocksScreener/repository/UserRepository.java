package com.stocksScreener.repository;

import com.stocksScreener.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    User findUserByUserEmail(String email);
}
