package com.stocksScreener.service;

import com.stocksScreener.model.User;
import com.stocksScreener.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StockSymbolService stockSymbolService;

    @Autowired
    public UserService(UserRepository userRepository, StockSymbolService stockSymbolService) {
        this.userRepository = userRepository;
        this.stockSymbolService = stockSymbolService;
    }

    public Optional<User> findByUserEmail(String email) {
        return userRepository.findUserByUserEmail(email);
    }

    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    @CacheEvict(value = "popularSymbols", allEntries = true)
    public void addToWatchList(String userEmail, List<String> symbols) {
        User user = findByUserEmail(userEmail).orElse(null);
        if (user != null) {
            LinkedHashSet<String> currentWatchlist = user.getWatchlist();
            // Create new list if user had no watchlist previously
            if (currentWatchlist == null) {
                currentWatchlist = new LinkedHashSet<>();
            }
            currentWatchlist.addAll(symbols);
            user.setWatchlist(currentWatchlist);
            saveUser(user);
            for(String symbol: symbols)
                stockSymbolService.updateSymbolPopularity(symbol, true);
        }
    }

    @CacheEvict(value = "popularSymbols", allEntries = true)
    public void removeFromWatchlist(String userEmail, List<String> symbols) {
        User user = findByUserEmail(userEmail).orElse(null);
        if (user != null) {
            LinkedHashSet<String> currentWatchlist = user.getWatchlist();
            currentWatchlist.removeIf(symbols::contains);
            user.setWatchlist(currentWatchlist);
            saveUser(user);
            for(String symbol: symbols)
                stockSymbolService.updateSymbolPopularity(symbol, false);
        }
    }
}
