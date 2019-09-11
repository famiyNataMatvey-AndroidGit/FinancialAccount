package com.namutomatvey.financialaccount.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    private TextView balanceAmountTextView;
    private SharedPreferences mSettings;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        mSettings = getSharedPreferences(getResources().getString(R.string.APP_PREFERENCES), Context.MODE_PRIVATE);

        if (!mSettings.contains(getResources().getString(R.string.APP_PREFERENCES_FIRST_LAUNCH))) {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(getResources().getString(R.string.APP_PREFERENCES_FIRST_LAUNCH), true);
            editor.putBoolean(getResources().getString(R.string.APP_PREFERENCES_BALANCE), false);
            editor.apply();

        }

        balanceAmountTextView = findViewById(R.id.textViewBalanceAmount);

        ImageButton expensesImageButton = findViewById(R.id.imageButtonExpenses);
        ImageButton incomeImageButton = findViewById(R.id.imageButtonIncome);
        ImageButton moneyboxImageButton = findViewById(R.id.imageButtonMoneybox);
        ImageButton statisticsImageButton = findViewById(R.id.imageButtonStatistics);

        expensesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(MainActivity.this, ExpenseChoseActivity.class);
                startActivity(intent);
            }
        });
        incomeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, EnterDataActivity.class);
                intent.putExtra("title", getResources().getString(R.string.income));
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_income));
                startActivity(intent);
            }
        });
        moneyboxImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, EnterDataActivity.class);
                intent.putExtra("title", getResources().getString(R.string.moneybox));
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_moneybox));
                startActivity(intent);
            }
        });
        statisticsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent = new Intent(MainActivity.this, StatisticsChoseActivity.class);
                intent.putExtra("title", getResources().getString(R.string.title_activity_statistics));
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem setting_item = menu.findItem(R.id.menu_settings);
        setting_item.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        if (mSettings.getBoolean(getResources().getString(R.string.APP_PREFERENCES_BALANCE), false)) {
            float balance = get_balance();
            balanceAmountTextView.setText(new DecimalFormat("#0.00").format(balance));
        }
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    private float get_balance() {
        float total_balance = 0;
        String table = DBHelper.TABLE_FINANCE;
        String columns[] = {
                "case when " + DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_TYPE + " = " +
                        DBHelper.FINANCE_TYPE_INCOME + " then " + DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_AMOUNT
                        + " else (-1 * " + DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_AMOUNT + ") end as balance"};
        String groupBy = DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_ID;

        DBHelper dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getReadableDatabase();

        Cursor cursor = database.query(table, columns, null, null, groupBy, null, null);

        if (cursor.moveToFirst()) {
            int balanceIndex = cursor.getColumnIndex("balance");
            do {
                total_balance += cursor.getFloat(balanceIndex);

            } while (cursor.moveToNext());
        }
        return total_balance;

    }

}