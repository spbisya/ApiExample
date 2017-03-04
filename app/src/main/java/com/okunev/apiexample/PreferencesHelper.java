package com.okunev.apiexample;

import android.content.Context;

/**
 * Project ApiExample. Created by okunev on 3/4/17.
 */

public class PreferencesHelper {

    public static String getDeviceId(Context context) {
        return context.getSharedPreferences("volx", 0).getString("deviceId", "");
    }

    public static String getUserToken(Context context) {
        return context.getSharedPreferences("volx", 0).getString("token", "");
    }

    public static void putUserToken(Context context, String token) {
        context.getSharedPreferences("volx", 0).edit().putString("token", token).apply();
    }

    public static void removeUserToken(Context context) {
        context.getSharedPreferences("volx", 0).edit().putString("token", "").remove("token").apply();
    }

}
