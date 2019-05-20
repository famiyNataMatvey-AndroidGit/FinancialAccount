package com.namutomatvey.financialaccount.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.adapter.CategoryAdapter;
import com.namutomatvey.financialaccount.adapter.CurrencyAdapter;
import com.namutomatvey.financialaccount.dto.Category;
import com.namutomatvey.financialaccount.dto.Currency;

import java.util.ArrayList;
import java.util.List;
//implements AdapterView.OnItemSelectedListener
public class SelectionDataActivity extends AppCompatActivity  {

    DBHelper dbHelper;
    SQLiteDatabase database;
    GridView gridView;

    private Toolbar mActionBarToolbar;
    private MenuItem acceptMenuItem;
    Cursor cursor;
    int number;
    int resourceCategory;
    int resourceCurrency;

    EditText editTextNewCategory;
    ImageButton imageButtonAccept;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_data);

        resourceCategory = getResources().getInteger(R.integer.click_button_category);
        resourceCurrency = getResources().getInteger(R.integer.click_button_currency);

        mActionBarToolbar = findViewById(R.id.toolbar);
        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        number = getIntent().getIntExtra("number", resourceCategory);
        gridView = findViewById(R.id.gridViewSelection);

        dbHelper = new DBHelper(this);
        if(number == resourceCategory) {
            final Integer categoryType = getIntent().getIntExtra(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_INCOME);
            database = dbHelper.getWritableDatabase();
            cursor = database.query(DBHelper.TABLE_CATEGORY, null, DBHelper.KEY_CATEGORY_TYPE + " = " + categoryType, null, null, null, null);
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int typeIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORY_TYPE);
            int parentIndex = cursor.getColumnIndex(DBHelper.KEY_PARENT);
            final List<Category> categories = new ArrayList<Category>();
            if (cursor.moveToFirst()) {
                do {
                    categories.add(new Category(database,
                                                cursor.getLong(idIndex),
                                                cursor.getString(nameIndex),
                                                cursor.getInt(typeIndex),
                                                cursor.getInt(parentIndex)));
                } while (cursor.moveToNext());
            }
            gridView.setAdapter(new CategoryAdapter(this, categories));

            editTextNewCategory = findViewById(R.id.editTextNewCategory);
            imageButtonAccept = findViewById(R.id.imageButtonAccept);

            imageButtonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newCategory = editTextNewCategory.getText().toString();
                    if (newCategory.isEmpty()) {
                        Toast.makeText(SelectionDataActivity.this, "Введите наименование категории", Toast.LENGTH_LONG).show();
                        return;
                    }
                    categories.add(new Category(database, newCategory, categoryType, null));
                }
            });
        } else if(number == resourceCurrency){
            LinearLayout linearLayoutCategory = findViewById(R.id.linearLayoutCategory);
            linearLayoutCategory.setVisibility(View.INVISIBLE);
            database = dbHelper.getReadableDatabase();
            cursor = database.query(DBHelper.TABLE_CURRENCY, null, null, null, null, null, null);
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int shortNameIndex = cursor.getColumnIndex(DBHelper.KEY_SHORT_NAME);
            int coefficientIndex = cursor.getColumnIndex(DBHelper.KEY_COEFFICIENT);
            List<Currency> currencies = new ArrayList<Currency>();
            if (cursor.moveToFirst()) {
                do {
                    currencies.add(new Currency(cursor.getLong(idIndex),
                                                cursor.getString(nameIndex),
                                                cursor.getString(shortNameIndex),
                                                cursor.getDouble(coefficientIndex)));
                } while (cursor.moveToNext());
            }
            gridView.setAdapter(new CurrencyAdapter(this, currencies));
        }
//        gridView.setOnItemSelectedListener(this);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // TODO Auto-generated method stub
                Object o = gridView.getItemAtPosition(position);
                if (number == resourceCurrency) {
                    Currency currency = (Currency) o;
                    Toast.makeText(SelectionDataActivity.this, "Selected :" + " " + currency.getName(), Toast.LENGTH_LONG).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("currency_name", currency.getName());
                    resultIntent.putExtra("currency", currency.getId());
                    setResult(Activity.RESULT_OK, resultIntent);
                } else if (number == resourceCategory) {
                    Category category = (Category) o;
                    Toast.makeText(SelectionDataActivity.this, "Selected :" + " " + category.getName(), Toast.LENGTH_LONG).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("category_name", category.getName());
                    resultIntent.putExtra("category", category.getId());
                    setResult(Activity.RESULT_OK, resultIntent);
                }
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.setGroupVisible(R.id.group_menu, false);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }
}
