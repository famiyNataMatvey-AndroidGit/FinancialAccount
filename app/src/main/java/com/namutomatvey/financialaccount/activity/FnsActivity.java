package com.namutomatvey.financialaccount.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.SPHelper;
import com.namutomatvey.financialaccount.adapter.ClientCheckAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

public class FnsActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;
    private Pattern email_pattern;
    private Pattern phone_pattern;

    private int MODE_REGISTRATION = 1;
    private int MODE_LOGIN = 2;
    private int MODE_RECOVERY = 3;

    private int operation_mode = MODE_LOGIN;

    private static final String PHONE_PATTERN = "^\\+7[0-9]{10}$";

    private static final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" +
                    "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final int PERMISSION_REQUEST = 200;

    private Button registration;
    private Button recovery;
    private Button save;

    private LinearLayout layoutFnsEmail;
    private LinearLayout layoutFnsName;
    private LinearLayout layoutFnsPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fns);

        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_fns_login));
        setSupportActionBar(mActionBarToolbar);

        final EditText email = findViewById(R.id.editTextEmail);
        final EditText phone = findViewById(R.id.editTextPhone);
        final EditText name = findViewById(R.id.editTextName);
        final EditText password = findViewById(R.id.editTextPassword);

        layoutFnsEmail = findViewById(R.id.layoutFnsEmail);
        layoutFnsName = findViewById(R.id.layoutFnsName);
        layoutFnsPassword = findViewById(R.id.layoutFnsPassword);

        registration = findViewById(R.id.buttonReg);
        recovery = findViewById(R.id.buttonRec);
        save = findViewById(R.id.buttonSaveLogin);

        email.setText(SPHelper.getFnsEmail());
        name.setText(SPHelper.getFnsName());
        phone.setText(SPHelper.getFnsPhone());
        password.setText(SPHelper.getFnsClosePassword());

        email_pattern = Pattern.compile(EMAIL_PATTERN);
        phone_pattern = Pattern.compile(PHONE_PATTERN);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operation_mode != MODE_REGISTRATION) {
                    operation_mode = MODE_REGISTRATION;
                    mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_fns_registration));
                    recovery.setVisibility(View.GONE);
                    layoutFnsEmail.setVisibility(View.VISIBLE);
                    layoutFnsName.setVisibility(View.VISIBLE);
                    layoutFnsPassword.setVisibility(View.GONE);
                    return;
                }
                if (ContextCompat.checkSelfPermission(FnsActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FnsActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST);
                }

                String temp_email = email.getText().toString();
                Matcher email_matcher = email_pattern.matcher(temp_email);
                if (temp_email.isEmpty() | !email_matcher.matches()) {
                    Toast.makeText(FnsActivity.this, "Неверный формат Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                String temp_name = name.getText().toString();
                if (temp_name.isEmpty()) {
                    Toast.makeText(FnsActivity.this, "Поле имя пустое", Toast.LENGTH_SHORT).show();
                    return;
                }

                String temp_phone = phone.getText().toString();
                Matcher phone_matcher = phone_pattern.matcher(temp_phone);
                if (temp_phone.isEmpty() | !phone_matcher.matches()) {
                    Toast.makeText(FnsActivity.this, "Неверный формат Телефона: +70000000000", Toast.LENGTH_SHORT).show();
                    return;
                }

                ClientCheckAdapter clientCheckAdapter = new ClientCheckAdapter(temp_email, temp_name, temp_phone);
                clientCheckAdapter.execute(ClientCheckAdapter.PURPOSE_REGISTRATION);
                JSONObject result = null;
                try {
                    result = clientCheckAdapter.get();
                    if (result.getInt("code") == HttpsURLConnection.HTTP_NO_CONTENT) {
                        SPHelper.registrationFns(temp_name, temp_email, temp_phone);

                        operation_mode = MODE_LOGIN;
                        recovery.setVisibility(View.VISIBLE);
                        save.setVisibility(View.VISIBLE);
                        layoutFnsEmail.setVisibility(View.INVISIBLE);
                        layoutFnsName.setVisibility(View.INVISIBLE);
                        layoutFnsPassword.setVisibility(View.VISIBLE);
                    } else
                        Toast.makeText(FnsActivity.this, result.getString("error"), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        recovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operation_mode != MODE_RECOVERY) {
                    operation_mode = MODE_RECOVERY;
                    mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_fns_recovery));
                    registration.setVisibility(View.GONE);
                    layoutFnsPassword.setVisibility(View.GONE);
                    return;
                }
                if (ContextCompat.checkSelfPermission(FnsActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FnsActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST);
                }
                String temp_phone = phone.getText().toString();
                Matcher phone_matcher = phone_pattern.matcher(temp_phone);
                if (temp_phone.isEmpty() | !phone_matcher.matches()) {
                    Toast.makeText(FnsActivity.this, "Неверный формат Телефона: +70000000000", Toast.LENGTH_SHORT).show();
                    return;
                }

                ClientCheckAdapter clientCheckAdapter = new ClientCheckAdapter(temp_phone);
                clientCheckAdapter.execute(ClientCheckAdapter.PURPOSE_PASSWORD_RECOVERY);
                JSONObject result = null;
                try {
                    result = clientCheckAdapter.get();
                    if (result.getInt("code") == HttpsURLConnection.HTTP_NO_CONTENT) {
                        operation_mode = MODE_LOGIN;
                        registration.setVisibility(View.VISIBLE);
                        save.setVisibility(View.VISIBLE);
                        layoutFnsEmail.setVisibility(View.INVISIBLE);
                        layoutFnsName.setVisibility(View.INVISIBLE);
                        layoutFnsPassword.setVisibility(View.VISIBLE);
                    } else
                        Toast.makeText(FnsActivity.this, result.getString("error"), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (operation_mode != MODE_LOGIN) {
                    operation_mode = MODE_LOGIN;
                    mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_fns_login));
                    registration.setVisibility(View.VISIBLE);
                    recovery.setVisibility(View.VISIBLE);
                    layoutFnsPassword.setVisibility(View.VISIBLE);
                    layoutFnsEmail.setVisibility(View.GONE);
                    layoutFnsName.setVisibility(View.GONE);
                    return;
                }
                if (ContextCompat.checkSelfPermission(FnsActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(FnsActivity.this, new String[]{Manifest.permission.INTERNET}, PERMISSION_REQUEST);
                }

                String temp_phone = phone.getText().toString();
                Matcher phone_matcher = phone_pattern.matcher(temp_phone);
                if (temp_phone.isEmpty() | !phone_matcher.matches()) {
                    Toast.makeText(FnsActivity.this, "Неверный формат Телефона: +70000000000", Toast.LENGTH_SHORT).show();
                    return;
                }

                String temp_password = password.getText().toString();
                if (temp_password.isEmpty()) {
                    Toast.makeText(FnsActivity.this, "Поле пароль пустое", Toast.LENGTH_SHORT).show();
                    return;
                }
                ClientCheckAdapter clientCheckAdapter = new ClientCheckAdapter(temp_phone, temp_password);
                clientCheckAdapter.execute(ClientCheckAdapter.PURPOSE_LOGIN);
                JSONObject result = null;
                try {
                    result = clientCheckAdapter.get();
                    if (result.getInt("code") == HttpsURLConnection.HTTP_OK) {
                        SPHelper.loginFns(temp_phone, temp_password);
                        finish();
                    } else
                        Toast.makeText(FnsActivity.this, result.getString("error"), Toast.LENGTH_LONG).show();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
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