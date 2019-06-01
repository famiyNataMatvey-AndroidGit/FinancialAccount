package com.namutomatvey.financialaccount.activity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.namutomatvey.financialaccount.DBHelper;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.adapter.CheckAdapter;
import com.namutomatvey.financialaccount.adapter.ClientCheckAdapter;
import com.namutomatvey.financialaccount.dto.Finance;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class BeforeAppendStatisticActivity extends AppCompatActivity {

    private Toolbar mActionBarToolbar;
    private GridView gridView;
    private DBHelper dbHelper;
    private SQLiteDatabase database;

    public static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_append_statistic);

        mActionBarToolbar = findViewById(R.id.toolbar);
        mActionBarToolbar.setTitle(getResources().getString(R.string.expenses));
        setSupportActionBar(mActionBarToolbar);


        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();

        gridView = findViewById(R.id.gridViewCheckItem);

        final List<Finance> finances = new ArrayList<Finance>();
        for(int i = 1; i < 50; i += 1) {
            finances.add(new Finance(database, i, 1, 5000.00, "вт, 21 Май 2019", 1, 1, "Comment " + i));
        }
        gridView.setAdapter(new CheckAdapter(this, finances));

        Button buttonChoiceCategory = findViewById(R.id.buttonChoiceCategory);
        buttonChoiceCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BeforeAppendStatisticActivity.this, BarcodeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if(data != null) {
                Barcode barcode = data.getParcelableExtra("barcode");
                String qr_value = barcode.displayValue;
                ClientCheckAdapter clientCheckAdapter = new ClientCheckAdapter("+79054278197", "123");
                clientCheckAdapter.setQrData(qr_value);
                clientCheckAdapter.execute(ClientCheckAdapter.PURPOSE_GET_CHECK);
                JSONObject result = null;
                while (1 == 1) {
                    try {
                        result = clientCheckAdapter.get();
                        Log.d("MayTag", "get returns " + result);
                        Toast.makeText(BeforeAppendStatisticActivity.this, "get returns " + result, Toast.LENGTH_LONG).show();
                        break;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                }
            }
            finish();
        }
    }
}
