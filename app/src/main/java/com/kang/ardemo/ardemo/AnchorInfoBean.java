package com.kang.ardemo.ardemo;

import com.google.ar.core.Anchor;

public class AnchorInfoBean {
    String dataText;
    Anchor anchor;
    Double length;

    public AnchorInfoBean(String dataText, Anchor anchor, Double length) {
        this.dataText = dataText;
        this.anchor = anchor;
        this.length = length;
    }

    public String getDataText() {
        return dataText;
    }

    public void setDataText(String dataText) {
        this.dataText = dataText;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public Double getLength() {
        return length;
    }

    public void setLength(Double length) {
        this.length = length;
    }
}
