package com.namutomatvey.financialaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InformationMainActivity extends AppCompatActivity {
    Integer numberButtonActivityMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information_main);

        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        setTitle(title);
        numberButtonActivityMain = getIntent().getExtras().getInt("number",  getResources().getInteger(R.integer.error));
        Button enterButton = findViewById(R.id.buttonEnter);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent.putExtra("title", expensesTextView.getText().toString());
//                startActivity(intent);
            }
        });
    }
}
