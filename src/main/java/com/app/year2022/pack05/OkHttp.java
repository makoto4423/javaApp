package com.app.year2022.pack05;

import okhttp3.*;

import java.io.IOException;
import java.util.Map;

public class OkHttp {

    public OkHttp(){

    }

    public Resp send(String option, String url) {
        return send(option, url, null, null);
    }

    public Resp send(String option, String url, Map<String, String> head) {
        return send(option, url, head, null);
    }

    public Resp send(String option, String url, String body) {
        return send(option, url, null, body);
    }

    public Resp send(String option, String url, Map<String, String> head, String body) {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Request.Builder builder = new Request.Builder();
        if (body != null) {
            MediaType mediaType = MediaType.parse("application/json");
            builder.url(url).method(option, RequestBody.create(body, mediaType));
        } else {
            builder.url(url).method(option, null);
        }
        if (head != null) {
            head.forEach(builder::header);
        }
        Resp ans = new Resp();
        try (Response resp = client.newCall(builder.build()).execute()) {
            ans.code = resp.code();
            ans.body = resp.body() == null ? null : resp.body().string();
        } catch (IOException e) {
            ans.code = 404;
            ans.body = e.getMessage();
        }
        return ans;
    }


    public static class Resp {
        public int code;
        public String body;
    }

}
