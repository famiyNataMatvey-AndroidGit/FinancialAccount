package com.namutomatvey.financialaccount;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.namutomatvey.financialaccount.adapter.GetCurrncyCBRAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

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
    public static final String KEY_FINANCE_DATE = "date";
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
                + KEY_FINANCE_DATE + " text not null,"
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
        for (int i = 0; i < 5; i += 1) {
            ContentValues contentCategoryValues = new ContentValues();
            contentCategoryValues.put(KEY_NAME, CategoryNames[i]);
            contentCategoryValues.put(KEY_CATEGORY_TYPE, FINANCE_TYPE_EXPENSES);
            database.insert(TABLE_CATEGORY, null, contentCategoryValues);
        }

        ContentValues contentCurrencyValues = new ContentValues();
        contentCurrencyValues.put(KEY_NAME, "Российский рубль");
        contentCurrencyValues.put(KEY_SHORT_NAME, "RUB");
        contentCurrencyValues.put(KEY_COEFFICIENT, 1.0);
        database.insert(TABLE_CURRENCY, null, contentCurrencyValues);

        GetCurrncyCBRAdapter getCurrncyCBRAdapter = new GetCurrncyCBRAdapter();
        getCurrncyCBRAdapter.execute();

        try {
            JSONObject currencyCRB = getCurrncyCBRAdapter.get();
            Iterator<String> keys = currencyCRB.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                JSONObject temp_currency = currencyCRB.getJSONObject(key);
                contentCurrencyValues = new ContentValues();
                contentCurrencyValues.put(KEY_NAME, temp_currency.getString("Name"));
                contentCurrencyValues.put(KEY_SHORT_NAME, temp_currency.getString("CharCode"));
                contentCurrencyValues.put(KEY_COEFFICIENT, temp_currency.getDouble("Value"));
                database.insert(TABLE_CURRENCY, null, contentCurrencyValues);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
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
