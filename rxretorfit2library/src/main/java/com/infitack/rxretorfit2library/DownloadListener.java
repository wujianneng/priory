package com.infitack.rxretorfit2library;

import java.io.File;

public interface DownloadListener {

    void onProgress(float percent);

    void onDownloadComplete(File file);

    void onError(String error);
}
