package com.pos.priory.utils;

/**
 * Created by Lenovo on 2019/1/4.
 */

public class Constants {
    public static final String LAST_USERNAME_KEY = "lastUsernameKey";
    public static final String LAST_PASSWORD_KEY = "lastPasswordKey";
    public static final String CURRENT_STAFF_INFO_KEY = "currentStaffInfoKey";
    public static final String Authorization_KEY = "Authorization";
    public static final String IS_SAVE_PASSWORD_KEY = "isSavePasswordKey";
    public static final Double CHANGE_GOOD_RAGE = 1D;
    public static final Double RETURN_GOOD_RAGE = 0.8;


    //Api
    public static final String LOGIN_URL = "https://pos.annabellaip.com/api/rest-auth/login/";
    public static final String STAFF_INFO_URL = "https://pos.annabellaip.com/api/staff";
    public static final String GET_ORDERS_URL = "https://pos.annabellaip.com/api/orders";
    public static final String GET_ORDER_ITEM_URL = "https://pos.annabellaip.com/api/orderitems";
    public static final String GET_STOCK_URL = "https://pos.annabellaip.com/api/stocks";
    public static final String GET_INVENTORYS_URL = "https://pos.annabellaip.com/api/inventory";
    public static final String GET_MEMBERS_URL = "https://pos.annabellaip.com/api/members";
    public static final String INVOICES_URL = "https://pos.annabellaip.com/api/invoices";
    public static final String TRANSACTION_URL = "https://pos.annabellaip.com/api/transactions";
    public static final String CHANGE_OR_RETURN_GOOD_URL = "https://pos.annabellaip.com/api/rmaorders";
    public static final String CHANGE_OR_RETURN_GOOD_ITEM_URL = "https://pos.annabellaip.com/api/rmaorderitems";
    public static final String CHANGE_PASSWORD_URL = "https://pos.annabellaip.com/api/rest-auth/password/change/";
}
