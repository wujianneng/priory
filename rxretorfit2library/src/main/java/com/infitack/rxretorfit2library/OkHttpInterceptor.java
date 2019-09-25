package com.infitack.rxretorfit2library;

import android.util.Log;


import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpHeaders;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by kaka on 2018/3/15.
 * email:375120706@qq.com
 */

public class OkHttpInterceptor implements Interceptor {
    private static final String TAG = "okHttp";
    private static final Charset UTF8 = Charset.forName("UTF-8");


    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();
        Request request = null;
        Log.e(TAG, "headerKey：" + RetrofitManager.headerKey + " headerValue:" + RetrofitManager.headerValue);
        if (RetrofitManager.headerKey.equals("")) {
            request = original.newBuilder()
                    .method(original.method(), original.body())
                    .build();
        } else {
            request = original.newBuilder()
                    .header(RetrofitManager.headerKey, RetrofitManager.headerValue)
                    .method(original.method(), original.body())
                    .build();
        }

        Response response = chain.proceed(request);
        ////
        //打印response
        ResponseBody responseBody = response.body();
        long contentLength = responseBody.contentLength();
        //注意 >>>>>>>>> okhttp3.4.1这里变成了 !HttpHeader.hasBody(response)
        //if (!HttpEngine.hasBody(response)) {
        if (!HttpHeaders.hasBody(response)) { //HttpHeader -> 改成了 HttpHeaders，看版本进行选择
            //END HTTP
            Log.e(TAG, "HttpHeaders.hasBody(response)");
            if (!response.isSuccessful()) {
                SentryLog.sentryLogError(!RetrofitManager.sentryUsername.equals("") ? RetrofitManager.sentryUsername : "nouser", 0, "", response.message(), "");
            }
        } else if (bodyEncoded(response.headers())) {
            //HTTP (encoded body omitted)
            Log.e(TAG, "bodyEncoded(response.headers())");
            if (!response.isSuccessful()) {
                SentryLog.sentryLogError(!RetrofitManager.sentryUsername.equals("") ? RetrofitManager.sentryUsername : "nouser", 0, "", response.message(), "");
            }
        } else {
            BufferedSource source = responseBody.source();
            source.request(Long.MAX_VALUE); // Buffer the entire body.
            Buffer buffer = source.buffer();

            Charset charset = UTF8;
            MediaType contentType = responseBody.contentType();
            if (contentType != null) {
                try {
                    charset = contentType.charset(UTF8);
                } catch (UnsupportedCharsetException e) {
                    //Couldn't decode the response body; charset is likely malformed.
                    return response;
                }
            }
            if (!isPlaintext(buffer)) {
                return response;
            }
            String result = "";
            if (contentLength != 0) {
                result = buffer.clone().readString(charset);
                Log.e(TAG, request.url() + "  respone:" + result);
                //获取到response的body的string字符串
                //do something .... <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
            }
            Log.e(TAG, "else" + response.code());
            if (!response.isSuccessful()) {
                Log.e(TAG, "else!response.isSuccessful()" + response.code());
                SentryLog.sentryLogError(!RetrofitManager.sentryUsername.equals("") ? RetrofitManager.sentryUsername : "nouser", 0, request.url().toString(), response.message(), result);
                Log.e(TAG, "sentryLogErrorSuccessful");
            }

        }
        //打印response
        ////

        return response;
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    private boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

}
