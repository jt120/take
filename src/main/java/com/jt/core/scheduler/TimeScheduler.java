package com.jt.core.scheduler;

import com.jt.core.Job;
import com.jt.core.PageContext;
import com.jt.core.downloader.OkhttpDownloader;
import com.jt.core.model.TakeURL;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by he on 2017/7/31.
 */
public class TimeScheduler implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeScheduler.class);

    private final LinkedBlockingQueue<TakeURL> waitedURL = new LinkedBlockingQueue<>();
    private final Set<TakeURL> httpUrlSet = Collections.synchronizedSet(new LinkedHashSet<>());
    private OkhttpDownloader okhttpDownloader;
    private ExecutorService executorService;
    private int jobs;

    public TimeScheduler(OkhttpDownloader okhttpDownloader, int jobs) {
        this.jobs = jobs;
        this.executorService = Executors.newFixedThreadPool(jobs);
        this.okhttpDownloader = okhttpDownloader;
        LOGGER.info("启动了 {} job", jobs);
    }

    public void addUrl(TakeURL url) {
        if (!httpUrlSet.add(url)) {
            return;
        }
        LOGGER.info("添加url {}", url);
        waitedURL.add(url);
    }

    public TakeURL take() throws InterruptedException {
        return waitedURL.take();
    }

    public void down(PageContext pageContext) throws IOException {
        okhttpDownloader.down(pageContext);

    }

    public void startJob() {
        for (int i = 0; i < jobs; i++) {
            executorService.submit(new Job(this));
        }
    }

    @Override
    public void close() throws IOException {
        executorService.shutdown();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
        }
    }


}
