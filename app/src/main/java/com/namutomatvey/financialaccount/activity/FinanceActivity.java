package com.namutomatvey.financialaccount.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.adapter.FinanceAdapter;
import com.namutomatvey.financialaccount.dto.Finance;

import java.util.ArrayList;
import java.util.List;

public class FinanceActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private GridView gridView;
    private long category_id;
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_layout);

        mActionBarToolbar = findViewById(R.id.toolbar);
        String title = getIntent().getExtras().getString("title", getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        category_id = getIntent().getExtras().getLong("categoryId");

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_FINANCE, null, DBHelper.KEY_FINANCE_CATEGORY + " = " + category_id, null, null, null, null);

        int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
        int amountIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_AMOUNT);
        int categoryIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_CATEGORY);
        int commentIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_COMMENT);
        int currencyIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_CURRENCY);
        int dateIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_DATE);
        int typeIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_TYPE);

        final List<Finance> finances = new ArrayList<Finance>();
        if (cursor.moveToFirst()) {
            do {
                finances.add(new Finance(database,
                        cursor.getLong(idIndex),
                        cursor.getInt(typeIndex),
                        cursor.getDouble(amountIndex),
                        cursor.getString(dateIndex),
                        cursor.getLong(currencyIndex),
                        cursor.getLong(categoryIndex),
                        cursor.getString(commentIndex)));
            } while (cursor.moveToNext());
        }

        gridView = findViewById(R.id.gridViewFinance);

        gridView.setAdapter(new FinanceAdapter(this, finances));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem setting_item = menu.findItem(R.id.menu_settings);
        setting_item.setVisible(false);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}