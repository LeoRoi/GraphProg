package com.fxgraph.graph;

import java.io.Serializable;

public class EdgeWrapper implements Serializable {
    String source;
    String target;
    String text;

    public EdgeWrapper(String source, String target, String text) {
        this.source = source;
        this.target = target;
        this.text = text;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public String getText() {
        return text;
    }
}
