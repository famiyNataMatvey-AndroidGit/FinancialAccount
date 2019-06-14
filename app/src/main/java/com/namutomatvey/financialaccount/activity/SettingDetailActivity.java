package com.namutomatvey.financialaccount.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;

import com.namutomatvey.financialaccount.R;

public class SettingDetailActivity extends AppCompatActivity {

    private static final int RESULT_ACTIVITY_LANGUAGE = 1;
    private static final int RESULT_ACTIVITY_NOTIFICATION = 2;
    private static final int RESULT_ACTIVITY_PROBLEM = 3;
    private static final int RESULT_ACTIVITY_ABOUT_DEVELOPER = 4;

    private Toolbar mActionBarToolbar;

    private SharedPreferences mSettings;

    private int selected_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_detail);

        mActionBarToolbar = findViewById(R.id.toolbar);
        String title = getIntent().getExtras().getString("title",  getResources().getString(R.string.title_activity_settings));
        mActionBarToolbar.setTitle(title);
        setSupportActionBar(mActionBarToolbar);

        selected_button = getIntent().getIntExtra("selected_button", RESULT_ACTIVITY_ABOUT_DEVELOPER);

        mSettings = getSharedPreferences(getResources().getString(R.string.APP_PREFERENCES), Context.MODE_PRIVATE);

        GridView gridViewLanguage = findViewById(R.id.gridViewLanguage);
        Switch switchNotification = findViewById(R.id.switchNotification);
        EditText editTextProblemMessage = findViewById(R.id.editTextProblemMessage);
        Button buttonProblemMessage = findViewById(R.id.buttonProblemMessage);
        TextView textViewAboutDeveloper = findViewById(R.id.textViewAboutDeveloper);

        switch (selected_button){
            case RESULT_ACTIVITY_LANGUAGE:
                gridViewLanguage.setVisibility(View.VISIBLE);
                break;
            case RESULT_ACTIVITY_NOTIFICATION:
                switchNotification.setVisibility(View.VISIBLE);
                break;
            case RESULT_ACTIVITY_PROBLEM:
                editTextProblemMessage.setVisibility(View.VISIBLE);
                buttonProblemMessage.setVisibility(View.VISIBLE);
                break;
            case RESULT_ACTIVITY_ABOUT_DEVELOPER:
                textViewAboutDeveloper.setVisibility(View.VISIBLE);
                break;
        }

        buttonProblemMessage.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putBoolean(getResources().getString(R.string.APP_PREFERENCES_FIRST_LAUNCH), true);
                        editor.putString(getResources().getString(R.string.APP_PREFERENCES_BALANCE), getResources().getString(R.string.default_hint_amount));
                        editor.apply();
                    }
                }
        );

        gridViewLanguage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(getResources().getString(R.string.APP_PREFERENCES_LANGUAGE), "Русский");
                editor.apply();
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
}
