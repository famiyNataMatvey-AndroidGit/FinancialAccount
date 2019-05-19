package com.namutomatvey.financialaccount.adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.dto.Finance;

import java.util.List;

public class FinanceAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Finance> finances;

    public FinanceAdapter(Context context, List<Finance> finances) {
        this.context = context;
        this.finances = finances;
        layoutInflater = LayoutInflater.from(context);
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


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.finance_item_layout, null);
            holder = new ViewHolder();
            holder.categoryNameView = convertView.findViewById(R.id.textViewCategoryName);
            holder.amountView = convertView.findViewById(R.id.textViewAmount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Finance finance = this.finances.get(position);
        holder.categoryNameView.setText(finance.getComment());
        holder.amountView.setText(Double.toString(finance.getAmount()));
        return convertView;
    }

    static class ViewHolder {
        TextView categoryNameView;
        TextView amountView;
    }
}
