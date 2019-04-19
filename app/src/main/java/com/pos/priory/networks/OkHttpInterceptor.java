package com.pos.priory.networks;

import android.util.Log;

import com.blankj.utilcode.util.NetworkUtils;
import com.pos.priory.MyApplication;
import com.pos.priory.utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by kaka on 2018/3/15.
 * email:375120706@qq.com
 */

public class OkHttpInterceptor implements Interceptor {
    private static final String TAG = "okHttp";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request original = chain.request();

        Request request = original.newBuilder()
                .header(Constants.Authorization_KEY, MyApplication.authorization)
                .method(original.method(), original.body())
                .build();
         Response response = chain.proceed(request);;
         Log.e(TAG,request.url() + "  respone:" + response.body().string());
        return chain.proceed(request);

    }

}
