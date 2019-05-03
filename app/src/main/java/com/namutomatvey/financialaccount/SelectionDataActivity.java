package com.namutomatvey.financialaccount;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import java.util.ArrayList;

public class SelectionDataActivity extends AppCompatActivity {

    DBHelper dbHelper;
    private Toolbar mActionBarToolbar;
    private MenuItem menuMenuItem;
    private MenuItem backMenuItem;
    private MenuItem acceptMenuItem;
    Cursor cursor;
    String title;
    int number;
    int resourceCategory;
    int resourceCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_data);
        resourceCategory = getResources().getInteger(R.integer.click_button_category);
        resourceCurrency = getResources().getInteger(R.integer.click_button_currency);
        mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
        title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);
        number = getIntent().getIntExtra("number", resourceCategory);

        GridView gridView = (GridView)findViewById(R.id.gridViewSelection);

        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        DataPrimerAdapter dataAdapter;
        if(number == resourceCategory) {
            cursor = database.query(DBHelper.TABLE_CATEGORY, null, null, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            ArrayList books = new ArrayList();
            if (cursor.moveToFirst()) {
                do {
                    books.add(cursor.getString(nameIndex));
                } while (cursor.moveToNext());
            }
            dataAdapter = new DataPrimerAdapter(this, books);
            gridView.setAdapter(dataAdapter);
        } else if(number == resourceCurrency){
            cursor = database.query(DBHelper.TABLE_CURRENCY, null, null, null, null, null, null);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            ArrayList books = new ArrayList();
            if (cursor.moveToFirst()) {
                do {
                    books.add(cursor.getString(nameIndex));
                } while (cursor.moveToNext());
            }
            dataAdapter = new DataPrimerAdapter(this, books);
            gridView.setAdapter(dataAdapter);

        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        menuMenuItem = menu.findItem(R.id.action_menu);
        backMenuItem = menu.findItem(R.id.action_back);
        acceptMenuItem = menu.findItem(R.id.action_accept);
        menuMenuItem.setVisible(false);
        backMenuItem.setVisible(false);
        acceptMenuItem.setVisible(true);
        mActionBarToolbar.setLogo(R.drawable.icon_back);
        return true;
    }
}
