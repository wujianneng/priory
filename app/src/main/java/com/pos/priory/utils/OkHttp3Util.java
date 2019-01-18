package com.pos.priory.utils;

import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


/**
 * Created by Lenovo on 2018/11/5.
 */

public class OkHttp3Util {

    private static OkHttpClient okHttpClient = null;


    private OkHttp3Util() {
    }

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            //加同步安全
            synchronized (OkHttp3Util.class) {
                if (okHttpClient == null) {
                    //okhttp可以缓存数据....指定缓存路径
                    File sdcache = new File(Environment.getExternalStorageDirectory(), "cache");
                    //指定缓存大小
                    int cacheSize = 10 * 1024 * 1024;

                    okHttpClient = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.MINUTES)
                            .connectionPool(new ConnectionPool(4, 60, TimeUnit.SECONDS))
                            .readTimeout(30, TimeUnit.MINUTES)
                            .writeTimeout(30, TimeUnit.MINUTES)
                            .sslSocketFactory(SSLSocketClient.getSSLSocketFactory())
                            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
//                            .cache(new Cache(sdcache.getAbsoluteFile(), cacheSize))//设置缓存
                            .build();
                }
            }

        }
        return okHttpClient;
    }

    public static Call doGet(String url, Okhttp3StringCallback callback) {
        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(callback);

        return call;
    }

    public static Call doGet(String url, Callback callback) {

        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(callback);

        return call;
    }

    public static Call doGetWithHeaders(String url, Headers headers, Callback callback) {

        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(callback);
        return call;
    }

    public static Call doGetWithHeaders(String url, Headers headers, Okhttp3StringCallback callback) {

        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(callback);
        return call;
    }

    public static Call doGetWithToken(String url, SharedPreferences sharedPreferences, Okhttp3StringCallback callback) {
        Log.e("doGeturl","url:" + url);
        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .addHeader(Constants.Authorization_KEY,
                        "Token " + sharedPreferences.getString(Constants.Authorization_KEY, ""))
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(callback);
        return call;
    }

    public static Call doGetWithHeaders(String url, Headers headers, Okhttp3BitmapCallback callback) {
        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(callback);
        return call;
    }

    public static Call doGetWithHeaders(String url, Headers headers, Okhttp3Callback callback) {

        //创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .headers(headers)
                .build();
        //得到Call对象
        Call call = okHttpClient.newCall(request);
        //执行异步请求
        call.enqueue(callback);
        return call;
    }


    public static Call doPost(String url, String paramString, Callback callback) {

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPost(String url, String paramString, Okhttp3StringCallback callback) {
        Log.e("dopost", "paramstring:" + paramString);

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPostWithToken(String url, String paramString, SharedPreferences sharedPreferences, Okhttp3StringCallback callback) {
        Log.e("dopost", "paramstring:" + paramString);

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .addHeader(Constants.Authorization_KEY,
                        "Token " + sharedPreferences.getString(Constants.Authorization_KEY, ""))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPost(String url, String paramString, Okhttp3Callback callback) {

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPostWithHeaders(String url, String paramString, Headers headers, Callback callback) {

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .headers(headers)
                .post(requestBody)
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPostWithHeaders(String url, String paramString, Headers headers, Okhttp3StringCallback callback) {

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        if (headers != null) {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(requestBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }


    public static Call doPostWithHeaders(String url, String paramString, Headers headers, Okhttp3Callback callback) {

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        if (headers != null) {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(requestBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPostWithHeaders(String url, FormBody formBody, Headers headers, Callback callback) {

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        if (headers != null) {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(formBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
        }
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPostWithHeaders(String url, FormBody formBody, Headers headers, Okhttp3StringCallback callback) {

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        if (headers != null) {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(formBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
        }

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPostWithHeaders(String url, FormBody formBody, Headers headers, Okhttp3Callback callback) {

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        if (headers != null) {
            request = new Request.Builder()
                    .url(url)
                    .headers(headers)
                    .post(formBody)
                    .build();
        } else {
            request = new Request.Builder()
                    .url(url)
                    .post(formBody)
                    .build();
        }

        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doDeleteWithToken(String url, SharedPreferences sharedPreferences, Okhttp3StringCallback callback) {
//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader(Constants.Authorization_KEY,
                        "Token " + sharedPreferences.getString(Constants.Authorization_KEY, ""))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPatchWithToken(String url, String paramString, SharedPreferences sharedPreferences, Okhttp3StringCallback callback) {
        Log.e("dopost", "paramstring:" + paramString);

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .patch(requestBody)
                .addHeader(Constants.Authorization_KEY,
                        "Token " + sharedPreferences.getString(Constants.Authorization_KEY, ""))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }

    public static Call doPutWithToken(String url, String paramString, SharedPreferences sharedPreferences, Okhttp3StringCallback callback) {
        Log.e("dopost", "paramstring:" + paramString);

//创建OkHttpClient请求对象
        OkHttpClient okHttpClient = getInstance();
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), paramString);
        //创建Request
        Request request = null;
        request = new Request.Builder()
                .url(url)
                .put(requestBody)
                .addHeader(Constants.Authorization_KEY,
                        "Token " + sharedPreferences.getString(Constants.Authorization_KEY, ""))
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
        return call;
    }


}
