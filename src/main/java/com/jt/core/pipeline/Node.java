package com.jt.core.pipeline;

import com.jt.core.PageContext;

/**
 * Created by he on 2017/8/1.
 */
public interface Node {

    void process(PageContext pageContext);
}
