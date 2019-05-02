package com.namutomatvey.financialaccount;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class EnterDataActivity extends AppCompatActivity {

    DBHelper dbHelper;
    private Toolbar mActionBarToolbar;
    private MenuItem menuMenuItem;
    private MenuItem backMenuItem;
    private MenuItem acceptMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        final Intent intent = new Intent(EnterDataActivity.this, SelectionDataActivity.class);

        ImageButton categoryImageButton = findViewById(R.id.imageButtonCategory);
        ImageButton currencyImageButton = findViewById(R.id.imageButtonCurrency);
        ImageButton cashVoucherImageButton = findViewById(R.id.imageButtonCashVoucher);
        EditText editTextDate = findViewById(R.id.editTextDate);

        dbHelper = new DBHelper(this);

        mActionBarToolbar = findViewById(R.id.toolbar_actionbar);
        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);


        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MyTag","Hello3");
                FragmentManager manager = getSupportFragmentManager();
                CalendarFragment calendarFragment = new CalendarFragment();
                calendarFragment.show(manager, "dialog");

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
        backMenuItem.setVisible(true);
        acceptMenuItem.setVisible(true);
        mActionBarToolbar.setLogo(R.drawable.icon_back);
        return true;
    }
}
