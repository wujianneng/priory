package com.infitack.rxretorfit2library;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ym on 2018/5/21.
 */

public class SentryLog {

    public static void sentryLogCapture(String username,int status, String api, String message) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String time = formatter.format(new Date(System.currentTimeMillis()));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", status);
            jsonObject.put("username", username);
            jsonObject.put("api", api);
            jsonObject.put("time", time);
            jsonObject.put("message", message);
            SentryUtils.logCapture(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sentryLogError(String username,int status, String api, String erromessage,String result) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        String time = formatter.format(new Date(System.currentTimeMillis()));
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("status", status);
            jsonObject.put("username", username);
            jsonObject.put("api", api);
            jsonObject.put("time", time);
            jsonObject.put("erromessage", erromessage);
            jsonObject.put("result", result);
            SentryUtils.logErrer(jsonObject.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}