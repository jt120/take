package com.jt.core;

import com.jt.core.downloader.OkhttpDownloader;
import com.jt.core.loader.ContextLoader;
import com.jt.core.model.TakeURL;
import com.jt.core.pipeline.Pipeline;
import com.jt.core.pipeline.ProxyNode;
import com.jt.core.pipeline.SinkFileNode;
import com.jt.core.processor.HtmlProcessor;
import com.jt.core.proxy.ProxyParseFactory;
import com.jt.core.scheduler.TimeScheduler;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.io.File;

/**
 * Created by he on 2017/7/31.
 */
public class Take {

    public static final String path = System.getProperty("user.dir") + File.separator + "tmp";

    public static void main(String[] args) throws Exception {
        ContextLoader contextLoader = ContextLoader.getInstance();
        contextLoader.loadClass("com.jt.core.proxy");

        OkHttpClient client = new OkHttpClient.Builder().build();

        HtmlProcessor htmlProcessor = new HtmlProcessor();

        OkhttpDownloader downloader = new OkhttpDownloader(client, htmlProcessor);
        downloader.setUseProxy(true);

        Pipeline pipeline = new Pipeline();
        pipeline.addNode(new SinkFileNode());
        ProxyNode proxyNode = new ProxyNode();
        proxyNode.setProxyParseFactory(contextLoader.getBean(ProxyParseFactory.class));
        pipeline.addNode(proxyNode);

        try (TimeScheduler timeScheduler = new TimeScheduler(downloader, 3)) {
            htmlProcessor.setTimeScheduler(timeScheduler, pipeline);
            TakeURL proxyUrl = new TakeURL(HttpUrl.parse("http://www.ip181.com/"), 0);
            TakeURL blogUrl = new TakeURL(HttpUrl.parse("http://www.hao123.com/qiwenqushi"), 1);
            timeScheduler.addUrl(proxyUrl);
            timeScheduler.addUrl(blogUrl);
            timeScheduler.startJob();
        }
    }


}
