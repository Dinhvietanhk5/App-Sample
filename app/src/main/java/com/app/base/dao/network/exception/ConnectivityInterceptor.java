package com.app.base.dao.network.exception;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;


import com.socks.library.KLog;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;


public class ConnectivityInterceptor implements Interceptor {

    public ConnectivityInterceptor() {

    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if (!NetworkUtil.isOnline())
            throw new NoConnectivityException();

        Request request = chain.request();
        long t1 = System.nanoTime();
        String requestLog = String.format(" %s on %s%n%s",
                request.url(), chain.connection(), request.headers());

        Log.d("Request ", "" + request.method() + requestLog);
        String param = "";
        param = bodyToString(request);
        if (!TextUtils.isEmpty(param)) {
            if (param.contains("&")) {
                try {
                    Log.d("param", " ");
                    String[] srt = param.split("&");
                    for (String s : srt)
                        Log.d("", s);
                    Log.d(" ", " ");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else
                Log.d("param", param);
        }


        Response response = chain.proceed(request);
        long t2 = System.nanoTime();

        @SuppressLint("DefaultLocale")
        String responseLog = String.format("%s in %.1fms%n%s",
                response.request().url(), (t2 - t1) / 1e6d, response.headers());

        String bodyString = response.body().string();

        Log.d("Result ", "" + request.method() + " " + responseLog + " \n");
        KLog.json(bodyString);

        return response.newBuilder()
                .body(ResponseBody.create(response.body().contentType(), bodyString))
                .build();
    }

    public static String bodyToString(final Request request) {
        try {
            final Request copy = request.newBuilder().build();
            if (copy.body() != null) {
                final Buffer buffer = new Buffer();
                Objects.requireNonNull(copy.body()).writeTo(buffer);
                return buffer.readUtf8();
            } else return "";
        } catch (final IOException e) {
            return "";
        }
    }
}
