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

public class FinanceActivity extends AppCompatActivity  {

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private GridView gridView;
    public int number;

    private Toolbar mActionBarToolbar;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finance_layout);

        number = getIntent().getIntExtra("number", 2);

        mActionBarToolbar = findViewById(R.id.toolbar);
        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        gridView = findViewById(R.id.gridViewFinance);

        final List<Finance> finances = new ArrayList<Finance>();
        for(int i = 1; i < 50; i += 1) {
            finances.add(new Finance(database, i, 1, 5000.00, "вт, 21 Май 2019", 1, 1, "Comment " + i));
        }
        gridView.setAdapter(new FinanceAdapter(this, finances, 2));

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
