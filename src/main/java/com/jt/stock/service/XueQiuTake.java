package com.jt.stock.service;

import com.jt.stock.consumer.XueQiuStockDbConsumer;
import com.jt.stock.model.Stock;
import com.jt.stock.producer.XueQiuStockProducer;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by he on 2016/4/30.
 */
public class XueQiuTake {

    public static void main(String[] args) throws Exception {
        Supplier<List<Stock>> xueqiuSupplier = new XueQiuStockProducer();
        List<Stock> stockList = xueqiuSupplier.get();
        Consumer<List<Stock>> xueqiuConsumer = new XueQiuStockDbConsumer();
        xueqiuConsumer.accept(stockList);
    }
}
