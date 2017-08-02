package com.jt.stock.producer;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.kevinsawicki.http.HttpRequest;
import com.jt.stock.model.Stock;
import com.jt.stock.util.JsonMapper;
import com.jt.stock.util.Links;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * Created by he on 2016/4/30.
 */
public class XueQiuStockProducer implements Supplier<List<Stock>> {

    private Map<String,String> getParams() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "1");
        params.put("size", "100");
        params.put("order", "desc");
        params.put("orderby", "percent");
        params.put("type", "11%2C12");
        return params;
    }

    @Override
    public List<Stock> get() {
        Map<String, String> params = getParams();
//        params.put("_", "1461851096446");
        HttpRequest request = HttpRequest.get(HttpRequest.append(Links.stock_list, params));
        request.header("Cookie", "xq_a_token=93b9123bccf67168e3adb0c07d89b9e1f6cc8db6;");

        String body = request.body();
        System.out.println(body);
        List<Stock> stockList = new ArrayList<>();
        try {
            JsonNode jsonNode = JsonMapper.objectMapper.readTree(body);
            JsonNode stocks = jsonNode.get("stocks");
            stocks.forEach(st -> {
                Stock stock = null;
                try {
                    stock = JsonMapper.objectMapper.readValue(st.toString(), Stock.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                stockList.add(stock);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stockList;
    }

    public static void main(String[] args) throws Exception {
        List<Stock> stockList = new XueQiuStockProducer().get();
        System.out.println(stockList.size());
    }
}
