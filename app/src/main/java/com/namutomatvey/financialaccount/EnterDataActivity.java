package com.namutomatvey.financialaccount;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class EnterDataActivity extends AppCompatActivity {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_data);
        final Intent intent = new Intent(EnterDataActivity.this, SelectionDataActivity.class);

        ImageButton categoryImageButton = findViewById(R.id.imageButtonCategory);
        ImageButton currencyImageButton = findViewById(R.id.imageButtonCurrency);
        ImageButton cashVoucherImageButton = findViewById(R.id.imageButtonCashVoucher);
        dbHelper = new DBHelper(this);

        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        setTitle(title);

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
}
