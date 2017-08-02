package com.jt.core;

import com.jt.core.model.TakeURL;
import com.jt.core.scheduler.TimeScheduler;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Created by he on 2017/8/1.
 */
public class Job implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Job.class);

    private TimeScheduler timeScheduler;

    public Job(TimeScheduler timeScheduler) {
        this.timeScheduler = timeScheduler;
    }

    @Override
    public void run() {
        try {
            for (TakeURL url; (url = timeScheduler.take()) != null; ) {
                Thread currentThread = Thread.currentThread();
                String currentThreadName = currentThread.getName();
                try {
                    currentThread.setName("job-" + url);
                    PageContext pageContext = new PageContext();
                    pageContext.setTakeURL(url);
                    LOGGER.info("start take {}", url);
                    Request req = new Request.Builder()
                            .url(url.httpUrl)
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36")
                            .build();
                    pageContext.setRequest(req);

                    timeScheduler.down(pageContext);
                    Thread.sleep(2000);
                } finally {
                    currentThread.setName(currentThreadName);
                }
            }
            LOGGER.info("抓取结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
