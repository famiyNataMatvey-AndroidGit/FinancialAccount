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

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_FIRST_LAUNCH = "first_launch";
    public static final String APP_PREFERENCES_BALANCE = "first_launch";

    private Toolbar mActionBarToolbar;
    private SharedPreferences mSettings;
    private TextView balanceAmountTextView;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mActionBarToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mActionBarToolbar);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        // Приложение запущено впервые или восстановлено из памяти?
        if ( !mSettings.contains(APP_PREFERENCES_FIRST_LAUNCH))   // приложение запущено впервые
        {
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(APP_PREFERENCES_FIRST_LAUNCH, true);
            editor.putString(APP_PREFERENCES_BALANCE, "0.0");
            editor.apply();
            new DBHelper(this);
        }


        balanceAmountTextView = findViewById(R.id.textViewBalanceAmount);
        balanceAmountTextView.setText(mSettings.getString(APP_PREFERENCES_BALANCE, "0.0"));

        ImageButton expensesImageButton = findViewById(R.id.imageButtonExpenses);
        ImageButton incomeImageButton = findViewById(R.id.imageButtonIncome);
        ImageButton moneyboxImageButton = findViewById(R.id.imageButtonMoneybox);
        ImageButton statisticsImageButton = findViewById(R.id.imageButtonStatistics);

        expensesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView expensesTextView = findViewById(R.id.textViewExpenses);

                intent = new Intent(MainActivity.this, ExpenseChoseActivity.class);
                intent.putExtra("title", expensesTextView.getText().toString());
                startActivity(intent);
            }
        });
        incomeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView incomeTextView = findViewById(R.id.textViewIncome);

                intent = new Intent(MainActivity.this, EnterDataActivity.class);
                intent.putExtra("title", incomeTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_income));
                startActivity(intent);
            }
        });
        moneyboxImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView moneyboxTextView = findViewById(R.id.textViewMoneybox);

                intent = new Intent(MainActivity.this, EnterDataActivity.class);
                intent.putExtra("title", moneyboxTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_moneybox));
                startActivity(intent);
            }
        });
        statisticsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView statisticsTextView = findViewById(R.id.textViewStatistics);

                intent = new Intent(MainActivity.this, StatisticsChoseActivity.class);
                intent.putExtra("title", statisticsTextView.getText().toString());
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
    protected void onPause() {
        super.onPause();
        // Запоминаем данные
        SharedPreferences.Editor editor = mSettings.edit();
        editor.putString(APP_PREFERENCES_BALANCE, balanceAmountTextView.getText().toString());
        editor.apply();
    }

}
