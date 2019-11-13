package com.infitack.rxretorfit2library;

public interface ModelListener {
    void onSuccess(String result) throws Exception;

    void onFailed(String erromsg);
}
