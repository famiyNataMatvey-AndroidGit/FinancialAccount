package com.namutomatvey.financialaccount;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DataPrimerAdapter extends BaseAdapter {

    private final Context mContext;
    private final ArrayList currency;

    public DataPrimerAdapter(Context context, ArrayList currency) {
        this.mContext = context;
        this.currency = currency;
    }

    @Override
    public int getCount() {
        return currency.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView dummyTextView = new TextView(mContext);
        dummyTextView.setText((this.currency.get(position).toString()));
        return dummyTextView;
    }
}
