package com.jt.core;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ze.liu on 2017/8/1.
 */
public class JsoupTest {

    @Test
    public void test01() throws Exception {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://www.ip181.com/")
                .header("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:50.0) Gecko/20100101 Firefox/50.0")

                .build();
        Response response = client.newCall(request).execute();
//<meta charset='gb2312'>

        System.out.println(response.headers());
        byte[] bytes = response.body().bytes();
        String html = new String(bytes, Charset.defaultCharset());

        Document content = Jsoup.parse(html);
        String charset = Charset.defaultCharset().name();
        for (Element element : content.select("meta")) {
            String metaContent = element.attr("content");
            String metaCharset = element.attr("charset");
            if (StringUtils.isNotBlank(metaCharset)) {
                charset = metaCharset;
                System.out.println("from charset " + charset);
            } else if (metaContent.indexOf("charset") > -1) {
                charset = metaContent.substring(metaContent.indexOf("charset")).split("=")[1];
                System.out.println(charset);
            }

        }
        Document document = Jsoup.parse(new String(bytes, charset));

        Elements elements = document.select("table > tbody > tr");
        for (int i = 0; i < elements.size(); i++) {
            if (i == 0) {
                continue;
            }
            Element element = elements.get(i);
            System.out.println(element);
            Elements tds = element.getElementsByTag("td");
            System.out.println(tds.get(0).ownText());
            System.out.println(tds.get(1).ownText());
            System.out.println(tds.get(2).ownText());
            System.out.println(tds.get(3).ownText());
        }
    }

    @Test
    public void test02() throws Exception {
        String s = "8分钟7秒前";
        Pattern compile = Pattern.compile("(\\d+)分钟");
        Matcher matcher = compile.matcher(s);
        if (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    @Test
    public void test03() throws Exception {
        //以网易为例子
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder().url("http://www.hao123.com/qiwenqushi").build();
        Document doc = Jsoup.parse(client.newCall(request).execute().body().string());
        //获取后缀为png和jpg的图片的元素集合
        Elements pngs = doc.select("img[src~=(?i)\\.(png|jpe?g)]");
        //遍历元素
        for(Element e : pngs) {
            String src = e.attr("src");//获取img中的src路径
            System.out.println(src);
            HttpUrl link = request.url().resolve(src);
            if (link == null) continue; // URL is either invalid or its scheme isn't http/https.
            System.out.println("解析出的图片地址 " + link);

        }
    }
}
