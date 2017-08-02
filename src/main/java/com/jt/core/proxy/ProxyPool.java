package com.jt.core.proxy;

import com.jt.core.model.Proxy;

import java.util.Collection;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by he on 2017/8/1.
 */
public class ProxyPool {

    private ProxyPool() {
    }

    private static ProxyPool proxyPool = new ProxyPool();

    public static ProxyPool getInstance() {
        return proxyPool;
    }

    private BlockingQueue<Proxy> proxyQueue = new LinkedBlockingQueue<>();

    public void add(Proxy proxy) {
        proxyQueue.add(proxy);
    }

    public void addAll(Collection<Proxy> proxies) {
        proxyQueue.addAll(proxies);
    }

    public Proxy get() {
        return proxyQueue.poll();
    }
}
