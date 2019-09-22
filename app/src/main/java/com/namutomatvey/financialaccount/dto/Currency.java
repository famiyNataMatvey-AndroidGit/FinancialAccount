package com.namutomatvey.financialaccount.dto;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.adapter.GetCurrencyCBRAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.ExecutionException;

public class Currency {
    private static ContentValues contentCurrencyValues;

    private long id;
    private String name;
    private String short_name;
    private double coefficient;

    public static void createCurrencyData(SQLiteDatabase database) {
        insertRusCurrency(database);
        JSONObject currencyCRB = requestCurrencyFromCBR();
        Iterator<String> keys = currencyCRB.keys();
        while (keys.hasNext()) {
            try {
                JSONObject temp_currency = currencyCRB.getJSONObject(keys.next());
                contentCurrencyValues = new ContentValues();
                contentCurrencyValues.put(DBHelper.KEY_NAME, temp_currency.getString("Name"));
                contentCurrencyValues.put(DBHelper.KEY_SHORT_NAME, temp_currency.getString("CharCode"));
                contentCurrencyValues.put(DBHelper.KEY_COEFFICIENT, temp_currency.getDouble("Value"));
                database.insert(DBHelper.TABLE_CURRENCY, null, contentCurrencyValues);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public static void updateCurrencyData(SQLiteDatabase database) {
        JSONObject currencyCRB = requestCurrencyFromCBR();
        Iterator<String> keys = currencyCRB.keys();
        while (keys.hasNext()) {
            try {
                JSONObject temp_currency = currencyCRB.getJSONObject(keys.next());
                contentCurrencyValues = new ContentValues();
                contentCurrencyValues.put(DBHelper.KEY_COEFFICIENT, temp_currency.getDouble("Value"));
                database.update(DBHelper.TABLE_CURRENCY,
                        contentCurrencyValues,
                        DBHelper.KEY_SHORT_NAME + " = " + temp_currency.getString("CharCode"),
                        null);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void insertRusCurrency(SQLiteDatabase database) {
        contentCurrencyValues = new ContentValues();
        contentCurrencyValues.put(DBHelper.KEY_NAME, "Российский рубль");
        contentCurrencyValues.put(DBHelper.KEY_SHORT_NAME, "RUB");
        contentCurrencyValues.put(DBHelper.KEY_COEFFICIENT, 1.0);
        database.insert(DBHelper.TABLE_CURRENCY, null, contentCurrencyValues);
    }

    private static JSONObject requestCurrencyFromCBR() {
        GetCurrencyCBRAdapter getCurrncyCBRAdapter = new GetCurrencyCBRAdapter();
        getCurrncyCBRAdapter.execute();
        while (true) {
            try {
                Thread.sleep(1000);
                return getCurrncyCBRAdapter.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                continue;
            }
            break;
        }
        return new JSONObject();
    }

    public Currency(long id, String name, String short_name, double coefficient) {
        this.id = id;
        this.name = name;
        this.short_name = short_name;
        this.coefficient = coefficient;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getShortName() {
        return short_name;
    }

    public double getCoefficient() {
        return coefficient;
    }
}