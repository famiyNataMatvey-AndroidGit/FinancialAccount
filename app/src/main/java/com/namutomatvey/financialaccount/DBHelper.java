package com.namutomatvey.financialaccount;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

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

    public static final String KEY_FINANCE_TYPE = "type";
    public static final String KEY_FINANCE_DATE = "date";
    public static final String KEY_FINANCE_TIME = "time";
    public static final String KEY_FINANCE_COMMENT = "comment";
    public static final String KEY_FINANCE_AMOUNT = "amount";
    public static final String KEY_FINANCE_CATEGORY = "category_id";
    public static final String KEY_FINANCE_CURRENCY = "currency_id";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CURRENCY + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_SHORT_NAME + " text,"
                + KEY_COEFFICIENT + " real" + ")");

        db.execSQL("create table " + TABLE_CATEGORY + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text,"
                + KEY_CATEGORY_TYPE + " integer not null,"
                + KEY_PARENT + " integer,"
                + "foreign key " + "(" + KEY_PARENT + ")" + " references " + TABLE_CATEGORY + "(" + "id" + ")" + ")");

//        db.execSQL("create table " + TABLE_FINANCE + "(" + KEY_ID
//                + " integer primary key," + KEY_NAME + " text," + KEY_MAIL + " text" + ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_CURRENCY);
        db.execSQL("drop table if exists " + TABLE_CATEGORY);
        db.execSQL("drop table if exists " + TABLE_FINANCE);

        onCreate(db);

    }
}
