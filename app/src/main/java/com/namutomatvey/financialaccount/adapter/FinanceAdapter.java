package com.namutomatvey.financialaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
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
            convertView = layoutInflater.inflate(R.layout.item_finance_layout, null);
            holder = new ViewHolder();
            holder.StatisticsChoice = convertView.findViewById(R.id.imageViewStatisticsChoice);
            holder.StatisticsDate = convertView.findViewById(R.id.textViewStatisticsDate);
            holder.StatisticsComment = convertView.findViewById(R.id.textViewStatisticsComment);
            holder.StatisticsAmount = convertView.findViewById(R.id.textViewStatisticsAmount);
            holder.StatisticsCurrency = convertView.findViewById(R.id.textViewStatisticsCurrency);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Finance finance = this.finances.get(position);
        holder.StatisticsDate.setText(finance.getDate());
        holder.StatisticsComment.setText(finance.getComment());
        holder.StatisticsAmount.setText(Double.toString(finance.getAmount()));
        holder.StatisticsCurrency.setText(" RUS");
        return convertView;
    }

    static class ViewHolder {
        ImageView StatisticsChoice;
        TextView StatisticsDate;
        TextView StatisticsComment;
        TextView StatisticsAmount;
        TextView StatisticsCurrency;
    }
}
