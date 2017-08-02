package com.jt.stock;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import java.io.File;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by he on 2016/5/7.
 */
public class ParseFenHong {
    String p = ".*派(.*)元.*";
    Pattern compile = Pattern.compile(p);
    @Test
    public void test01() throws Exception {
        List<String> lines = Files.readLines(new File("e:/stock/fh.txt"), Charsets.UTF_8);
        Splitter splitter = Splitter.on("\t").omitEmptyStrings().trimResults();
        List<StockWithValue> stockWithValues = Lists.newArrayList();
        for (String line : lines) {
            List<String> v = splitter.splitToList(line);
            if (StringUtils.startsWith(v.get(0), "股票代码") || v.size() < 15) {
                continue;
            }
//            System.out.println(line);
            StockWithValue stockWithValue = new StockWithValue(
                    v.get(0),
                    v.get(1),
                    NumberUtils.toDouble(v.get(2)),
                    NumberUtils.toDouble(v.get(3)),
                    parse(v.get(5)),
                    NumberUtils.toDouble(v.get(7)),
                    NumberUtils.toDouble(v.get(8)),
                    v.get(10),
                    v.get(11),
                    NumberUtils.toDouble(v.get(15)));

            Matcher matcher = compile.matcher(stockWithValue.fenhong);
            if (matcher.find()) {
                stockWithValue.hong = NumberUtils.toDouble(matcher.group(1));
                if (stockWithValue.price <= 0) {
                    stockWithValue.lv = 0;
                } else {
                    stockWithValue.lv = stockWithValue.hong / (stockWithValue.price*10);
                }
            }
            stockWithValues.add(stockWithValue);
        }

        Collections.sort(stockWithValues);
        File file = new File("e:/stock/ret.txt");

        FileUtils.deleteQuietly(file);
        StringBuilder sb = new StringBuilder();
        sb.append("code").append("\t")
                .append("名字").append("\t")
                .append("涨跌").append("\t")
                .append("流通市值").append("\t")
                .append("利润").append("\t")
                .append("利润率").append("\t")
                .append("红率").append("\t")
                .append("红").append("\t")
                .append("登记日").append("\n");
        int count = 0;
        for (StockWithValue stockWithValue : stockWithValues) {
            if (count == 0) {
                FileUtils.write(file, sb.toString(), Charsets.UTF_8, true);
            }
            count++;
            if (stockWithValue.dengji != null && stockWithValue.dengji.compareTo(new Date()) < 0) {
                continue;
            }
            String retLine = stockWithValue.code + "\t" +
                    stockWithValue.name + "\t" +
                    stockWithValue.up + "\t" +
                    stockWithValue.shizhi + "\t" +
                    stockWithValue.lirun + "\t" +
                    stockWithValue.lirun / stockWithValue.zongshizhi + "\t" +
                    stockWithValue.lv + "\t" +
                    stockWithValue.hong + "\t" +
                    format(stockWithValue.dengji) + "\n";
            FileUtils.write(file, retLine, Charsets.UTF_8, true);
        }
//        System.out.println(stockWithValues);
    }

    private Date parse(String d) {
        Date date = null;
        try {
            date = DateUtils.parseDate(d.substring(0, 8), "yyyyMMdd");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return date;
    }

    private String format(Date date) {
        String s = "";
        try {
            s = DateFormatUtils.format(date, "yyyy-MM-dd");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return s;
    }


    private class StockWithValue implements Comparable {
        public final String code;
        public final String name; //名字
        public final double up; //涨跌
        public final double price; //价格
        public final Date dengji; //登记日
        public final double shizhi; //流通市值
        public final double zongshizhi; //总市值
        public final String hangye; //行业
        public final String fenhong; //分红
        public final double lirun; //利润
        public double lv; //分红率
        public double hong; //分红多少
        private StockWithValue(String code, String name, double up, double price, Date dengji, double shizhi, double zongshizhi,String hangye, String fenhong, double lirun) {
            this.code = code;
            this.name = name;
            this.up = up;
            this.price = price;
            this.dengji = dengji;
            this.shizhi = shizhi;
            this.zongshizhi = zongshizhi;
            this.hangye = hangye;
            this.fenhong = fenhong;
            this.lirun = lirun;
        }



        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("StockWithValue{");
            sb.append("name='").append(name).append('\'');
            sb.append(", price=").append(price);
            sb.append(", dengji=").append(dengji);
            sb.append(", shizhi=").append(shizhi);
            sb.append(", hangye='").append(hangye).append('\'');
            sb.append(", fenhong='").append(fenhong).append('\'');
            sb.append(", lirun=").append(lirun);
            sb.append('}');
            return sb.toString();
        }

        @Override
        public int compareTo(Object o) {
            StockWithValue v = (StockWithValue) o;
            return this.lv - v.lv < 0 ? 1 : -1;
        }
    }
}
