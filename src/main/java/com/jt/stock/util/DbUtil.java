package com.jt.stock.util;

import org.nutz.dao.Dao;
import org.nutz.dao.impl.NutDao;
import org.nutz.dao.impl.SimpleDataSource;

/**
 * Created by he on 2016/4/30.
 */
public class DbUtil {
    private static SimpleDataSource dataSource = new SimpleDataSource();

    static {
        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/take");
        dataSource.setUsername("root");
        dataSource.setPassword("root");
    }

    public static Dao dao = new NutDao(dataSource);
}
