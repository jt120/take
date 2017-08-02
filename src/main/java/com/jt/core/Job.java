package com.jt.core;

import com.jt.core.model.TakeURL;
import com.jt.core.scheduler.TimeScheduler;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.io.IOException;

/**
 * Created by he on 2017/8/1.
 */
public class Job implements Runnable {

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
                    Request req = new Request.Builder().url(url.httpUrl).build();
                    pageContext.setRequest(req);

                    timeScheduler.down(pageContext);
                    Thread.sleep(3000);
                } finally {
                    currentThread.setName(currentThreadName);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
