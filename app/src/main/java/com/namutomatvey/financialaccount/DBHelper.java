package com.namutomatvey.financialaccount;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DBHelper  extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "financialAccountDb";
    public static final String TABLE_CURRENCY = "currency";
    public static final String TABLE_CATEGORY = "category";
    public static final String TABLE_FINANCE = "finance";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";

    public static final String KEY_SHORT_NAME = "short_name";
    public static final String KEY_COEFFICIENT = "coefficient";

    public static final String KEY_CATEGORY_TYPE = "type";
    public static final String KEY_PARENT = "parent_id";

    public static final Integer FINANCE_TYPE_INCOME = 1;
    public static final Integer FINANCE_TYPE_EXPENSES = 2;
    public static final Integer FINANCE_TYPE_MONEYBOX = 3;

    public static final String KEY_FINANCE_TYPE = "type";
    public static final String KEY_FINANCE_DATE_TIME = "date_time";
    public static final String KEY_FINANCE_COMMENT = "comment";
    public static final String KEY_FINANCE_AMOUNT = "amount";
    public static final String KEY_FINANCE_CATEGORY = "category_id";
    public static final String KEY_FINANCE_CURRENCY = "currency_id";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CURRENCY + "("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_NAME + " text not null,"
                + KEY_SHORT_NAME + " text,"
                + KEY_COEFFICIENT + " real not null" + " check (" + KEY_COEFFICIENT + " >= 0)" + ")");

        db.execSQL("create table " + TABLE_CATEGORY + "("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_NAME + " text not null,"
                + KEY_CATEGORY_TYPE + " integer not null,"
                + KEY_PARENT + " integer,"
                + "foreign key "
                + "(" + KEY_PARENT + ")" + " references "
                + TABLE_CATEGORY + "(" + "id" + ")" + ")");

        db.execSQL("create table " + TABLE_FINANCE + "("
                + KEY_ID + " integer primary key autoincrement,"
                + KEY_FINANCE_TYPE + " integer not null"
                + " check (" + KEY_FINANCE_TYPE + " >= 1 and " + KEY_FINANCE_TYPE + " <= 3),"
                + KEY_FINANCE_DATE_TIME + " text not null,"
                + KEY_FINANCE_COMMENT + " text,"
                + KEY_FINANCE_AMOUNT + " real not null"  + " check (" + KEY_FINANCE_AMOUNT + " >= 0),"
                + KEY_FINANCE_CATEGORY + " integer not null,"
                + KEY_FINANCE_CURRENCY + " integer not null,"
                + "foreign key "
                + "(" + KEY_FINANCE_CATEGORY + ")" + " references "
                + TABLE_CATEGORY + "(" + "id" + "),"
                + "foreign key "
                + "(" + KEY_FINANCE_CURRENCY + ")" + " references "
                + TABLE_CURRENCY + "(" + "id" + ")" + ")");
        onBaseInsertDatabase(db);
    }

    private void onBaseInsertDatabase(SQLiteDatabase database) {
        String [] CategoryNames = {"Фрукты", "Овощи", "Кисломолочные продукты", "Напитки", "Быт"};
        Integer [] CategoryTypes = {FINANCE_TYPE_INCOME, FINANCE_TYPE_EXPENSES, FINANCE_TYPE_MONEYBOX};
        String [] CurrencyNames = {"Доллар", "Рубль", "Евро"};
        Double [] CurrencyCoefficients = {66.7, 1.0, 75.25};
        for (int i = 0; i < 5; i += 1) {
            ContentValues contentCategoryValues = new ContentValues();
            contentCategoryValues.put(KEY_NAME, CategoryNames[i]);
            contentCategoryValues.put(KEY_CATEGORY_TYPE, FINANCE_TYPE_INCOME);
            database.insert(TABLE_CATEGORY, null, contentCategoryValues);
        }
        for (int i = 0; i < 3; i += 1) {
            ContentValues contentCurrencyValues = new ContentValues();
            contentCurrencyValues.put(KEY_NAME, CurrencyNames[i]);
            contentCurrencyValues.put(KEY_COEFFICIENT, CurrencyCoefficients[i]);
            database.insert(TABLE_CURRENCY, null, contentCurrencyValues);
        }
        for (int i = 0; i < 3; i += 1) {
            ContentValues contentCurrencyValues = new ContentValues();
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDate =  new SimpleDateFormat("dd-MM-yyyy HH:mm");
            String str_date = simpleDate.format(date);
            for (int j = 0; j < 3; j += 1) {
                contentCurrencyValues.put(KEY_FINANCE_TYPE, CategoryTypes[i]);
                contentCurrencyValues.put(KEY_FINANCE_DATE_TIME, str_date);
                contentCurrencyValues.put(KEY_FINANCE_COMMENT, "Комент 1");
                contentCurrencyValues.put(KEY_FINANCE_AMOUNT, 179 * CurrencyCoefficients[j]);
                contentCurrencyValues.put(KEY_FINANCE_CATEGORY, i);
                contentCurrencyValues.put(KEY_FINANCE_CURRENCY, i);
                database.insert(TABLE_FINANCE, null, contentCurrencyValues);
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CURRENCY);
        db.execSQL("drop table if exists " + TABLE_CATEGORY);
        db.execSQL("drop table if exists " + TABLE_FINANCE);
        onCreate(db);

    }
}
