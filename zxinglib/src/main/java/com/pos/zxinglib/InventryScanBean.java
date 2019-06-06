package com.pos.zxinglib;

public class InventryScanBean {
    String scanResult;

    public InventryScanBean(String result){
        this.scanResult = result;
    }

    public String getScanResult() {
        return scanResult;
    }

    public void setScanResult(String scanResult) {
        this.scanResult = scanResult;
    }
}
