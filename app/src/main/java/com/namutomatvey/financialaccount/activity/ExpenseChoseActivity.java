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

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_expense_chose);

    ImageButton imageButtonExpenses = findViewById(R.id.imageButtonExpenses);
    ImageButton imageButtonCashVoucher = findViewById(R.id.imageButtonCashVoucher);

    mActionBarToolbar = findViewById(R.id.toolbar);
    mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_choice_expenses));
    setSupportActionBar(mActionBarToolbar);

    imageButtonExpenses.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {

                                               intent = new Intent(ExpenseChoseActivity.this, EnterDataActivity.class);
                                               intent.putExtra("title", getResources().getString(R.string.expenses));
                                               intent.putExtra("number", getResources().getInteger(R.integer.click_button_expenses));
                                               startActivity(intent);
                                             }
                                           }
    );

    imageButtonCashVoucher.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                  intent = new Intent(ExpenseChoseActivity.this, BeforeAppendStatisticActivity.class);
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