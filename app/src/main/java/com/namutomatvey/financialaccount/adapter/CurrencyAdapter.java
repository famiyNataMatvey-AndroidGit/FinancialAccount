package com.namutomatvey.financialaccount.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namutomatvey.financialaccount.dto.Currency;

import java.util.List;

public class CurrencyAdapter extends BaseAdapter {

    private Context context;
    private List<Currency> currencies;

    public CurrencyAdapter(Context context, List<Currency> currencies) {
        this.context = context;
        this.currencies = currencies;
    }

    @Override
    public int getCount() {
        return currencies.size();
    }

    @Override
    public Object getItem(int position) {
        return currencies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(context);
            textView.setText(currencies.get(position).getName());
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(18);
        } else {
            textView = (TextView) convertView;
        }
//        textView.setBackgroundResource(R.drawable.border_box);
        textView.setId(position);

        return textView;
    }
}