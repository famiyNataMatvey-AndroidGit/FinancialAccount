package com.namutomatvey.financialaccount.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.namutomatvey.financialaccount.R;

public class SettingActivity extends AppCompatActivity {

    private static final int RESULT_ACTIVITY_LANGUAGE = 1;
    private static final int RESULT_ACTIVITY_NOTIFICATION = 2;
    private static final int RESULT_ACTIVITY_PROBLEM = 3;
    private static final int RESULT_ACTIVITY_ABOUT_DEVELOPER = 4;

    private Toolbar mActionBarToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_settings));
        setSupportActionBar(mActionBarToolbar);

        Button buttonGoToFns = findViewById(R.id.buttonGoToFns);
        Button buttonLanguage = findViewById(R.id.buttonLanguage);
        Button buttonNotification = findViewById(R.id.buttonNotification);
        Button buttonProblem = findViewById(R.id.buttonProblem);
        Button buttonAboutDeveloper = findViewById(R.id.buttonAboutDeveloper);

        buttonGoToFns.setOnClickListener(
            new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                     Intent intent = new Intent(SettingActivity.this, FnsActivity.class);
                     startActivity(intent);
                 }
             }
        );

        buttonLanguage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingActivity.this, SettingDetailActivity.class);
                        intent.putExtra("selected_button", RESULT_ACTIVITY_LANGUAGE);
                        startActivity(intent);
                    }
                }
        );

        buttonNotification.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingActivity.this, SettingDetailActivity.class);
                        intent.putExtra("selected_button", RESULT_ACTIVITY_NOTIFICATION);
                        startActivity(intent);
                    }
                }
        );

        buttonProblem.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingActivity.this, SettingDetailActivity.class);
                        intent.putExtra("selected_button", RESULT_ACTIVITY_PROBLEM);
                        startActivity(intent);
                    }
                }
        );

        buttonAboutDeveloper.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SettingActivity.this, SettingDetailActivity.class);
                        intent.putExtra("selected_button", RESULT_ACTIVITY_ABOUT_DEVELOPER);
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
