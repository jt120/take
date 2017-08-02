package com.jt.core;

import com.jt.core.model.TakeURL;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by he on 2017/7/31.
 */
public class PageContext {

    private TakeURL takeURL;
    private Request request;
    private Response response;
    private byte[] body;
    private String charset;
    private String html;
    private MediaType mediaType;

    public TakeURL getTakeURL() {
        return takeURL;
    }

    public void setTakeURL(TakeURL takeURL) {
        this.takeURL = takeURL;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }
}
