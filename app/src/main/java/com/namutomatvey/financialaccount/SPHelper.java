package com.namutomatvey.financialaccount;

import android.content.SharedPreferences;

public class SPHelper {
    private static final String APP_PREFERENCES_FIRST_LAUNCH = "first_launch";
    private static final String APP_PREFERENCES_BALANCE = "balance";
    private static final String APP_PREFERENCES_REGISTRATION = "registration_fns";
    private static final String APP_PREFERENCES_DEFAULT_CURRENCY = "default_currency";

    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void setSharedPreferences(SharedPreferences mSettings) {
        sharedPreferences = mSettings;
    }

    public static void checkFirstLaunch() {
        if (!sharedPreferences.contains(APP_PREFERENCES_FIRST_LAUNCH))
            startFirstLaunch();
    }

    private static void startFirstLaunch() {
        editor = sharedPreferences.edit();
        editor.putBoolean(APP_PREFERENCES_FIRST_LAUNCH, true);
        editor.putBoolean(APP_PREFERENCES_REGISTRATION, false);
        editor.putLong(APP_PREFERENCES_DEFAULT_CURRENCY, 1);
        editor.apply();
    }

    public static boolean checkUpdateBalance() {
        if(sharedPreferences.contains(APP_PREFERENCES_BALANCE))
            return sharedPreferences.getBoolean(APP_PREFERENCES_BALANCE, false);
        return true;
    }
}
