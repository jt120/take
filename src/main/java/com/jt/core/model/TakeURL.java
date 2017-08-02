package com.jt.core.model;

import okhttp3.HttpUrl;

/**
 * Created by he on 2017/8/1.
 */
public class TakeURL extends BaseModel {

    public final HttpUrl httpUrl;
    //depth for take
    public final int depth;

    public TakeURL(HttpUrl httpUrl, int depth) {
        this.httpUrl = httpUrl;
        this.depth = depth;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TakeURL takeURL = (TakeURL) o;

        if (!httpUrl.equals(takeURL.httpUrl)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return httpUrl.hashCode();
    }
}
