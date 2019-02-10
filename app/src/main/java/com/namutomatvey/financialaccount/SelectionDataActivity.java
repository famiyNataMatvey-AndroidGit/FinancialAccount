package com.namutomatvey.financialaccount;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SelectionDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection_data);
        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.app_name));
        setTitle(title);
    }
}
