package com.jt.core;

import com.google.common.net.InetAddresses;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import sun.net.spi.DefaultProxySelector;

import java.io.File;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;

import static org.junit.Assert.*;

public class TakeTest {

    @Test
    public void test01() throws Exception {
        System.out.println(System.getProperty("user.dir") + File.separator + "tmp");
    }

    @Test
    public void test02() throws Exception {
        HttpUrl parse = HttpUrl.parse("http://www.dapenti.com/blog/more.asp?name=xilei&id=123093");
        System.out.println(parse);
        System.out.println(parse.host());

        System.out.println(parse.uri());
        System.out.println(parse.encodedPath());
        System.out.println(parse.encodedQuery());
        System.out.println(parse.query());


        String s = DigestUtils.md5Hex(parse.toString());
        String s2 = DigestUtils.md5Hex(parse.toString());
        System.out.println(s);
        System.out.println(s2);
    }

    @Test
    public void test03() throws Exception {
        InetAddress inetAddress = InetAddresses.forString("42.178.17.57");
        InetSocketAddress inetSocketAddress = new InetSocketAddress(inetAddress, 8998);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, inetSocketAddress);
        OkHttpClient client = new OkHttpClient.Builder().proxy(proxy).build();
        Request request = new Request.Builder().url("http://www.cnblogs.com/xinz/archive/2011/11/27/2265425.html").build();
        Response execute = client.newCall(request).execute();
        System.out.println(execute.body().string());

        client.newBuilder().proxy(null).build();
    }

    @Test
    public void test04() throws Exception {
        InetAddress inetAddress = InetAddresses.forString("221.200.21.12");
        System.out.println(inetAddress);
    }

    @Test
    public void test05() throws Exception {
        Class<?> aClass = Class.forName("com.jt.core.proxy.IP181ProxyParser");
        System.out.println(aClass);
        System.out.println(aClass.getGenericSuperclass());
        System.out.println(aClass.getSuperclass());
        System.out.println(aClass.getGenericInterfaces()[0]);
    }

    @Test
    public void test06() throws Exception {
        BlockingQueue<String> queue = new LinkedBlockingQueue<>();
        queue.add("1");
        queue.add("2");
        queue.add("3");

        System.out.println(queue.poll());
        queue.add("4");
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
        System.out.println(queue.poll());
    }


}