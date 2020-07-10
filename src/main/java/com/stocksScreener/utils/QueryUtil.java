package com.stocksScreener.utils;

import org.influxdb.dto.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class QueryUtil {

    public static Query formHistoryQuery(String symbol, String period) {
        // TODO check if symbol is available
        String queryString = "SELECT * FROM %s WHERE %s";

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String predicate;

        // TODO implement more date ranges
        switch (period) {
            case "1d":
                predicate = String.format("time>='%s 09:30:00'", df.format(new Date()));
                break;
            case "5d":
                predicate = null;
                break;
            default:
                predicate = "";
        }

        return new Query(String.format(queryString, symbol, predicate));
    }

    public static Query formLastPriceQuery(List<String> symbols){
        // TODO check if symbol is available
        return new Query("SELECT last(*) FROM " + String.join(",", symbols));
    }
}
