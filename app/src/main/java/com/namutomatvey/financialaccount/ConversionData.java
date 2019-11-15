package com.namutomatvey.financialaccount;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ConversionData {
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateFormatRevert = new SimpleDateFormat("dd-MM-yyyy");

    @SuppressLint("DefaultLocale")
    public static String conversionDoubleToString(double number){
        return String.format(Locale.ENGLISH, "%.2f", number);
    }

    public static String conversionDateToString(Date date) {
        return dateFormat.format(date);
    }

    public static String conversionDateToRevert(Date date) {
        return dateFormatRevert.format(date);
    }

    public static Date conversionStringToDate(String date) {
        try {
            return dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }

    public static Date conversionRevertToDate(String date) {
        try {
            return dateFormatRevert.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return new Date();
    }
}
