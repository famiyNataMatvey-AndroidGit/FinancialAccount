package com.namutomatvey.financialaccount.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.namutomatvey.financialaccount.dto.Finance;

import java.util.List;

public class FinanceAdapter extends BaseAdapter {

    private Context context;
    private List<Finance> finances;

    public FinanceAdapter(Context context, List<Finance> finances) {
        this.context = context;
        this.finances = finances;
    }

    @Override
    public int getCount() {
        return finances.size();
    }

    @Override
    public Object getItem(int position) {
        return finances.get(position);
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
            textView.setText("" + finances.get(position).getAmount());
        } else {
            textView = (TextView) convertView;
        }
        textView.setId(position);

        return textView;
    }
}
