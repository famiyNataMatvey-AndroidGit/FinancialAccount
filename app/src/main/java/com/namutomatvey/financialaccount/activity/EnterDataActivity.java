package com.namutomatvey.financialaccount.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.namutomatvey.financialaccount.fragment.CalendarFragment;
import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.fragment.TimePickerFragment;

public class EnterDataActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    DBHelper dbHelper;
    private Toolbar mActionBarToolbar;
    private Intent intent;
    private static final int requestCodeCategory = 1;
    private static final int requestCodeCurrency = 2;
    MenuItem menuMenuItem;
    MenuItem backMenuItem;
    MenuItem acceptMenuItem;
    TextView dateView;
    TextView timeView;
    EditText amountView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        intent = new Intent(EnterDataActivity.this, SelectionDataActivity.class);

        ImageButton categoryImageButton = findViewById(R.id.imageButtonCategory);
        ImageButton currencyImageButton = findViewById(R.id.imageButtonCurrency);
        ImageButton cashVoucherImageButton = findViewById(R.id.imageButtonCashVoucher);
        dateView = findViewById(R.id.textTextDate);
        timeView = findViewById(R.id.textTextTime);
        amountView = findViewById(R.id.editTextAmount);

        dbHelper = new DBHelper(this);

        mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.show(manager, "dialog");

            }
        });

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment .show(manager, "dialog");

            }
        });

        categoryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView categoryTextView = findViewById(R.id.textViewCategory);
                intent.putExtra("title", categoryTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_category));
                int number = getIntent().getExtras().getInt("number",  2);
                switch (number) {
                    case 2:
                        intent.putExtra(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_INCOME);
                        break;
                    case 3:
                        intent.putExtra(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_EXPENSES);
                        break;
                    case 4:
                        intent.putExtra(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_MONEYBOX);
                        break;
                }
                startActivityForResult(intent, requestCodeCategory);

            }
        });

        currencyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView currencyTextView = findViewById(R.id.textViewCurrency);
                intent.putExtra("title", currencyTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_currency));
                startActivityForResult(intent, requestCodeCurrency);

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        menuMenuItem = menu.findItem(R.id.action_menu);
        backMenuItem = menu.findItem(R.id.action_back);
        acceptMenuItem = menu.findItem(R.id.action_accept);
        menuMenuItem.setVisible(false);
        backMenuItem.setVisible(false);
        acceptMenuItem.setVisible(true);
//        mActionBarToolbar.setLogo(R.drawable.icon_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        dbHelper = new DBHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        if (item.equals(acceptMenuItem)) {
            ContentValues contentCurrencyValues = new ContentValues();
            if(!typeFinanceCategory(contentCurrencyValues))
                return super.onOptionsItemSelected(item);
            contentCurrencyValues.put(DBHelper.KEY_FINANCE_DATE_TIME, dateView.getText().toString() + ' ' + timeView.getText().toString());
            contentCurrencyValues.put(DBHelper.KEY_FINANCE_AMOUNT, Double.parseDouble(amountView.getText().toString()));
            contentCurrencyValues.put(DBHelper.KEY_FINANCE_CATEGORY, 1);
            contentCurrencyValues.put(DBHelper.KEY_FINANCE_CURRENCY, 1);
            database.insert(DBHelper.TABLE_FINANCE, null, contentCurrencyValues);
        }
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    private boolean typeFinanceCategory(ContentValues contentCurrencyValues) {
        int number = getIntent().getExtras().getInt("number",  getResources().getInteger(R.integer.click_button_income));
        switch (number) {
            case 2:
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_INCOME);
                return true;
            case 3:
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_EXPENSES);
                return true;
            case 4:
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_MONEYBOX);
                return true;
            default:
                return false;
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeView = findViewById(R.id.textTextTime);
        timeView.setText("" + hourOfDay + ':' + minute);
        timeView.setTextColor(Color.BLACK);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        Log.d("MyTag", "" + resultCode);
        switch(requestCode) {
            case (requestCodeCategory) : {
                if (resultCode == Activity.RESULT_OK) {
                    assert data != null;
                    long returnValue = data.getLongExtra("category", -1);
                    Log.d("MyTag", Long.toString(returnValue));

                }
                break;
            }
            case (requestCodeCurrency) : {
                if (resultCode == Activity.RESULT_OK) {
                    assert data != null;
                    long returnValue = data.getLongExtra("currency", -1);
                    Log.d("MyTag", Long.toString(returnValue));
                }
                break;

            }
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateView = findViewById(R.id.textTextDate);
        dateView.setText("" + dayOfMonth + "-" + month + "-" + year);
        dateView.setTextColor(Color.BLACK);
    }
}
