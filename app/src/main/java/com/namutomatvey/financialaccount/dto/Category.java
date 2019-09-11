package com.namutomatvey.financialaccount.dto;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.namutomatvey.financialaccount.DBHelper;

public class Category {
  private long id;
  private String name;
  private Integer type;
  private Integer parent;

  private SQLiteDatabase database;

  public Category(SQLiteDatabase database, long id, String name, Integer type, Integer parent) {
    this.id = id;
    this.name = name;
    this.type = type;
    this.parent = parent;
    this.database = database;
  }

  public Category(SQLiteDatabase database, String name, Integer type, Integer parent) {
    this.name = name;
    this.type = type;
    this.parent = parent;
    this.database = database;
    ContentValues contentCategoryValues = new ContentValues();
    contentCategoryValues.put(DBHelper.KEY_NAME, name);
    contentCategoryValues.put(DBHelper.KEY_CATEGORY_TYPE, type);
    contentCategoryValues.put(DBHelper.KEY_PARENT, parent);
    this.id = database.insert(DBHelper.TABLE_CATEGORY, null, contentCategoryValues);
  }

  public long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    ContentValues contentCategoryValues = new ContentValues();
    contentCategoryValues.put(DBHelper.KEY_NAME, name);
    int updCount = database.update(DBHelper.TABLE_CATEGORY, contentCategoryValues, "id = " + id, null);
    if(updCount != 0)
      this.name = name;
  }

  public void delCategory() {
    int delCount = database.delete(DBHelper.TABLE_CATEGORY, "id = " + id, null);
  }
}