package com.infitack.rxretorfit2library;

public interface ModelGsonListener<T> {
    void onSuccess(T result) throws Exception;

    void onFailed(String erromsg);
}
