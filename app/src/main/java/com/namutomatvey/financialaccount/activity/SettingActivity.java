package com.namutomatvey.financialaccount.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.namutomatvey.financialaccount.R;

public class SettingActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;
    private SharedPreferences mSettings;
    private Button buttonGoToFns;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_settings));
        setSupportActionBar(mActionBarToolbar);

        buttonGoToFns = findViewById(R.id.buttonGoToFns);
        buttonGoToFns.setOnClickListener(
            new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(SettingActivity.this, FnsActivity.class);
                     startActivity(intent);
                 }
             }
        );

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
}
