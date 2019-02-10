package com.namutomatvey.financialaccount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    // имя файла настройки
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_FIRST_LAUNCH= "first_launch";
    private SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        // Приложение запущено впервые или восстановлено из памяти?
        if ( !mSettings.contains(APP_PREFERENCES_FIRST_LAUNCH))   // приложение запущено впервые
        {
            Log.d("MyTag","Запускаю в первый раз");
            // инициализация суммы счета нулем
            // другой код
            SharedPreferences.Editor editor = mSettings.edit();
            editor.putBoolean(APP_PREFERENCES_FIRST_LAUNCH, true);
            editor.apply();
        }
        final Intent intent = new Intent(MainActivity.this, EnterDataActivity.class);
        final Intent information_intent = new Intent(MainActivity.this, InformationMainActivity.class);

        // в ключ username пихаем текст из первого текстового поля

        ImageButton expensesImageButton = findViewById(R.id.imageButtonExpenses);
        ImageButton incomeImageButton = findViewById(R.id.imageButtonIncome);
        ImageButton moneyboxImageButton = findViewById(R.id.imageButtonMoneybox);
        ImageButton statisticsImageButton = findViewById(R.id.imageButtonStatistics);

        expensesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView expensesTextView = findViewById(R.id.textViewExpenses);
                intent.putExtra("title", expensesTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_expenses));
                startActivity(intent);
            }
        });
        incomeImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView incomeTextView = findViewById(R.id.textViewIncome);
                intent.putExtra("title", incomeTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_income));
                startActivity(intent);
            }
        });
        moneyboxImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView moneyboxTextView = findViewById(R.id.textViewMoneybox);
                intent.putExtra("title", moneyboxTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_moneybox));
                startActivity(intent);
            }
        });
        statisticsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView statisticsTextView = findViewById(R.id.textViewStatistics);
                information_intent.putExtra("title", statisticsTextView.getText().toString());
                information_intent.putExtra("number", getResources().getInteger(R.integer.click_button_statistics));
                startActivity(information_intent);
            }
        });
    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        // Запоминаем данные
//        SharedPreferences.Editor editor = mSettings.edit();
//        editor.putInt(APP_PREFERENCES_FIRST_LAUNCH, mCounter);
//        editor.apply();
//    }

}
