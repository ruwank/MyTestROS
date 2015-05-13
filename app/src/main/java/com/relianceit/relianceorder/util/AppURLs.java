package com.relianceit.relianceorder.util;

/**
 * Created by sura on 5/6/15.
 */
public class AppURLs {

    public static String BASE_URL = "http://reliancereldiz.com.lk";

    public static String LOGIN_ENDPOINT = BASE_URL + "/api/authenticate/authenticate";
    public static String CUSTOMER_LIST_ENDPOINT = BASE_URL + "/api/customer/get";
    public static String STOCK_LIST_ENDPOINT = BASE_URL + "/api/stock/get";
    public static String NEW_ORDER_SYNC_ENDPOINT = BASE_URL + "/api/order/add";
    public static String PRODUCT_LIST_ENDPOINT = BASE_URL + "/api/stock/productlist";
    public static String SALES_LIST_GET_ENDPOINT = BASE_URL + "/api/order/get/";
    public static String SALE_GET_ENDPOINT = BASE_URL + "/api/order/get/";
    public static String LOGOUT_ENDPOINT = BASE_URL + "/api/authenticate/logout";
}
