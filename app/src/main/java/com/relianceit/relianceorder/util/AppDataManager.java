package com.relianceit.relianceorder.util;

import android.content.Context;
import android.content.SharedPreferences;

public class AppDataManager {
	public static void saveData(Context context, String key, String value) {
		SharedPreferences settings = context.getSharedPreferences(
				Constants.PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.commit();
	}

    public static String getData(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(
                Constants.PREFS_NAME, 0);
        return settings.getString(key, "");

    }

    public static void saveDataLong(Context context, String key, long value) {
        SharedPreferences settings = context.getSharedPreferences(
                Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public static long getDataLong(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(
                Constants.PREFS_NAME, 0);
        return settings.getLong(key, 0);

    }

    public static void saveDataInt(Context context, String key, int value) {
        SharedPreferences settings = context.getSharedPreferences(
                Constants.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getDataInt(Context context, String key) {
        SharedPreferences settings = context.getSharedPreferences(
                Constants.PREFS_NAME, 0);
        return settings.getInt(key, 0);

    }
}
