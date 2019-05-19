package com.namutomatvey.financialaccount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.namutomatvey.financialaccount.R;

public class ExpenseChoseActivity extends AppCompatActivity {
    private Toolbar mActionBarToolbar;
    private Intent intent;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_chose);

        ImageButton imageButtonExpenses = findViewById(R.id.imageButtonExpenses);
        ImageButton imageButtonCashVoucher = findViewById(R.id.imageButtonCashVoucher);

        mActionBarToolbar = findViewById(R.id.include);
        title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        imageButtonExpenses.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        intent = new Intent(ExpenseChoseActivity.this, EnterDataActivity.class);
                        intent.putExtra("title", title);
                        intent.putExtra("number", getResources().getInteger(R.integer.click_button_expenses));
                        startActivity(intent);
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        MenuItem acceptMenuItem = menu.findItem(R.id.action_accept);
        acceptMenuItem.setVisible(false);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back);
        return true;
    }
}
