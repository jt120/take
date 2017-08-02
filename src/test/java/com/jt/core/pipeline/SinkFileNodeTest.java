package com.jt.core.pipeline;

import com.google.common.io.Files;
import okhttp3.*;
import org.junit.Test;

import java.io.File;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Created by ze.liu on 2017/8/1.
 */
public class SinkFileNodeTest {

    @Test
    public void test01() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("https://upload.wikimedia.org/wikipedia/commons/thumb/6/66/Colorado_state_coat_of_arms_%28illustrated%2C_1876%29.jpg/280px-Colorado_state_coat_of_arms_%28illustrated%2C_1876%29.jpg").build();
        Response response = client.newCall(request).execute();
        String contentType = response.header("Content-Type");
        MediaType parse = MediaType.parse(contentType);
        System.out.println(parse);
        byte[] bytes = response.body().bytes();
        Files.write(bytes, new File("d:/test/1.jpg"));

    }

    @Test
    public void test02() throws Exception {
        String s = "content-type:text/html; charset=UTF-8";
        System.out.println(s.substring(s.indexOf("charset")+8));
    }

    @Test
    public void test03() throws Exception {
        HttpUrl parse = HttpUrl.parse("http://www.ip181.com/");
        System.out.println(parse.host());
        System.out.println(parse.uri());
        System.out.println(parse.url());
        System.out.println(parse.encodedPath());
    }



}