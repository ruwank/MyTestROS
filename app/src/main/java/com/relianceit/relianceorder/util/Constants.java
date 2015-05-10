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
    public static final String DM_DAILY_SYNC_TIME_KEY = "com.relianceit.relianceorder.daily_sync_time_key";

    public static final class LocalDataChange {
        public static final String ACTION_ORDER_ADDED = "action_order_added";
        public static final String ACTION_ORDER_SYNCED = "action_order_synced";
        public static final String ACTION_DAILY_SYNCED = "action_daily_synced";
    }

    public static final class OrderStatus {
        public static final int PENDING = 0;
        public static final int SYNCED = 1;
        public static final int ONLINE = 2;
    }

    //Message section
    //Need to create ne class

    public static final String MSG_NO_INTERNET_TITLE = "You are offline";
    public static final String MSG_NO_INTERNET_MSG = "Make sure you have a good signal strength.";

}
