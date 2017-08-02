package com.jt.core.proxy;

import com.google.common.collect.ImmutableMap;
import com.jt.core.annotations.Bind;
import com.jt.core.annotations.Managed;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by he on 2017/8/1.
 */
@Managed
public class ProxyParseFactory {

    //@Bind 可以在Field上，通过反射注入
    private Map<String, ProxyParser> factoryMap = new HashMap<>();

    //在方法上注入更容易
    @Bind
    public void registerParser(String key, ProxyParser proxyParser) {
        this.factoryMap.put(key, proxyParser);
    }

    public ProxyParser getParser(String host) {
        return factoryMap.get(host);
    }

    public Map<String, ProxyParser> getFactoryMap() {
        return ImmutableMap.copyOf(factoryMap);
    }
}
