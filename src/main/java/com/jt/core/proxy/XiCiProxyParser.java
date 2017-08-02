package com.jt.core.proxy;

import com.jt.core.annotations.Managed;
import com.jt.core.model.Proxy;
import org.apache.commons.lang3.math.NumberUtils;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by he on 2017/8/1.
 */
@Managed(mapKey = "www.xicidaili.com")
public class XiCiProxyParser implements ProxyParser {


    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table > tbody > tr");
        List<Proxy> proxyList = new LinkedList<>();
        for (int i = 0; i < elements.size(); i++) {
            if (i == 0) {
                continue;
            }
            Element element = elements.get(i);
            Elements tds = element.getElementsByTag("td");
            Proxy proxy = new Proxy();
            proxyList.add(proxy);
            proxy.setIp(tds.get(0).ownText());
            proxy.setPort(NumberUtils.toInt(tds.get(1).ownText()));
            proxy.setAnonymity(tds.get(2).ownText());
            proxy.setProtocol(tds.get(3).ownText());
            proxy.setSpeed(tds.get(4).ownText());
            proxy.setSource(tds.get(5).ownText());
            Pattern compile = Pattern.compile("(\\d+)分钟");
            Matcher matcher = compile.matcher(tds.get(6).ownText());
            if (matcher.find()) {
                String group = matcher.group(1);
                DateTime time = DateTime.now().minusMinutes(NumberUtils.toInt(group));
                proxy.setUpdateTime(time.toDate());
            }
        }
        return proxyList;
    }
}
