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
import okhttp3.Cache;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

import java.io.File;
import java.util.concurrent.TimeUnit;

/**
 * Created by he on 2017/7/31.
 */
public class Take {

    public static final String path = System.getProperty("user.home") + File.separator + "tmp";

    public static void main(String[] args) throws Exception {
        //load class
        ContextLoader contextLoader = ContextLoader.getInstance();
        contextLoader.loadClass("com.jt.core.proxy");

        //create single okHttpClient
        int cacheSize = 1024 * 10240;
        File cacheFile = new File(System.getProperty("user.home") + File.separator + "cache");
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(3, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .cache(new Cache(cacheFile, cacheSize))
                .build();

        //new Processor
        HtmlProcessor htmlProcessor = new HtmlProcessor();

        //new Downloader
        OkhttpDownloader downloader = new OkhttpDownloader(client, htmlProcessor);
        downloader.setUseProxy(true);

        //init pipeline
        Pipeline pipeline = new Pipeline();
        pipeline.addNode(new SinkFileNode());
        ProxyNode proxyNode = new ProxyNode();
        proxyNode.setProxyParseFactory(contextLoader.getBean(ProxyParseFactory.class));
        pipeline.addNode(proxyNode);

        //start scheduler, auto close
        try (TimeScheduler timeScheduler = new TimeScheduler(downloader, 5)) {
            htmlProcessor.setTimeScheduler(timeScheduler, pipeline);
            timeScheduler.addUrl(new TakeURL(HttpUrl.parse("http://www.ip181.com/"), 0));
            for (int i = 1; i <= 5; i++) {
                timeScheduler.addUrl(new TakeURL(HttpUrl.parse("http://www.xicidaili.com/wt/" + i + ".html"), 0));
                timeScheduler.addUrl(new TakeURL(HttpUrl.parse("http://www.xicidaili.com/nn/" + i + ".html"), 0));
                timeScheduler.addUrl(new TakeURL(HttpUrl.parse("http://www.xicidaili.com/wn/" + i + ".html"), 0));
                timeScheduler.addUrl(new TakeURL(HttpUrl.parse("http://www.xicidaili.com/nt/" + i + ".html"), 0));
            }
            timeScheduler.addUrl(new TakeURL(HttpUrl.parse("http://www.hao123.com/qiwenqushi"), 1));
            timeScheduler.startJob();
        }
    }


}
