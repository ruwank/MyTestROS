package com.relianceit.relianceorder.util;


public class Constants {
    public enum Section {
        ADD_NEW_ORDER,
        VIEW_ORDER_LIST,
        VIEW_ORDER,
        ADD_SALE_RETURNS,
        VIEW_SALE_RETURNS_LIST,
        VIEW_SALE_RETURNS
    }

	public static final String PREFS_NAME = "com.relianceit.relianceorder.press_name";


    public static final String DM_LOGGED_KEY = "com.relianceit.relianceorder.already_logged_key";
    public static final String DM_ACCESS_TOKEN_KEY = "com.relianceit.relianceorder.token_key";
    public static final String DM_USERNAME_KEY = "com.relianceit.relianceorder.username_key";

    public static final String DM_DISTRIBUTER_KEY = "com.relianceit.relianceorder.distributer_key";
    public static final String DM_SALES_REP_NAME_KEY = "com.relianceit.relianceorder.sales_rep_name_key";
    public static final String DM_USER_LEVEL_KEY = "com.relianceit.relianceorder.user_level_key";

    public static final String DM_PASSWORD_KEY = "com.relianceit.relianceorder.password_key";
    public static final String DM_DAILY_SYNC_TIME_KEY = "com.relianceit.relianceorder.daily_sync_time_key";
    public static final String DM_DAILY_SYNC_PRODUCT_KEY = "com.relianceit.relianceorder.daily_sync_product_time_key";
    public static final String DM_OFFLINE_LOGOUT_KEY = "com.relianceit.relianceorder.offline_logout_key";
    public static final String DM_DAILY_SYNC_SHOWN_KEY = "com.relianceit.relianceorder.daily_sync_shown_key";
    public static final String BASE_URL_KEY = "com.relianceit.relianceorder.base_url_data_key";

    public static final int DEFAULT_TIMEOUT_MS = 60000;

    public static final class LocalDataChange {
        public static final String ACTION_ORDER_ADDED = "action_order_added";
        public static final String ACTION_ORDER_SYNCED = "action_order_synced";
        public static final String ACTION_DAILY_SYNCED = "action_daily_synced";
    }

    public static final class OrderStatus {
        public static final int PENDING = 2;
        public static final int SYNCED = 1;
        public static final int ONLINE = 0;
    }

    public static final class OrderType {
        public static final int Sale = 0;
        public static final int Return = 1;
    }

    //Message section
    //Need to create ne class

    public static final String MSG_NO_INTERNET_TITLE = "You are offline";
    public static final String MSG_NO_INTERNET_MSG = "Make sure you have a good signal strength.";

}
