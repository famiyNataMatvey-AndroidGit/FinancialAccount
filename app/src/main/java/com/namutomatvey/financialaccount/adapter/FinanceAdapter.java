package com.namutomatvey.financialaccount.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.support.v7.widget.PopupMenu;
import android.widget.TextView;

import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.activity.EnterDataActivity;
import com.namutomatvey.financialaccount.activity.FinanceActivity;
import com.namutomatvey.financialaccount.dto.Finance;

import java.text.DecimalFormat;
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
    public Finance getItem(int position) {
        return finances.get(position);
    }

    @Override
    public long getItemId(int position) {
        return this.getItem(position).getId();
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
        holder.StatisticsAmount.setText(new DecimalFormat("#0.00").format(finance.getAmount()));
        holder.StatisticsCurrency.setText(finance.getCurrency());
        holder.StatisticsChoice.setTag(position);
        holder.StatisticsChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(v);
            }
        });
        return convertView;
    }


    static class ViewHolder {
        ImageButton StatisticsChoice;
        TextView StatisticsDate;
        TextView StatisticsComment;
        TextView StatisticsAmount;
        TextView StatisticsCurrency;
    }

    private void showPopupMenu(View v) {
        PopupMenu popupMenu = new PopupMenu(context, v);
        popupMenu.inflate(R.menu.popupmenu);
        final int position = (int) v.getTag();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Finance finance = finances.get(position);
                        switch (item.getItemId()) {
                            case R.id.edit:
                                Intent intent = new Intent(context, EnterDataActivity.class);
                                intent.putExtra("title", context.getResources().getString(R.string.title_activity_edit));
                                intent.putExtra("number", finance.getType() + 1);
                                intent.putExtra("finance_id", finance.getId());
                                context.startActivity(intent);
                                return true;
                            case R.id.delete:
                                finances.remove(position);
                                finance.delFinance();
                                FinanceActivity finance_activity = (FinanceActivity) context;
                                finance_activity.fillingGridView();
                                return true;
                            default:
                                return false;
                        }
                    }
                });

        popupMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {
            }
        });
        popupMenu.show();
    }
}