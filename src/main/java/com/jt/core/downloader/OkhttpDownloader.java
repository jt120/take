package com.jt.core.downloader;

import com.google.common.net.InetAddresses;
import com.jt.core.PageContext;
import com.jt.core.model.Proxy;
import com.jt.core.processor.HtmlProcessor;
import com.jt.core.proxy.ProxyPool;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;

/**
 * Created by he on 2017/7/31.
 */
public class OkhttpDownloader {

    private static final Logger LOGGER = LoggerFactory.getLogger(OkhttpDownloader.class);

    private OkHttpClient client;

    private HtmlProcessor htmlProcessor;

    private boolean useProxy;

    public OkhttpDownloader(OkHttpClient client, HtmlProcessor htmlProcessor) {
        this.htmlProcessor = htmlProcessor;
        this.client = client;
    }

    public void setUseProxy(boolean useProxy) {
        this.useProxy = useProxy;
    }

    public void down(PageContext context) {
        try {
            OkHttpClient useClient = this.client;
            if (useProxy) {
                Proxy proxy = ProxyPool.getInstance().get();
                if (proxy != null) {
                    LOGGER.info("使用代理抓取 {}", proxy);
                    ProxyPool.getInstance().add(proxy);
                    InetAddress inetAddress = InetAddresses.forString(proxy.getIp());
                    java.net.Proxy px = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(inetAddress, proxy.getPort()));
                    useClient = this.client.newBuilder().proxy(px).build();
                }

            }
            Request request = context.getRequest();
            Response response = useClient.newCall(request).execute();
            context.setResponse(response);
            int code = response.code();
            String contentType = response.header("Content-Type");
            if (code != 200 || contentType == null) {
                LOGGER.info("不合法的状态 {}", code);
                response.close();
                return;
            }

            MediaType mediaType = MediaType.parse(contentType);
            if (mediaType == null) {
                LOGGER.info("不清楚的类型 {}", mediaType);
                response.body().close();
                return;
            }
            context.setMediaType(mediaType);
            byte[] bytes = response.body().bytes();
            context.setBody(bytes);

            String charset = Charset.defaultCharset().name();
            if ("charset".indexOf(contentType) > -1) {
                charset = contentType.substring(contentType.indexOf("charset")).split("=")[1];
            } else {
                String html = new String(bytes, Charset.defaultCharset());

                Document content = Jsoup.parse(html);
                for (Element element : content.select("meta")) {
                    String metaContent = element.attr("content");
                    String metaCharset = element.attr("charset");
                    if (StringUtils.isNotBlank(metaCharset)) {
                        charset = metaCharset;
                    } else if (metaContent.indexOf("charset") > -1) {
                        charset = metaContent.substring(metaContent.indexOf("charset")).split("=")[1];
                    }
                }
            }
            context.setCharset(charset);
            context.setHtml(new String(bytes, charset));
            htmlProcessor.process(context);
        }catch (SocketTimeoutException e) {
            LOGGER.warn("timeout", e);
            this.down(context);
        }catch (IOException e) {
            e.printStackTrace();
        }

    }


}
