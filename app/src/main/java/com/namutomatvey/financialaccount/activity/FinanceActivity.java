package com.namutomatvey.financialaccount.activity;

import android.annotation.SuppressLint;
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
import com.namutomatvey.financialaccount.dto.Currency;
import com.namutomatvey.financialaccount.dto.Finance;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class FinanceActivity extends AppCompatActivity {
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormatRevert = new SimpleDateFormat("dd MMM y");
    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private GridView gridView;
    private String selection;
    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_layout);

        mActionBarToolbar = findViewById(R.id.toolbar);
        String title = getIntent().getExtras().getString("title", getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        selection = getIntent().getExtras().getString("selection");

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        fillingGridView();
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

    @Override
    protected void onRestart() {
        super.onRestart();
        fillingGridView();
    }

    public void fillingGridView() {
        Cursor cursor = database.query(DBHelper.TABLE_FINANCE, null, selection, null, null, null, null);

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
                try {
                    long currency = cursor.getLong(currencyIndex);
                    finances.add(new Finance(database,
                            cursor.getLong(idIndex),
                            cursor.getInt(typeIndex),
                            cursor.getDouble(amountIndex) * Finance.getCoefficient(database, Finance.default_currency) / Finance.getCoefficient(database, currency),
                            dateFormatRevert.format(dateFormat.parse(cursor.getString(dateIndex))),
                            currency,
                            cursor.getLong(categoryIndex),
                            cursor.getString(commentIndex)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        gridView = findViewById(R.id.gridViewFinance);
        gridView.setAdapter(new FinanceAdapter(this, finances));
    }
}