package com.jt.core.proxy;

import com.jt.core.model.Proxy;

import java.util.List;

/**
 * Created by he on 2017/8/1.
 */
public interface ProxyParser {

    List<Proxy> parse(String html);
}
