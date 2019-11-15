package com.namutomatvey.financialaccount.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.namutomatvey.financialaccount.ConversionData;
import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.SPHelper;
import com.namutomatvey.financialaccount.adapter.CheckAdapter;
import com.namutomatvey.financialaccount.adapter.ClientCheckAdapter;
import com.namutomatvey.financialaccount.dto.Finance;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class BeforeAppendStatisticActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;

    private GridView gridView;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    private Intent intent_barcode;
    private CheckAdapter checkAdapter;

    final List<Finance> finances = new ArrayList<Finance>();


    public static final int REQUEST_CODE_CATEGORY = 100;
    public static final int REQUEST_CODE_BARCODE = 102;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_append_statistic);

        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(getResources().getString(R.string.title_activity_qr));
        setSupportActionBar(mActionBarToolbar);

        gridView = findViewById(R.id.gridViewCheckItem);

        intent_barcode = new Intent(BeforeAppendStatisticActivity.this, BarcodeActivity.class);

        Button buttonChoiceCategory = findViewById(R.id.buttonChoiceCategory);
        buttonChoiceCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAdapter.getCheckFinance().size() == 0)
                    return;
                Intent intent = new Intent(BeforeAppendStatisticActivity.this, SelectionDataActivity.class);
                intent.putExtra("title", getResources().getString(R.string.category));
                intent.putExtra("number", getResources().getInteger(R.integer.click_button_category));
                intent.putExtra(DBHelper.KEY_FINANCE_TYPE, DBHelper.FINANCE_TYPE_EXPENSES);
                startActivityForResult(intent, REQUEST_CODE_CATEGORY);
            }
        });

        startActivityForResult(intent_barcode, REQUEST_CODE_BARCODE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        menu.findItem(R.id.menu_settings).setVisible(false);
        mActionBarToolbar.setNavigationIcon(R.drawable.ic_back);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CATEGORY && resultCode == RESULT_OK) {
            if (data != null) {
                long category = data.getLongExtra("category", -1);
                if (category != -1) {
                    List<Finance> temp_finances = checkAdapter.getCheckFinance();
                    Finance temp_finance;
                    for (int i = 0; i < temp_finances.size(); i += 1) {
                        temp_finance = temp_finances.get(i);
                        temp_finance.setCategory(category);
                        temp_finance.createFinance();
                        finances.remove(temp_finance);
                    }
                    checkAdapter.setFinances(finances);
                    checkAdapter.notifyDataSetChanged();
                    gridView.invalidateViews();
                    gridView.setAdapter(checkAdapter);
                    SPHelper.setBalanceTrue();
                }
            }
        } else if (requestCode == REQUEST_CODE_BARCODE && resultCode == RESULT_OK) {
            if (data != null) {
                Barcode barcode = data.getParcelableExtra("barcode");
                String qr_value = barcode.displayValue;
                ClientCheckAdapter clientIsCheckAdapter = new ClientCheckAdapter(SPHelper.getFnsPhone(), SPHelper.getFnsPassword());
                clientIsCheckAdapter.setQrData(qr_value);
                clientIsCheckAdapter.execute(ClientCheckAdapter.PURPOSE_IS_CHECK);
                JSONObject result = null;
                while (true) {
                    try {
                        Thread.sleep(1000);
                        result = clientIsCheckAdapter.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        continue;
                    }
                    break;
                }
                ClientCheckAdapter clientCheckAdapter = new ClientCheckAdapter(SPHelper.getFnsPhone(), SPHelper.getFnsPassword());
                try {
                    if (result == null) {
                        finish();
                        return;
                    } else {
                        if (result.getInt("code") == HttpsURLConnection.HTTP_NO_CONTENT) {
                            clientCheckAdapter.setQrData(qr_value);
                            clientCheckAdapter.execute(ClientCheckAdapter.PURPOSE_GET_CHECK);
                        } else {
                            Toast.makeText(BeforeAppendStatisticActivity.this, result.getString("error"), Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                while (true) {
                    try {
                        Thread.sleep(1000);
                        result = clientCheckAdapter.get();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        continue;
                    }
                    break;
                }
                try {
                    if (result.getInt("code") == HttpsURLConnection.HTTP_OK) {
                        dbHelper = new DBHelper(this);
                        database = dbHelper.getWritableDatabase();

                        JSONObject receipt = result.getJSONObject("massage").getJSONObject("document").getJSONObject("receipt");
                        String date = receipt.getString("dateTime").split("T")[0];
                        JSONArray items = receipt.getJSONArray("items");
                        for (int i = 0; i < items.length(); i += 1) {
                            JSONObject temp_item = new JSONObject(items.get(i).toString());
                            finances.add(new Finance(database,
                                    DBHelper.FINANCE_TYPE_EXPENSES,
                                    temp_item.getDouble("sum") / 100,
                                    ConversionData.conversionStringToDate(date),
                                    1,
                                    temp_item.getString("name")));
                        }
                        checkAdapter = new CheckAdapter(this, finances);
                        gridView.setAdapter(checkAdapter);

                    } else
                        Toast.makeText(BeforeAppendStatisticActivity.this, result.getString("error"), Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        if (finances.size() == 0)
            finish();
    }
}