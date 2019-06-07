package com.namutomatvey.financialaccount.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSettings;
    private Intent intent;
    private TextView balanceAmountTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        mSettings = getSharedPreferences(getResources().getString(R.string.APP_PREFERENCES), Context.MODE_PRIVATE);

        if (!mSettings.contains(getResources().getString(R.string.APP_PREFERENCES_FIRST_LAUNCH)))   // приложение запущено впервые
        {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(getResources().getString(R.string.APP_PREFERENCES_FIRST_LAUNCH), true);
            editor.putString(getResources().getString(R.string.APP_PREFERENCES_BALANCE), getResources().getString(R.string.default_hint_amount));
            editor.apply();
            DBHelper dbHelper = new DBHelper(this);
            dbHelper.getWritableDatabase();
        }


        balanceAmountTextView = findViewById(R.id.textViewBalanceAmount);
        balanceAmountTextView.setText(mSettings.getString(getResources().getString(R.string.APP_PREFERENCES_BALANCE), getResources().getString(R.string.default_hint_amount)));

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
        balanceAmountTextView.setText(mSettings.getString(getResources().getString(R.string.APP_PREFERENCES_BALANCE), getResources().getString(R.string.default_hint_amount)));
        super.onStart();
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(getResources().getString(R.string.APP_PREFERENCES_BALANCE), balanceAmountTextView.getText().toString());
        editor.apply();
    }

}
