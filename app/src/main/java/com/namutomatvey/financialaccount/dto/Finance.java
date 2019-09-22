package com.namutomatvey.financialaccount.dto;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.SPHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Finance {
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormatRevert = new SimpleDateFormat("dd-MM-yyyy");
    private long id;
    private int type;
    private double amount;
    private String date;
    private String comment;
    private long category;
    private long currency;
    public boolean box = false;

    private SQLiteDatabase database;

    public static void updateAmountCurrency(DBHelper dbHelper, long currency_id) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        long default_currency = SPHelper.getDefaultCurrency();
        double coefficient = getCoefficient(database, default_currency) / getCoefficient(database, currency_id);
        String qs = "UPDATE " + DBHelper.TABLE_FINANCE + " SET " + DBHelper.KEY_FINANCE_AMOUNT + " = " + DBHelper.KEY_FINANCE_AMOUNT + " * " + coefficient;
        database.execSQL(qs);
        SPHelper.setDefaultCurrency(currency_id);
    }

    public static float getBalance(DBHelper dbHelper) {
        String[] columns = {
                "CASE WHEN " +
                        DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_TYPE + " = " + DBHelper.FINANCE_TYPE_INCOME +
                        " THEN " + DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_AMOUNT +
                        " ELSE (-1 * " + DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_AMOUNT
                        + ") END AS balance"};

        SQLiteDatabase database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_FINANCE, columns, null, null, null, null, null);
        int balanceIndex = cursor.getColumnIndex("balance");
        float total_balance = 0;
        if (cursor.moveToFirst()) {
            do {
                total_balance += cursor.getFloat(balanceIndex);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return total_balance;
    }

    public Finance(SQLiteDatabase database, int type, double amount, String date, long currency, String comment) {
        this.type = type;
        this.amount = amount;
        try {
            this.date = dateFormatRevert.format(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.currency = currency;
        this.comment = comment;
        this.database = database;
    }

    public Finance(SQLiteDatabase database, long id, int type, double amount, String date, long currency, long category, String comment) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.category = category;
        this.comment = comment;
        this.database = database;
    }

    public Finance(SQLiteDatabase database, int type, double amount, String date, long currency, long category, String comment) {
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.category = category;
        this.comment = comment;
        this.database = database;
    }

    public long getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public String getCategory() {
        String category_name = "";
        Cursor cursor = database.query(DBHelper.TABLE_CATEGORY, null, DBHelper.KEY_ID + " = " + category, null, null, null, null);
        int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
        if (cursor.moveToFirst())
            category_name = cursor.getString(nameIndex);
        cursor.close();
        return category_name;
    }

    public String getCurrency() {
        String currency_name = "";
        Cursor cursor = database.query(DBHelper.TABLE_CURRENCY, null, DBHelper.KEY_ID + " = " + currency, null, null, null, null);
        int shortNameIndex = cursor.getColumnIndex(DBHelper.KEY_SHORT_NAME);
        if (cursor.moveToFirst())
            currency_name = cursor.getString(shortNameIndex);
        cursor.close();
        return currency_name;
    }

    public static String getCurrency(SQLiteDatabase database, long currency_id) {
        String currency_name = "";
        Cursor cursor = database.query(DBHelper.TABLE_CURRENCY, null, DBHelper.KEY_ID + " = " + currency_id, null, null, null, null);
        int shortNameIndex = cursor.getColumnIndex(DBHelper.KEY_SHORT_NAME);
        if (cursor.moveToFirst())
            currency_name = cursor.getString(shortNameIndex);
        cursor.close();
        return currency_name;
    }

    public static Double getCoefficient(SQLiteDatabase database, long currency_id) {
        double coefficientTemp = 1.0;
        Cursor cursor = database.query(DBHelper.TABLE_CURRENCY, null, DBHelper.KEY_ID + " = " + currency_id, null, null, null, null);
        int coefficientIndex = cursor.getColumnIndex(DBHelper.KEY_COEFFICIENT);
        if (cursor.moveToFirst())
            coefficientTemp = cursor.getDouble(coefficientIndex);
        cursor.close();
        return coefficientTemp;
    }

    public void setCategory(long category_id) {
        this.category = category_id;
    }

    public void createFinance() {
        ContentValues contentFinanceValues = new ContentValues();
        contentFinanceValues.put(DBHelper.KEY_FINANCE_TYPE, this.type);
        Double amount = this.amount * getCoefficient(database, this.currency) / getCoefficient(database, SPHelper.getDefaultCurrency());
        contentFinanceValues.put(DBHelper.KEY_FINANCE_AMOUNT, amount);
        try {
            contentFinanceValues.put(DBHelper.KEY_FINANCE_DATE, dateFormat.format(dateFormatRevert.parse(this.date)));
        } catch (ParseException e) {
            contentFinanceValues.put(DBHelper.KEY_FINANCE_DATE, this.date);
        }
        contentFinanceValues.put(DBHelper.KEY_FINANCE_COMMENT, this.comment);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_CATEGORY, this.category);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_CURRENCY, this.currency);
        this.id = database.insert(DBHelper.TABLE_FINANCE, null, contentFinanceValues);
    }

    public void updateFinance(int type, double amount, String date, long currency, long category, String comment) {
        ContentValues contentFinanceValues = new ContentValues();
        contentFinanceValues.put(DBHelper.KEY_FINANCE_TYPE, type);
        Double temp_amount = amount * getCoefficient(database, currency) / getCoefficient(database, SPHelper.getDefaultCurrency());
        contentFinanceValues.put(DBHelper.KEY_FINANCE_AMOUNT, temp_amount);
        try {
            contentFinanceValues.put(DBHelper.KEY_FINANCE_DATE, dateFormat.format(dateFormatRevert.parse(date)));
        } catch (ParseException e) {
            contentFinanceValues.put(DBHelper.KEY_FINANCE_DATE, date);
        }
        contentFinanceValues.put(DBHelper.KEY_FINANCE_COMMENT, comment);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_CATEGORY, category);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_CURRENCY, currency);
        int updCount = database.update(DBHelper.TABLE_FINANCE, contentFinanceValues, DBHelper.KEY_ID + " = " + id, null);
        if (updCount != 0)
            this.type = type;
        this.amount = amount;
        this.date = date;
        this.currency = currency;
        this.category = category;
        this.comment = comment;
    }

    public void delFinance() {
        database.delete(DBHelper.TABLE_FINANCE, DBHelper.KEY_ID + " = " + id, null);
    }
}