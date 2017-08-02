package com.jt.stock.qq;


import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class QQFetcherTest {

    @Test
    public void test01() throws Exception {
        String url = "http://stock.gtimg.cn/data/view/flow.php?t=2";
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "gbk"));
        String s = null;
        while ((s = br.readLine()) != null) {
            System.out.println(s);
            System.out.println("===");
        }

        conn.disconnect();
    }

    public static String readUrl(String url, String encoding) {
        StringBuilder sb = new StringBuilder();
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), encoding));
            String s = null;
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }

            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}