package com.namutomatvey.financialaccount.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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

    private DBHelper dbHelper;
    private SQLiteDatabase database;
    private GridView gridView;
    private CategoryAdapter categoryAdapter;

    private Toolbar mActionBarToolbar;
    private Cursor cursor;
    private int number;
    private int resourceCategory;
    private int resourceCurrency;

    private EditText editTextNewCategory;

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
        LinearLayout linearLayoutCategory = findViewById(R.id.linearLayoutCategory);

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
            categoryAdapter = new CategoryAdapter(this, categories);
            gridView.setAdapter(categoryAdapter);

            editTextNewCategory = findViewById(R.id.editTextNewCategory);
            editTextNewCategory.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
                    int typeIndex = cursor.getColumnIndex(DBHelper.KEY_CATEGORY_TYPE);
                    int parentIndex = cursor.getColumnIndex(DBHelper.KEY_PARENT);

                    String tempCategoryName = editTextNewCategory.getText().toString().trim();
                    if(!tempCategoryName.equals(""))
                        cursor = database.query(DBHelper.TABLE_CATEGORY,
                                null,
                                DBHelper.KEY_CATEGORY_TYPE + " = " + categoryType + " AND " + DBHelper.KEY_NAME + " LIKE '" + tempCategoryName + "%'",
                                null, null, null, null);
                    else
                        cursor = database.query(DBHelper.TABLE_CATEGORY,
                                null,
                                DBHelper.KEY_CATEGORY_TYPE + " = " + categoryType,
                                null, null, null, null);
                    categories.clear();
                    if (cursor.moveToFirst()) {
                        do {
                            categories.add(new Category(database,
                                    cursor.getLong(idIndex),
                                    cursor.getString(nameIndex),
                                    cursor.getInt(typeIndex),
                                    cursor.getInt(parentIndex)));
                        } while (cursor.moveToNext());
                    }
                    categoryAdapter.setCategories(categories);
                    categoryAdapter.notifyDataSetChanged();
                    gridView.invalidateViews();
                    gridView.setAdapter(categoryAdapter);


                }
            });

            ImageButton imageButtonAccept = findViewById(R.id.imageButtonAccept);

            imageButtonAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String newCategory = editTextNewCategory.getText().toString().trim();
                    if (newCategory.isEmpty()) {
                        Toast.makeText(SelectionDataActivity.this, "Введите наименование категории", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if(checkCategoryName(newCategory, categoryType)){
                        Toast.makeText(SelectionDataActivity.this, "Такая категория уже существует", Toast.LENGTH_LONG).show();
                        return;
                    }
                    categoryAdapter.addingItemAdapter(new Category(database, newCategory, categoryType, null));
                    gridView.setAdapter(categoryAdapter);
                    editTextNewCategory.setText("");
                    InputMethodManager inputManager = (InputMethodManager) SelectionDataActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(SelectionDataActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            });
        } else if(number == resourceCurrency){
            linearLayoutCategory.setVisibility(View.GONE);

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
            LinearLayout linearLayoutSelection = findViewById(R.id.linearLayoutSelection);
            ViewGroup.LayoutParams params = linearLayoutSelection.getLayoutParams();
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
            }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Object o = gridView.getItemAtPosition(position);
                if (number == resourceCurrency) {
                    Currency currency = (Currency) o;
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("currency_name", currency.getShortName());
                    resultIntent.putExtra("currency", currency.getId());
                    setResult(Activity.RESULT_OK, resultIntent);
                } else if (number == resourceCategory) {
                    Category category = (Category) o;
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

    public boolean checkCategoryName(String name, Integer categoryType){
        cursor = database.query(DBHelper.TABLE_CATEGORY,
                null,
                DBHelper.KEY_CATEGORY_TYPE + " = " + categoryType + " AND " + DBHelper.KEY_NAME + " = '" + name + "'",
                null, null, null, null);
        return cursor.getCount() != 0;
    }
}
