package com.pos.priory.utils;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class Constants {
    //生产环境域名
//    public static final String BASE_URL = "http://api.annabellaip.com";

    //开发环境域名
    public static final String BASE_URL = "http://annabella.infitack.cn";

    public static final String BASE_URL_HTTP = BASE_URL;

    public static final String SENTRY_DNS = "https://16fbe74588aa409eafdb10e6a94bc827:39bbbc909aec4afba24523c286bbc926@sentry.io/1481689";

    public static final String LAST_BASE_URL_KEY = "lastBaseUrlKey";
    public static final String LAST_USERNAME_KEY = "lastUsernameKey";
    public static final String LAST_PASSWORD_KEY = "lastPasswordKey";
    public static final String CURRENT_STAFF_INFO_KEY = "currentStaffInfoKey";
    public static final String Authorization_KEY = "Authorization";
    public static final String IS_SAVE_PASSWORD_KEY = "isSavePasswordKey";
    public static final String IS_REFRESH_DETIALLISTFRAGMENT = "isRefreshDetiallistFragment";
    public static final Double CHANGE_GOOD_RAGE = 0.8;
    public static final Double RETURN_GOOD_RAGE = 1D;

    //Api
    public static final String LOGIN_URL = "https://pos.annabellaip.com/api/rest-auth/login/";
    public static final String STAFF_INFO_URL = "https://pos.annabellaip.com/api/staff";
    public static final String GET_ORDERS_URL = "https://pos.annabellaip.com/api/orders";
    public static final String GET_ORDER_ITEM_URL = "https://pos.annabellaip.com/api/orderitems";
    public static final String GET_STOCK_URL = "https://pos.annabellaip.com/api/stocks";
    public static final String GET_STOCK_TRANSACTIONS_URL = "https://pos.annabellaip.com/api/stocktransactions";
    public static final String GET_INVENTORYS_URL = "https://pos.annabellaip.com/api/inventory";
    public static final String GET_MEMBERS_URL = "https://pos.annabellaip.com/api/members";
    public static final String INVOICES_URL = "https://pos.annabellaip.com/api/invoices";
    public static final String TRANSACTION_URL = "https://pos.annabellaip.com/api/transactions";
    public static final String CHANGE_OR_RETURN_GOOD_URL = "https://pos.annabellaip.com/api/rmaorders";
    public static final String CHANGE_OR_RETURN_GOOD_ITEM_URL = "https://pos.annabellaip.com/api/rmaorderitems";
    public static final String CHANGE_PASSWORD_URL = "https://pos.annabellaip.com/api/rest-auth/password/change/";
    public static final String RETURN_STOCKS_URL = "https://pos.annabellaip.com/api/returnstocks";
    public static final String PURCHASING_URL = "https://pos.annabellaip.com/api/purchasing";
    public static final String PURCHASING_ITEM_URL = "https://pos.annabellaip.com/api/purchasingitems";

    public static final String GOLD_PRICE_URL = "https://pos.annabellaip.com/api/goldprice/";
    public static final String GET_DISCOUNT_LIST_URL = "https://pos.annabellaip.com/api/discount/";
    public static final String CHANGE_OR_RETURN_GOOD_VOICES_URL = "https://pos.annabellaip.com/api/rmainvoices";
}
