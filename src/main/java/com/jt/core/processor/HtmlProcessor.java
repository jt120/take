package com.jt.core.processor;

import com.jt.core.PageContext;
import com.jt.core.model.TakeURL;
import com.jt.core.pipeline.Pipeline;
import com.jt.core.scheduler.TimeScheduler;
import okhttp3.HttpUrl;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by he on 2017/7/31.
 */
public class HtmlProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlProcessor.class);

    private TimeScheduler timeScheduler;
    private Pipeline pipeline;

    public HtmlProcessor() {
    }

    public void setTimeScheduler(TimeScheduler timeScheduler, Pipeline pipeline) {
        this.timeScheduler = timeScheduler;
        this.pipeline = pipeline;
    }

    public void process(PageContext task) {
        Response response = task.getResponse();

        try {
            String subtype = task.getMediaType().subtype();
            if (subtype.equalsIgnoreCase("html") && task.getTakeURL().depth > 0) {
                String html = new String(task.getBody(), task.getCharset());
                task.setHtml(html);
                Document document = Jsoup.parse(html, task.getRequest().url().toString());
                //图片
                LOGGER.info("解析图片地址");
                for (Element element : document.select("img[src~=(?i)\\.(png|jpe?g)]")) {
                    String src=element.attr("src");//获取img中的src路径
                    HttpUrl link = response.request().url().resolve(src);
                    if (link == null) continue; // URL is either invalid or its scheme isn't http/https.
                    LOGGER.info("解析出的图片地址 {}", link);
                    timeScheduler.addUrl(new TakeURL(link.newBuilder().fragment(null).build(), 0));
                }

                //网页
                LOGGER.info("解析链接地址");
                for (Element element : document.select("a[href]")) {
                    String href = element.attr("href");
                    HttpUrl link = response.request().url().resolve(href);
                    if (link == null) continue; // URL is either invalid or its scheme isn't http/https.
                    timeScheduler.addUrl(new TakeURL(link.newBuilder().fragment(null).build(), task.getTakeURL().depth-1));
                }


            }
            pipeline.process(task);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
