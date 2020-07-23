package com.stocksScreener.controller;

import com.stocksScreener.model.StockSymbol;
import com.stocksScreener.service.StockSymbolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stocks/")
public class StockSymbolController {

    private final StockSymbolService stockSymbolService;

    @Autowired
    public StockSymbolController(StockSymbolService stockSymbolService) {
        this.stockSymbolService = stockSymbolService;
    }

    @PostMapping("/symbols")
    public void addSymbols(@RequestBody List<StockSymbol> stockSymbols){
        stockSymbolService.addSymbols(stockSymbols);
    }

    @GetMapping("/symbols")
    public List<StockSymbol> getSymbols(@RequestParam("query") String query){
        return stockSymbolService.getSymbols(query);
    }
}
