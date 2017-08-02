package com.jt.core.pipeline;

import com.jt.core.PageContext;
import com.jt.core.model.Proxy;
import com.jt.core.proxy.ProxyParseFactory;
import com.jt.core.proxy.ProxyParser;
import com.jt.core.proxy.ProxyPool;
import okhttp3.HttpUrl;
import okhttp3.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by ze.liu on 2017/8/1.
 */
public class ProxyNode implements Node {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProxyNode.class);

    private ProxyParseFactory proxyParseFactory;

    public void setProxyParseFactory(ProxyParseFactory proxyParseFactory) {
        this.proxyParseFactory = proxyParseFactory;
    }

    @Override
    public void process(PageContext pageContext) {
        Request request = pageContext.getRequest();
        HttpUrl url = request.url();
        ProxyParser parser = proxyParseFactory.getParser(url.host());
        if (parser != null) {
            List<Proxy> parse = parser.parse(pageContext.getHtml());
            LOGGER.info("proxy {}", parse);
            ProxyPool.getInstance().addAll(parse);
        }

    }
}
