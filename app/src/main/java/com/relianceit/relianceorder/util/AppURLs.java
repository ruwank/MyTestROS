package com.relianceit.relianceorder.util;

import android.content.Context;
import android.util.Log;

/**
 * Created by Suresh on 5/6/15.
 */
public class AppURLs {

    private static final String TAG = AppUtils.class.getSimpleName();

    private static String BASE_URL = "http://reliancereldiz.com.lk";

//    private static String LOGIN_ENDPOINT = BASE_URL + "/api/authenticate/authenticate";
//    private static String CUSTOMER_LIST_ENDPOINT = BASE_URL + "/api/customer/get";
//    private static String STOCK_LIST_ENDPOINT = BASE_URL + "/api/stock/get";
//    private static String NEW_ORDER_SYNC_ENDPOINT = BASE_URL + "/api/order/add";
//    private static String PRODUCT_LIST_ENDPOINT = BASE_URL + "/api/stock/productlist";
//    private static String SALES_LIST_GET_ENDPOINT = BASE_URL + "/api/order/get/";
//    private static String SALE_GET_ENDPOINT = BASE_URL + "/api/order/get/";
//    private static String LOGOUT_ENDPOINT = BASE_URL + "/api/authenticate/logout";
//    private static String RETURN_ORDER_SYNC_ENDPOINT = BASE_URL + "/api/return/add";
//    private static String RETURN_LIST_GET_ENDPOINT = BASE_URL + "/api/return/get/";
//    private static String RETURN_GET_ENDPOINT = BASE_URL + "/api/return/get/";
//    private static String INVOICE_GET_ENDPOINT = BASE_URL + "/api/order/getforinvoice/";
//    private static String VISIT_SEND_ENDPOINT = BASE_URL + "/api/customer/visitmark";

    private static String LOGIN_ENDPOINT = "/api/authenticate/authenticate";
    private static String CUSTOMER_LIST_ENDPOINT = "/api/customer/get";
    private static String STOCK_LIST_ENDPOINT = "/api/stock/get";
    private static String NEW_ORDER_SYNC_ENDPOINT = "/api/order/add";
    private static String PRODUCT_LIST_ENDPOINT = "/api/stock/productlist";
    private static String SALES_LIST_GET_ENDPOINT = "/api/order/get/";
    private static String SALE_GET_ENDPOINT = "/api/order/get/";
    private static String LOGOUT_ENDPOINT = "/api/authenticate/logout";
    private static String RETURN_ORDER_SYNC_ENDPOINT = "/api/return/add";
    private static String RETURN_LIST_GET_ENDPOINT = "/api/return/get/";
    private static String RETURN_GET_ENDPOINT = "/api/return/get/";
    private static String INVOICE_GET_ENDPOINT = "/api/order/getforinvoice/";
    private static String VISIT_SEND_ENDPOINT = "/api/customer/visitmark";

    public static String getBASE_URL(Context context) {
        String url = AppDataManager.getData(context, Constants.BASE_URL_KEY);
        if (url.length() == 0) {
            url = BASE_URL;
        }

        Log.v(TAG, "Base URL: " + url);

        return url;
    }

    public static String getLOGIN_ENDPOINT(Context context) {
        return getBASE_URL(context) + LOGIN_ENDPOINT;
    }

    public static String getCUSTOMER_LIST_ENDPOINT(Context context) {
        return getBASE_URL(context) + CUSTOMER_LIST_ENDPOINT;
    }

    public static String getSTOCK_LIST_ENDPOINT(Context context) {
        return getBASE_URL(context) + STOCK_LIST_ENDPOINT;
    }

    public static String getNEW_ORDER_SYNC_ENDPOINT(Context context) {
        return getBASE_URL(context) + NEW_ORDER_SYNC_ENDPOINT;
    }

    public static String getPRODUCT_LIST_ENDPOINT(Context context) {
        return getBASE_URL(context) + PRODUCT_LIST_ENDPOINT;
    }

    public static String getSALES_LIST_GET_ENDPOINT(Context context) {
        return getBASE_URL(context) + SALES_LIST_GET_ENDPOINT;
    }

    public static String getSALE_GET_ENDPOINT(Context context) {
        return getBASE_URL(context) + SALE_GET_ENDPOINT;
    }

    public static String getLOGOUT_ENDPOINT(Context context) {
        return getBASE_URL(context) + LOGOUT_ENDPOINT;
    }

    public static String getRETURN_ORDER_SYNC_ENDPOINT(Context context) {
        return getBASE_URL(context) + RETURN_ORDER_SYNC_ENDPOINT;
    }

    public static String getRETURN_LIST_GET_ENDPOINT(Context context) {
        return getBASE_URL(context) + RETURN_LIST_GET_ENDPOINT;
    }

    public static String getRETURN_GET_ENDPOINT(Context context) {
        return getBASE_URL(context) + RETURN_GET_ENDPOINT;
    }

    public static String getINVOICE_GET_ENDPOINT(Context context) {
        return getBASE_URL(context) + INVOICE_GET_ENDPOINT;
    }

    public static String getVISIT_SEND_ENDPOINT(Context context) {
        return getBASE_URL(context) + VISIT_SEND_ENDPOINT;
    }
}
