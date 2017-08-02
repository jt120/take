package com.jt.stock;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by he on 2016/4/27.
 */
public class UpTest {

    @Test
    public void test01() throws Exception {
        List<String> lines = FileUtils.readLines(new File("E:\\stock\\up.txt"));
        List<Stock> stocks = new ArrayList<>();
        for (String line : lines) {
            System.out.println(line);
            if (line.startsWith("股票")) {
                continue;
            }
            String[] split = line.split("\\t");
            String name = split[0];
            String up = split[3];
            String hangye = split[4].split("\\-")[0];
            stocks.add(new Stock(name, up, hangye));
        }

        FileUtils.writeLines(new File("e:/stock/up2.txt"), stocks);
        System.out.println(stocks);
    }

    @Test
    public void test02() throws Exception {
        String hello = "hello java";
        System.out.println(hello.indexOf('o'));
    }

    static class Stock {
        public final String name;
        public final String up;
        public final String hangye;

        Stock(String name, String up, String hangye) {
            this.name = name;
            this.up = up;
            this.hangye = hangye;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("");
            sb.append(name).append('\t');
            sb.append(up).append('\t');
            sb.append(hangye).append('\t');
            return sb.toString();
        }
    }
}
