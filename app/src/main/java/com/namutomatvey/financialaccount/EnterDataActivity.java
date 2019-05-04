package com.namutomatvey.financialaccount;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
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

public class EnterDataActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    DBHelper dbHelper;
    private Toolbar mActionBarToolbar;
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
        final Intent intent = new Intent(EnterDataActivity.this, SelectionDataActivity.class);

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
                Log.d("MyTag","Hello3");
                FragmentManager manager = getSupportFragmentManager();
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.show(manager, "dialog");

            }
        });

        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyTag","Hello3");
                FragmentManager manager = getSupportFragmentManager();
                TimePickerFragment timePickerFragment = new TimePickerFragment();
                timePickerFragment .show(manager, "dialog");

            }
        });

        categoryImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyTag","Hello1");
                TextView categoryTextView = findViewById(R.id.textViewCategory);
                intent.putExtra("title", categoryTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_category));

                startActivity(intent);

            }
        });

        currencyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyTag","Hello2");
                TextView currencyTextView = findViewById(R.id.textViewCurrency);
                intent.putExtra("title", currencyTextView.getText().toString());
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_currency));
                startActivity(intent);

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
            contentCurrencyValues.put(DBHelper.KEY_FINANCE_AMOUNT, Integer.parseInt(amountView.getText().toString()));
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
                Log.d("MyTag","Страница Доходов");
                return true;
            case 3:
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_EXPENSES);
                Log.d("MyTag","Страница Расходов");
                return true;
            case 4:
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_MONEYBOX);
                Log.d("MyTag","Страница Копилка");
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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateView = findViewById(R.id.textTextDate);
        dateView.setText("" + dayOfMonth + "-" + month + "-" + year);
        dateView.setTextColor(Color.BLACK);
    }
}
