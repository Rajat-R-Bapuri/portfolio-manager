package com.stocksScreener.controller;

import com.stocksScreener.model.StockPrice;
import com.stocksScreener.utils.QueryUtil;
import org.influxdb.InfluxDB;
import org.influxdb.dto.QueryResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/stocks")
@CrossOrigin("*")
public class StockPriceController {

    private final InfluxDB influxDB;

    @Autowired
    public StockPriceController(@Qualifier("createDBConnection") InfluxDB influxDB) {
        this.influxDB = influxDB;
    }

    @GetMapping("/{symbol}/price")
    public Mono<Map<String, Float>> getStockPrice(@PathVariable("symbol") String symbol) {
        return null;
    }

    @GetMapping("/{symbol}/history/{period}")
    public Map<String, List<StockPrice>> getStockHistory(@PathVariable("symbol") String symbol, @PathVariable("period") String period) {

        Map<String, List<StockPrice>> result = new HashMap<>();
        List<StockPrice> stockHistory = new ArrayList<>();

        QueryResult queryResult = influxDB.query(QueryUtil.formHistoryQuery(symbol, period));

        System.out.println(queryResult);

        for (QueryResult.Result qr : queryResult.getResults()) {
            for (QueryResult.Series series1 : qr.getSeries()) {
                for (List<Object> x : series1.getValues()) {
                    StockPrice sd = new StockPrice((String) x.get(0), (Double) x.get(1));
                    stockHistory.add(sd);
                }
            }
        }

        result.put("data", stockHistory);

        return result;
    }

    @GetMapping("/price")
    public Flux<Map<String, StockPrice>> getStockPrices(@RequestParam("symbols") List<String> symbols) {
        return Flux
                .interval(Duration.ofSeconds(6))
                .map(symbol -> {
                    QueryResult queryResult = influxDB.query(QueryUtil.formLastPriceQuery(symbols));
                    System.out.println(queryResult);
                    Map<String, StockPrice> response = new HashMap<>();

                    for (QueryResult.Result qr : queryResult.getResults()) {
                        List<QueryResult.Series> series = qr.getSeries();

                        for (QueryResult.Series series1 : series) {
                            StockPrice sd = new StockPrice((String) series1.getValues().get(0).get(0), (Double) series1.getValues().get(0).get(1));
                            response.put(series1.getName(), sd);
                            System.out.println(response);
                        }
                    }
                    return response;
                });
    }
}