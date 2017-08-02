package com.jt.core.pipeline;

import com.jt.core.PageContext;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by he on 2017/8/1.
 */
public class Pipeline {

    private List<Node> nodeList = new LinkedList<>();

    public void addNode(Node node) {
        this.nodeList.add(node);
    }

    public void process(PageContext pageContext) {
        for (Node node : nodeList) {
            node.process(pageContext);
        }
    }

}
