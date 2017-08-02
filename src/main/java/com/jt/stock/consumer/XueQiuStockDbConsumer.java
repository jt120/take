package com.jt.stock.consumer;

import com.jt.stock.model.Stock;
import com.jt.stock.util.DbUtil;

import java.util.List;
import java.util.function.Consumer;

/**
 * Created by he on 2016/4/30.
 */
public class XueQiuStockDbConsumer implements Consumer<List<Stock>> {


    @Override
    public void accept(List<Stock> stocks) {
        DbUtil.dao.insert(stocks);
    }
}
