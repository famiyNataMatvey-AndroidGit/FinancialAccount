package com.namutomatvey.financialaccount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.namutomatvey.financialaccount.R;

public class StatisticsChoseActivity extends AppCompatActivity {
    private Toolbar mActionBarToolbar;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_chose);

        mActionBarToolbar = findViewById(R.id.toolbar);
        String title = getIntent().getExtras().getString("title", getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        Button imageButtonStatisticsExpenses = findViewById(R.id.imageButtonStatisticsExpenses);
        Button imageButtonStatisticsIncome = findViewById(R.id.imageButtonStatisticsIncome);
        Button imageButtonStatisticsMoneybox = findViewById(R.id.imageButtonStatisticsMoneybox);

        imageButtonStatisticsExpenses.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                intent = new Intent(StatisticsChoseActivity.this, StatisticsActivity.class);
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_expenses));
                startActivity(intent);
                }
            }
        );
        imageButtonStatisticsIncome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                intent = new Intent(StatisticsChoseActivity.this, StatisticsActivity.class);
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_income));
                startActivity(intent);
                }
            }
        );
        imageButtonStatisticsMoneybox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                intent = new Intent(StatisticsChoseActivity.this, StatisticsActivity.class);
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_moneybox));
                startActivity(intent);
                }
            }
        );
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
