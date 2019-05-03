package com.namutomatvey.financialaccount;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

public class EnterDataActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    DBHelper dbHelper;
    private Toolbar mActionBarToolbar;
    TextView dateView;
    TextView timeView;

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
        MenuItem menuMenuItem = menu.findItem(R.id.action_menu);
        MenuItem backMenuItem = menu.findItem(R.id.action_back);
        MenuItem acceptMenuItem = menu.findItem(R.id.action_accept);
        menuMenuItem.setVisible(false);
        backMenuItem.setVisible(false);
        acceptMenuItem.setVisible(true);
        mActionBarToolbar.setLogo(R.drawable.icon_back);
        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        timeView =  findViewById(R.id.textTextTime);
        timeView.setText("" + hourOfDay + ':' + minute);
        timeView.setTextColor(Color.BLACK);

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateView =  findViewById(R.id.textTextDate);
        dateView.setText("" + dayOfMonth + "-" + month + "-" + year);
        dateView.setTextColor(Color.BLACK);
    }
}
