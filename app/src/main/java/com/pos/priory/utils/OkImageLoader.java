package com.pos.priory.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;
import android.widget.ImageView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

public class OkImageLoader {
    private Context mContext;
    private String imageUrl;
    private int defRes = -1, errorRes = -1;
    private static Map<ImageView, String> tags = new HashMap<>();
    private static Map<ImageView, Call> callMap = new HashMap<>();
    private MemoryCache lruCache;

    public OkImageLoader(Context context) {
        mContext = context;
        lruCache = MemoryCache.getInstance();
    }

    public static OkImageLoader with(Context context) {
        return new OkImageLoader(context);
    }

    public OkImageLoader load(String url) {
        imageUrl = url;
        return this;
    }

    public OkImageLoader placeholder(int res) {
        defRes = res;
        return this;
    }

    public OkImageLoader error(int res) {
        errorRes = res;
        return this;
    }

    public void into(final ImageView imageView) {
        if (!imageUrl.equals(tags.get(imageView))) {
            if (callMap.get(imageView) != null) {
                callMap.get(imageView).cancel();
                callMap.remove(imageView);
            }
            tags.put(imageView, imageUrl);
        }
        Bitmap bitmap = getBitmapFromCache(imageUrl);
        if (bitmap == null) {
            Log.e("test", "doGet:" + imageUrl);
            Call call = OkHttp3Util.doGet(imageUrl, new Okhttp3BitmapCallback() {
                @Override
                public void onSuccess(Bitmap results) throws Exception {
                    if (lruCache.getBitmapFromMemoryCache(imageUrl) == null) {
                        lruCache.addBitmapToMemoryCache(imageUrl,results);
                    }

                    imageView.setImageBitmap(results);
                    callMap.remove(imageView);
                }

                @Override
                public void onFailed(String erromsg) {
                    if (errorRes != -1)
                        imageView.setImageResource(errorRes);
                    callMap.remove(imageView);
                }
            });
            callMap.put(imageView, call);
        } else {
            Log.e("test", "cacheHas:" + imageUrl);
            imageView.setImageBitmap(bitmap);
        }
        Log.e("test", "callMap:" + callMap.size() + " lruCache:" + lruCache.getSize());
    }

    public static void clear() {
        callMap.clear();
        tags.clear();
    }

    private Bitmap getBitmapFromCache(String imageUrl) {
        Log.e("test", "getBitmapFromCache:" + imageUrl);
        return lruCache.getBitmapFromMemoryCache(imageUrl);
    }

    public static class MemoryCache {
        private static MemoryCache instance;
        private LruCache<String, Bitmap> mMemoryCache;

        private MemoryCache() {
            //获取当前进程的可用内存
            int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            int cacheSize = maxMemory / 8;
            mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    //完成bitmap对象大小的计算
                    return bitmap.getRowBytes() * bitmap.getHeight() / 1024;
                }
            };
        }

        public int getSize(){
            return mMemoryCache.putCount();
        }

        public static MemoryCache getInstance() {
            if (instance == null) {
                synchronized (MemoryCache.class) {
                    if (instance == null) {
                        instance = new MemoryCache();
                    }
                }
            }
            return instance;
        }
        public void addBitmapToMemoryCache(String uri, Bitmap bitmap) {
            mMemoryCache.put(uri, bitmap);
        }

        public Bitmap getBitmapFromMemoryCache(String key) {
            return mMemoryCache.get(key);
        }


    }
}
