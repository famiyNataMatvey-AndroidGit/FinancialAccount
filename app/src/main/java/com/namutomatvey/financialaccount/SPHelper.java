package com.namutomatvey.financialaccount;

import android.content.SharedPreferences;

public class SPHelper {
    public static final String APP_PREFERENCES = "mysettings";

    private static final String APP_PREFERENCES_FIRST_LAUNCH = "first_launch";
    private static final String APP_PREFERENCES_BALANCE = "balance";
    private static final String APP_PREFERENCES_REGISTRATION = "registration_fns";
    private static final String APP_PREFERENCES_DEFAULT_CURRENCY = "default_currency";
    private static final String APP_PREFERENCES_FNS_PHONE = "fns_phone";
    private static final String APP_PREFERENCES_FNS_EMAIL = "fns_email";
    private static final String APP_PREFERENCES_FNS_NAME = "fns_name";
    private static final String APP_PREFERENCES_FNS_PASSWORD = "fns_password";
    private static final String APP_PREFERENCES_TYPE_DATE_PICKER = "datePickerType";
    private static final String APP_PREFERENCES_TITLE= "titleStatisticsActivity";

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
        editor.putInt(APP_PREFERENCES_TYPE_DATE_PICKER, 7);
        editor.putString(APP_PREFERENCES_TITLE, "День");
        editor.apply();
    }

    public static boolean checkUpdateBalance() {
        if(sharedPreferences.contains(APP_PREFERENCES_BALANCE))
            return sharedPreferences.getBoolean(APP_PREFERENCES_BALANCE, false);
        return true;
    }

    public static void setBalanceTrue(){
        if (!sharedPreferences.getBoolean(APP_PREFERENCES_BALANCE, false)) {
            editor = sharedPreferences.edit();
            editor.putBoolean(APP_PREFERENCES_BALANCE, true);
            editor.apply();
        }
    }

    public static void saveStatisticParams(int typeDatePicker, String statisticTitle) {
        editor = sharedPreferences.edit();
        setStatisticTypeDatePicker(typeDatePicker);
        setStatisticTitle(statisticTitle);
        editor.apply();
    }

    private static void setStatisticTypeDatePicker(int typeDatePicker) {
        editor.putInt(APP_PREFERENCES_TYPE_DATE_PICKER, typeDatePicker);
    }

    private static void setStatisticTitle(String statisticTitle) {
        editor.putString(APP_PREFERENCES_TITLE, statisticTitle);
    }

    public static String getStatisticTitle() {
        return sharedPreferences.getString(APP_PREFERENCES_TITLE, "День");
    }

    public static int getStatisticTypeDatePicker() {
        return sharedPreferences.getInt(APP_PREFERENCES_TYPE_DATE_PICKER, 7);
    }

    public static void setDefaultCurrency(long currency_id) {
        editor = sharedPreferences.edit();
        editor.putLong(APP_PREFERENCES_DEFAULT_CURRENCY, currency_id);
        editor.apply();
    }

    public static long getDefaultCurrency() {
        return sharedPreferences.getLong(APP_PREFERENCES_DEFAULT_CURRENCY, 1);
    }

    public static void registrationFns(String name, String email,  String phone) {
        editor = sharedPreferences.edit();
        setFnsName(name);
        setFnsEmail(email);
        setFnsPhone(phone);
        editor.apply();
    }

    public static void loginFns(String phone, String password) {
        editor = sharedPreferences.edit();
        setFnsPhone(phone);
        setFnsPassword(password);
        editor.apply();
    }

    private static void setFnsName(String name) {
        editor.putString(APP_PREFERENCES_FNS_NAME, name);
    }

    private static void setFnsEmail(String email) {
        editor.putString(APP_PREFERENCES_FNS_EMAIL, email);
    }

    private static void setFnsPhone(String phone) {
        editor.putString(APP_PREFERENCES_FNS_PHONE, phone);
    }

    private static void setFnsPassword(String password) {
        editor.putString(APP_PREFERENCES_FNS_PASSWORD, password);
    }

    public static String getFnsName() {
        return sharedPreferences.getString(APP_PREFERENCES_FNS_NAME, "");
    }

    public static String getFnsEmail() {
        return sharedPreferences.getString(APP_PREFERENCES_FNS_EMAIL, "");
    }

    public static String getFnsPhone() {
        return sharedPreferences.getString(APP_PREFERENCES_FNS_PHONE, "");
    }

    public static String getFnsPassword() {
        return sharedPreferences.getString(APP_PREFERENCES_FNS_PASSWORD, "");
    }

    public static String getFnsClosePassword() {
        return getFnsPassword().replaceAll(".", "*");
    }

    public static boolean getRegistrationFns() {
        return sharedPreferences.getBoolean(APP_PREFERENCES_REGISTRATION, false);
    }
}
