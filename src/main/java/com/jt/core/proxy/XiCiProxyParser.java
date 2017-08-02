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
 * parse xicidaili proxy
 * Created by he on 2017/8/1.
 */
@Managed(mapKey = "www.xicidaili.com")
public class XiCiProxyParser implements ProxyParser {


    @Override
    public List<Proxy> parse(String html) {
        Document document = Jsoup.parse(html);
        Elements elements = document.select("table[id=ip_list] tr[class]");
        List<Proxy> proxyList = new LinkedList<>();
        for (int i = 0; i < elements.size(); i++) {
            if (i == 0) {
                continue;
            }
            Element element = elements.get(i);
            String ip = element.select("td:eq(1)").first().text();
            String port  = element.select("td:eq(2)").first().text();
            String isAnonymous = element.select("td:eq(4)").first().text();
            Proxy p = new Proxy();
            p.setIp(ip);
            p.setPort(NumberUtils.toInt(port));
            p.setAnonymity(isAnonymous);
            proxyList.add(p);
        }
        return proxyList;
    }
}
