package com.namutomatvey.financialaccount.dto;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.namutomatvey.financialaccount.DBHelper;

public class Finance {
    private long id;
    private int type;
    private int amount;
    private String dateTime;
    private String comment;
    private int category;
    private int currency;

    private SQLiteDatabase database;

    public Finance(SQLiteDatabase database, long id, int type, int amount, String dateTime, int currency, int category, String comment) {
        this.id = id;
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
        this.currency = currency;
        this.category = category;
        this.comment = comment;
        this.database = database;
    }

    public Finance(SQLiteDatabase database, int type, int amount, String dateTime, int currency, int category, String comment) {
        this.type = type;
        this.amount = amount;
        this.dateTime = dateTime;
        this.currency = currency;
        this.category = category;
        this.comment = comment;
        this.database = database;
        ContentValues contentFinanceValues = new ContentValues();
        contentFinanceValues.put(DBHelper.KEY_FINANCE_TYPE, type);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_AMOUNT, amount);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_DATE_TIME, dateTime);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_COMMENT, comment);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_CATEGORY, category);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_CURRENCY, currency);
        this.id = database.insert(DBHelper.TABLE_FINANCE, null, contentFinanceValues);
    }

    public long getId() {
        return id;
    }

    public double getAmount() {
        return amount;
    }

    public String getComment() {
        return comment;
    }

    public void updateFinance(int type, int amount, String dateTime, int currency, int category, String comment) {
        ContentValues contentFinanceValues = new ContentValues();
        contentFinanceValues.put(DBHelper.KEY_FINANCE_TYPE, type);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_AMOUNT, amount);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_DATE_TIME, dateTime);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_COMMENT, comment);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_CATEGORY, category);
        contentFinanceValues.put(DBHelper.KEY_FINANCE_CURRENCY, currency);
        int updCount = database.update(DBHelper.TABLE_FINANCE, contentFinanceValues, "id = " + id, null);
        if(updCount != 0)
            this.type = type;
            this.amount = amount;
            this.dateTime = dateTime;
            this.currency = currency;
            this.category = category;
            this.comment = comment;
    }

    public void delFinance() {
        int delCount = database.delete(DBHelper.TABLE_FINANCE, "id = " + id, null);
    }
}
