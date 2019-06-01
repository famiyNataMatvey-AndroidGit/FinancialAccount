package com.namutomatvey.financialaccount.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentValues;
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

import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.dto.Category;
import com.namutomatvey.financialaccount.fragment.CalendarFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EnterDataActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private DBHelper dbHelper;
    private Toolbar mActionBarToolbar;
    private Intent intent;
    private Date date ;
    private SimpleDateFormat simpleDate;
    private static final int requestCodeCategory = 1;
    private static final int requestCodeCurrency = 2;
    private TextView dateView;
    private TextView textViewCategoryName;
    private TextView textViewCurrencyName;
    private EditText editTextComment;
    private long category;
    private long currency;
    private int number;

    private int typeFinanceCategory() {
        switch (number) {
            case 2:
                return DBHelper.FINANCE_TYPE_INCOME;
            case 3:
                return DBHelper.FINANCE_TYPE_EXPENSES;
            case 4:
                return DBHelper.FINANCE_TYPE_MONEYBOX;
            default:
                return 0;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);

        intent = new Intent(EnterDataActivity.this, SelectionDataActivity.class);
        number = getIntent().getExtras().getInt("number",  2);

        mActionBarToolbar = findViewById(R.id.toolbar);
        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        long finance_id = getIntent().getExtras().getLong("finance_id", -1);
        dbHelper = new DBHelper(this);
        if (finance_id != -1) {
            SQLiteDatabase database = dbHelper.getReadableDatabase();
            Cursor cursor = database.query(DBHelper.TABLE_CATEGORY, null, DBHelper.KEY_ID + " = " + finance_id, null, null, null, null);
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int amountIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_AMOUNT);
            int categoryIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_CATEGORY);
            int commentIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_COMMENT);
            int currencyIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_CURRENCY);
            int dateIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_DATE);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_TYPE);
            final List<Category> categories = new ArrayList<Category>();
            if (cursor.moveToFirst()) {

//                categories.add(new Category(database,
//                        cursor.getLong(idIndex),
//                        cursor.getString(nameIndex),
//                        cursor.getInt(typeIndex),
//                        cursor.getInt(parentIndex)));
            }

        }

        date = Calendar.getInstance().getTime();
        simpleDate = new SimpleDateFormat("dd.MM.yyyy");

        dateView = findViewById(R.id.textViewDate);
        dateView.setText(simpleDate.format(date));
        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.show(manager, "dialog");
            }
        });

        textViewCategoryName = findViewById(R.id.financeItemCategoryName);
        textViewCategoryName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("title", getResources().getString(R.string.category));
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_category));
                intent.putExtra(DBHelper.KEY_FINANCE_TYPE, typeFinanceCategory());
                startActivityForResult(intent, requestCodeCategory);
            }
        });

        textViewCurrencyName = findViewById(R.id.textViewCurrencyName);
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
        if(typeFinanceCategory() != DBHelper.FINANCE_TYPE_EXPENSES){
            editTextComment.setVisibility(View.INVISIBLE);
        }

        Button buttonSave = findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextAmount = findViewById(R.id.editTextAmount);

                SQLiteDatabase database = dbHelper.getWritableDatabase();
                ContentValues contentCurrencyValues = new ContentValues();
                String amount = editTextAmount.getText().toString();
                if(amount.isEmpty() || category == -1 || currency == -1)
                    return;
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_TYPE, typeFinanceCategory());
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_DATE, dateView.getText().toString());
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_AMOUNT, Double.parseDouble(amount));
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_CATEGORY, category);
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_CURRENCY, currency);
                contentCurrencyValues.put(DBHelper.KEY_FINANCE_COMMENT, editTextComment.getText().toString());
                database.insert(DBHelper.TABLE_FINANCE, null, contentCurrencyValues);
                finish();
            }
        });
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch(requestCode) {
            case (requestCodeCategory) : {
                if (resultCode == Activity.RESULT_OK) {
                    assert data != null;
                    category = data.getLongExtra("category", -1);
                    textViewCategoryName.setText(data.getStringExtra("category_name"));
                }
                break;
            }
            case (requestCodeCurrency) : {
                if (resultCode == Activity.RESULT_OK) {
                    assert data != null;
                    currency = data.getLongExtra("currency", -1);
                    textViewCurrencyName.setText(data.getStringExtra("currency_name"));
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
        date = calendar.getTime();
        dateView.setText(simpleDate.format(date));
        dateView.setTextColor(Color.BLACK);
    }
}
