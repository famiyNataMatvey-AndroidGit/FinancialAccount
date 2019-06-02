package com.namutomatvey.financialaccount.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.adapter.ViewCategoryAdapter;
import com.namutomatvey.financialaccount.dto.ViewCategory;
import com.namutomatvey.financialaccount.fragment.CalendarFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class StatisticsActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private Toolbar mActionBarToolbar;
    private SharedPreferences sharedPreferences;
    private int number;
    private int datePickerType;
    private int datePickerIdentifier;

    private RelativeLayout datePickerLayout;
    private RelativeLayout datePickerCustomLayout;

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dayDateFormat = new SimpleDateFormat("EEE, dd MMM y");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat weekFromDateFormat = new SimpleDateFormat("dd MMM");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat weekToDateFormat = new SimpleDateFormat("dd MMM y");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat monthDateFormat = new SimpleDateFormat("MMMM y");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat yearDateFormat = new SimpleDateFormat("y");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat customDateFormat = new SimpleDateFormat("dd MMM y");

    private ImageButton imageButtonLeft;
    private ImageButton imageButtonRight;
    private ImageButton imageButtonDatePicker;
    private ImageButton imageButtonDateFromPicker;
    private ImageButton imageButtonDateToPicker;

    private TextView datePickerView;
    private TextView datePickerFromView;
    private TextView datePickerToView;

    private Date dateTo;
    private Date dateFrom;

    private String AS_CATEGORY_NAME = "category_name";
    private String AS_FINANCE_SUM = "amount_sum";
    private GridView gridViewFinanceCategory;

    private static final String APP_PREFERENCES = "mysettings";
    private static final String TYPE_DATE_PICKER= "datePickerType";
    private static final String TITLE= "titleStatisticsActivity";


    private int typeFinanceCategory() {
        switch (number) {
            case 2:
                return DBHelper.FINANCE_TYPE_INCOME;
            case 3:
                return DBHelper.FINANCE_TYPE_EXPENSES;
            case 4:
                return DBHelper.FINANCE_TYPE_MONEYBOX;
            default:
                return DBHelper.FINANCE_TYPE_INCOME;
        }
    }

    private void moveDatePickerType(Date date_to, Boolean is_direction) {
        int sign = 1;
        if(!is_direction)
            sign = -1;
        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date_to);
        if (datePickerType == getResources().getInteger(R.integer.date_picker_day)) {
            calendarDate.add(Calendar.DAY_OF_MONTH, sign);
        } else if (datePickerType == getResources().getInteger(R.integer.date_picker_week)) {
            calendarDate.add(Calendar.WEEK_OF_MONTH, sign);
        } else if (datePickerType == getResources().getInteger(R.integer.date_picker_month)) {
            calendarDate.add(Calendar.MONTH, sign);
        } else if (datePickerType == getResources().getInteger(R.integer.date_picker_year)) {
            calendarDate.add(Calendar.YEAR, sign);
        } else {
            return;
        }
        Date date = calendarDate.getTime();
        changeDatePickerType(null, date);
    }

    private void changeDatePickerType(@Nullable Date date_from, @Nullable Date date_to) {
        if(date_to == null) {
            Calendar calendar = Calendar.getInstance();
            date_to = calendar.getTime();
        }
        dateTo = date_to;

        Calendar calendarDate = Calendar.getInstance();
        calendarDate.setTime(date_to);
        if (datePickerType == getResources().getInteger(R.integer.date_picker_day)) {
            datePickerView.setText(dayDateFormat.format(date_to));
        } else if (datePickerType == getResources().getInteger(R.integer.date_picker_week)) {
            if(date_from == null) {
                calendarDate.add(Calendar.WEEK_OF_MONTH, -1);
                date_from = calendarDate.getTime();
            }
            dateFrom = date_from;
            datePickerView.setText(String.format("%s - %s", weekFromDateFormat.format(date_from), weekToDateFormat.format(date_to)));
        } else if (datePickerType == getResources().getInteger(R.integer.date_picker_month)) {
            datePickerView.setText(monthDateFormat.format(date_to));
        } else if (datePickerType == getResources().getInteger(R.integer.date_picker_year)) {
            datePickerView.setText(yearDateFormat.format(date_to));
        } else if (datePickerType == getResources().getInteger(R.integer.date_picker_custom)) {
            if(date_from == null)
                date_from = date_to;
            datePickerFromView.setText(customDateFormat.format(date_from));
            datePickerToView.setText(customDateFormat.format(date_to));
        }
    }

    private Date parseDate(String date){
        Date parse_date = null;
        try {
            parse_date = customDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse_date;
    }

    public void setTitle(int itemId){
        switch (itemId) {
            case R.id.menu_day:
                mActionBarToolbar.setTitle(getResources().getString(R.string.menu_period_day));
                datePickerType = getResources().getInteger(R.integer.date_picker_day);
                break;
            case R.id.menu_week:
                mActionBarToolbar.setTitle(getResources().getString(R.string.menu_period_week));
                datePickerType = getResources().getInteger(R.integer.date_picker_week);
                break;
            case R.id.menu_month:
                mActionBarToolbar.setTitle(getResources().getString(R.string.menu_period_month));
                datePickerType = getResources().getInteger(R.integer.date_picker_month);
                break;
            case R.id.menu_year:
                mActionBarToolbar.setTitle(getResources().getString(R.string.menu_period_year));
                datePickerType = getResources().getInteger(R.integer.date_picker_year);
                break;
            case R.id.menu_custom:
                mActionBarToolbar.setTitle(getResources().getString(R.string.menu_period_custom));
                datePickerType = getResources().getInteger(R.integer.date_picker_custom);
                break;
            case R.id.menu_all:
                mActionBarToolbar.setTitle(getResources().getString(R.string.menu_period_all));
                datePickerType = getResources().getInteger(R.integer.date_picker_all);
                break;
            default:
                this.finish();
        }
    }

    public void setPickerLayout(){
        if (datePickerType == getResources().getInteger(R.integer.date_picker_all)){
            datePickerLayout.setVisibility(View.GONE);
            datePickerCustomLayout.setVisibility(View.GONE);
        } else if (datePickerType == getResources().getInteger(R.integer.date_picker_custom)) {
            datePickerLayout.setVisibility(View.GONE);
            datePickerCustomLayout.setVisibility(View.VISIBLE);
        }  else if (datePickerType == getResources().getInteger(R.integer.date_picker_day) || datePickerType == getResources().getInteger(R.integer.date_picker_week)) {
            datePickerLayout.setVisibility(View.VISIBLE);
            imageButtonDatePicker.setVisibility(View.VISIBLE);
            datePickerCustomLayout.setVisibility(View.GONE);
        } else {
            datePickerLayout.setVisibility(View.VISIBLE);
            imageButtonDatePicker.setVisibility(View.GONE);
            datePickerCustomLayout.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        number = getIntent().getExtras().getInt("number",  2);
        dbHelper = new DBHelper(this);

        datePickerLayout = findViewById(R.id.datePickerLayout);
        datePickerCustomLayout = findViewById(R.id.datePickerCustomLayout);

        imageButtonLeft = findViewById(R.id.imageButtonLeft);
        imageButtonRight = findViewById(R.id.imageButtonRight);
        imageButtonDatePicker = findViewById(R.id.imageButtonDatePicker);
        imageButtonDateFromPicker = findViewById(R.id.imageButtonDateFromPicker);
        imageButtonDateToPicker = findViewById(R.id.imageButtonDateToPicker);

        datePickerView = findViewById(R.id.datePickerView);
        datePickerFromView = findViewById(R.id.datePickerFromView);
        datePickerToView = findViewById(R.id.datePickerToView);

        gridViewFinanceCategory = findViewById(R.id.gridViewFinanceCategory);

        sharedPreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if ( !sharedPreferences.contains(TYPE_DATE_PICKER))   // приложение запущено впервые
        {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(TYPE_DATE_PICKER,  getResources().getInteger(R.integer.date_picker_day));
            editor.putString(TITLE,  getResources().getString(R.string.menu_period_day));
            editor.apply();
        }
        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(sharedPreferences.getString(TITLE, getResources().getString(R.string.menu_period_day)));
        setSupportActionBar(mActionBarToolbar);

        datePickerType = sharedPreferences.getInt(TYPE_DATE_PICKER, getResources().getInteger(R.integer.date_picker_day));
        setPickerLayout();

        String table = DBHelper.TABLE_FINANCE + " left outer join " + DBHelper.TABLE_CATEGORY + " on "
                + DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_CATEGORY + " = " + DBHelper.TABLE_CATEGORY
                + "." + DBHelper.KEY_ID;
        String columns[] = {
                DBHelper.TABLE_CATEGORY + "." + DBHelper.KEY_NAME + " as " + AS_CATEGORY_NAME,
                "sum(" + DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_AMOUNT + ") as " + AS_FINANCE_SUM,
                DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_CATEGORY};
        String selection = DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_TYPE + " = " + typeFinanceCategory();
        String groupBy = DBHelper.TABLE_FINANCE + "." + DBHelper.KEY_FINANCE_CATEGORY;
        dbHelper = new DBHelper(this);
        database = dbHelper.getReadableDatabase();
        Cursor cursor = database.query(table, columns, selection, null, groupBy, null, null);

        final List<ViewCategory> viewCategories = new ArrayList<ViewCategory>();
        if (cursor.moveToFirst()) {
            int categoryIndex = cursor.getColumnIndex(DBHelper.KEY_FINANCE_CATEGORY);
            int categoryNameIndex = cursor.getColumnIndex(AS_CATEGORY_NAME);
            int sumIndex = cursor.getColumnIndex(AS_FINANCE_SUM);
            do {

                viewCategories.add(new ViewCategory(
                        cursor.getLong(categoryIndex),
                        cursor.getString(categoryNameIndex),
                        cursor.getDouble(sumIndex)));

            } while (cursor.moveToNext());
        }
        gridViewFinanceCategory.setAdapter(new ViewCategoryAdapter(this, viewCategories));

        changeDatePickerType(null, null);

        imageButtonLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDatePickerType(dateTo, false);
            }
        });

        imageButtonRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moveDatePickerType(dateTo,true);
            }
        });

        imageButtonDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerIdentifier = 1;
                FragmentManager manager = getSupportFragmentManager();
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.show(manager, "dialog");
            }
        });

        imageButtonDateFromPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerIdentifier = 2;
                FragmentManager manager = getSupportFragmentManager();
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.show(manager, "dialog");
            }
        });

        imageButtonDateToPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerIdentifier = 3;
                FragmentManager manager = getSupportFragmentManager();
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.show(manager, "dialog");
            }
        });

        gridViewFinanceCategory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v,  int position, long id) {
                ViewCategory item = (ViewCategory) gridViewFinanceCategory.getItemAtPosition(position);
                Intent intent = new Intent(StatisticsActivity.this, FinanceActivity.class);
                intent.putExtra("title", item.getCategory());
                intent.putExtra("number", number);
                intent.putExtra("categoryId", item.getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.setGroupVisible(R.id.menu_group_period, true);
        MenuItem setting_item = menu.findItem(R.id.menu_settings);
        setting_item.setVisible(false);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setTitle(item.getItemId());
        setPickerLayout();
        changeDatePickerType(null, null);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        Date date = calendar.getTime();
        switch (datePickerIdentifier){
            case 1:
                changeDatePickerType(null, date);
                break;
            case 2:
                String to = datePickerToView.getText().toString();
                changeDatePickerType(date, parseDate(to));
                break;
            case 3:
                String from = datePickerFromView.getText().toString();
                changeDatePickerType(parseDate(from), date);
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(TYPE_DATE_PICKER,  datePickerType);
        editor.putString(TITLE,  mActionBarToolbar.getTitle().toString());
        editor.apply();
    }

}
