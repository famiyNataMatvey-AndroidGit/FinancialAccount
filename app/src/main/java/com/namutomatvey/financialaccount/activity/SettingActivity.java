package com.namutomatvey.financialaccount.activity;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.adapter.ClientCheckAdapter;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SettingActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;
    private SharedPreferences mSettings;
    private Pattern email_pattern;
    private Pattern phone_pattern;

    private static final String PHONE_PATTERN = "^\\+7[0-9]{10}$";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_FNS_EMAIL = "fns_email";
    public static final String APP_PREFERENCES_FNS_NAME = "fns_name";
    public static final String APP_PREFERENCES_FNS_PHONE = "fns_phone";
    public static final String APP_PREFERENCES_FNS_PASSWORD = "fns_phone";

    public static final int PERMISSION_REQUEST = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_settings));
        setSupportActionBar(mActionBarToolbar);

        final EditText email = findViewById(R.id.editTextEmail);
        final EditText phone = findViewById(R.id.editTextPhone);
        final EditText name = findViewById(R.id.editTextName);
        final EditText password = findViewById(R.id.editTextPassword);

        Button reg = findViewById(R.id.buttonReg);
        Button rec = findViewById(R.id.buttonRec);
        Button save = findViewById(R.id.buttonSaveLogin);

        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if (!mSettings.contains(APP_PREFERENCES_FNS_EMAIL))
            email.setText(mSettings.getString(APP_PREFERENCES_FNS_EMAIL, ""));
        if (!mSettings.contains(APP_PREFERENCES_FNS_NAME))
            name.setText(mSettings.getString(APP_PREFERENCES_FNS_NAME, ""));
        if (!mSettings.contains(APP_PREFERENCES_FNS_PHONE))
            phone.setText(mSettings.getString(APP_PREFERENCES_FNS_PHONE, ""));
        if (!mSettings.contains(APP_PREFERENCES_FNS_PASSWORD))
            password.setText(mSettings.getString(APP_PREFERENCES_FNS_PASSWORD, ""));

        email_pattern = Pattern.compile(EMAIL_PATTERN);
        phone_pattern = Pattern.compile(PHONE_PATTERN);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST);
                }
                String temp_email = email.getText().toString();
                Matcher email_matcher = email_pattern.matcher(temp_email);
                if(temp_email.isEmpty() | !email_matcher.matches()) {
                    Toast.makeText(SettingActivity.this, "Неверный формат Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                String temp_name = name.getText().toString();
                if(temp_name.isEmpty()) {
                    Toast.makeText(SettingActivity.this, "Поле имя пустое", Toast.LENGTH_SHORT).show();
                    return;
                }

                String temp_phone = phone.getText().toString();
                Matcher phone_matcher = phone_pattern.matcher(temp_phone);
                if(temp_phone.isEmpty() | !phone_matcher.matches()) {
                    Toast.makeText(SettingActivity.this, "Неверный формат Телефона: +70000000000", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_FNS_EMAIL, temp_email);
                editor.putString(APP_PREFERENCES_FNS_NAME, temp_name);
                editor.putString(APP_PREFERENCES_FNS_PHONE, temp_phone);
                editor.apply();

                ClientCheckAdapter clientCheckAdapter = new ClientCheckAdapter(temp_email, temp_name, temp_phone);
                clientCheckAdapter.execute(ClientCheckAdapter.PURPOSE_REGISTRATION);
                JSONObject result = null;
                try {
                    result = clientCheckAdapter.get();
                    Log.d("MayTag", "get returns " + result);
                    Toast.makeText(SettingActivity.this, "get returns " + result, Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST);
                }
                String temp_phone = phone.getText().toString();
                Matcher phone_matcher = phone_pattern.matcher(temp_phone);
                if(temp_phone.isEmpty() | !phone_matcher.matches()) {
                    Toast.makeText(SettingActivity.this, "Неверный формат Телефона: +70000000000", Toast.LENGTH_SHORT).show();
                    return;
                }

                SharedPreferences.Editor editor = mSettings.edit();
                editor.putString(APP_PREFERENCES_FNS_PHONE, temp_phone);
                editor.apply();

                ClientCheckAdapter clientCheckAdapter = new ClientCheckAdapter(temp_phone);
                clientCheckAdapter.execute(ClientCheckAdapter.PURPOSE_PASSWORD_RECOVERY);
                JSONObject result = null;
                try {
                    result = clientCheckAdapter.get();
                    Log.d("MayTag", "get returns " + result);
                    Toast.makeText(SettingActivity.this, "get returns " + result, Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(SettingActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SettingActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST);
                }

                String temp_phone = phone.getText().toString();
                Matcher phone_matcher = phone_pattern.matcher(temp_phone);
                if(temp_phone.isEmpty() | !phone_matcher.matches()) {
                    Toast.makeText(SettingActivity.this, "Неверный формат Телефона: +70000000000", Toast.LENGTH_SHORT).show();
                    return;
                }

                String temp_password = password.getText().toString();
                if(temp_password.isEmpty()) {
                    Toast.makeText(SettingActivity.this, "Поле пароль пустое", Toast.LENGTH_SHORT).show();
                    return;
                }
                ClientCheckAdapter clientCheckAdapter = new ClientCheckAdapter(temp_phone, temp_password);
                clientCheckAdapter.execute(ClientCheckAdapter.PURPOSE_LOGIN);
                JSONObject result = null;
                try {
                    result = clientCheckAdapter.get();
                    Log.d("MayTag", "get returns " + result);
                    Toast.makeText(SettingActivity.this, "get returns " + result, Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
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
