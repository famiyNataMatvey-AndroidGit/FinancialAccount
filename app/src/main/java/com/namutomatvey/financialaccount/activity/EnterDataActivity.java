package com.namutomatvey.financialaccount.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.namutomatvey.financialaccount.ConversionData;
import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.SPHelper;
import com.namutomatvey.financialaccount.dto.Finance;
import com.namutomatvey.financialaccount.fragment.CalendarFragment;

import java.util.Calendar;

public class EnterDataActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {
    private DBHelper dbHelper;
    private Toolbar mActionBarToolbar;
    private Intent intent;
    private static final int requestCodeCategory = 1;
    private static final int requestCodeCurrency = 2;
    private TextView dateView;
    private TextView textViewCategoryName;
    private TextView textViewCurrencyName;
    private EditText editTextComment;
    private EditText editTextAmount;

    private long category;
    private long currency;
    private long finance_id;
    private int type_finance;

    private Finance finance = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        String title = getIntent().getExtras().getString("title", getResources().getString(R.string.app_name));
        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        type_finance = getIntent().getExtras().getInt("type_finance", DBHelper.FINANCE_TYPE_INCOME);
        finance_id = getIntent().getExtras().getLong("finance_id", -1);

        intent = new Intent(EnterDataActivity.this, SelectionDataActivity.class);

        dateView = findViewById(R.id.textViewDate);
        textViewCategoryName = findViewById(R.id.financeItemCategoryName);
        textViewCurrencyName = findViewById(R.id.textViewCurrencyName);
        editTextComment = findViewById(R.id.editTextComment);
        editTextAmount = findViewById(R.id.editTextAmount);

        dbHelper = new DBHelper(this);
        if (finance_id != -1) {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_FINANCE, null, DBHelper.KEY_ID + " = " + finance_id, null, null, null, null);
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int amountIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_AMOUNT);
            int categoryIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_CATEGORY);
            int commentIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_COMMENT);
            int currencyIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_CURRENCY);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_DATE);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_TYPE);

            if (cursor.moveToFirst()) {
                currency = cursor.getLong(currencyIndex);
                category = cursor.getLong(categoryIndex);

                finance = new Finance(database,
                        cursor.getLong(idIndex),
                        cursor.getInt(typeIndex),
                        cursor.getDouble(amountIndex) * Finance.getCoefficient(database, SPHelper.getDefaultCurrency()) / Finance.getCoefficient(database, currency),
                        ConversionData.conversionStringToDate(cursor.getString(dateIndex)),
                        currency,
                        category,
                        cursor.getString(commentIndex));

                dateView.setText(finance.getDate());
                textViewCategoryName.setText(finance.getCategory());
                textViewCurrencyName.setText(finance.getCurrency());
                editTextComment.setText(finance.getComment());
                editTextAmount.setText(ConversionData.conversionDoubleToString(finance.getAmount()));

                dateView.setTextColor(Color.BLACK);
                textViewCategoryName.setTextColor(Color.BLACK);
                textViewCategoryName.setTextColor(Color.BLACK);

            }
            cursor.close();

        } else {
            SQLiteDatabase database = dbHelper.getWritableDatabase();
            currency = SPHelper.getDefaultCurrency();
            textViewCurrencyName.setText(Finance.getCurrency(database, currency));
        }

        if (dateView.getText().toString().isEmpty()) {
            dateView.setText(ConversionData.conversionDateToRevert(Calendar.getInstance().getTime()));
        }

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.show(manager, "dialog");
            }
        });

        textViewCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("title", getResources().getString(R.string.category));
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_category));
                intent.putExtra(DBHelper.KEY_FINANCE_TYPE, type_finance);
                startActivityForResult(intent, requestCodeCategory);
            }
        });

        textViewCurrencyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView currencyTextView = findViewById(R.id.textViewCurrency);
                intent.putExtra("title", currencyTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_currency));
                startActivityForResult(intent, requestCodeCurrency);
            }
        });

        editTextComment = findViewById(R.id.editTextComment);
        if (type_finance != DBHelper.FINANCE_TYPE_EXPENSES) {
            editTextComment.setVisibility(View.INVISIBLE);
        }

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = editTextAmount.getText().toString();
                if (amount.isEmpty() || category == -1 || currency == -1)
                    return;

                if (finance_id != -1) {
                    finance.updateFinance(type_finance,
                            Double.parseDouble(amount),
                            ConversionData.conversionRevertToDate(dateView.getText().toString()),
                            currency,
                            category,
                            editTextComment.getText().toString());
                } else {
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    finance = new Finance(database,
                            type_finance,
                            Double.parseDouble(amount),
                            ConversionData.conversionRevertToDate(dateView.getText().toString()),
                            currency,
                            category,
                            editTextComment.getText().toString()
                    );
                    finance.createFinance();
                }
                SPHelper.setBalanceTrue();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.menu_settings).setVisible(false);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case (requestCodeCategory): {
                if (resultCode == Activity.RESULT_OK) {
                    assert data != null;
                    category = data.getLongExtra("category", -1);
                    textViewCategoryName.setText(data.getStringExtra("category_name"));
                    textViewCategoryName.setTextColor(Color.BLACK);
                }
                break;
            }
            case (requestCodeCurrency): {
                if (resultCode == Activity.RESULT_OK) {
                    assert data != null;
                    currency = data.getLongExtra("currency", -1);
                    textViewCurrencyName.setText(data.getStringExtra("currency_name"));
                    textViewCurrencyName.setTextColor(Color.BLACK);
                }
                break;
            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        dateView.setText(ConversionData.conversionDateToRevert(calendar.getTime()));
        dateView.setTextColor(Color.BLACK);
    }
}