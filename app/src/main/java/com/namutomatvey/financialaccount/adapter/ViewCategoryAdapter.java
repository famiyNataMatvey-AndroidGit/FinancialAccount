package com.namutomatvey.financialaccount.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.dto.ViewCategory;

import java.util.List;

public class ViewCategoryAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<ViewCategory> viewCategories;

    public ViewCategoryAdapter(Context context, List<ViewCategory> viewCategories) {
        this.context = context;
        this.viewCategories = viewCategories;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return viewCategories.size();
    }

    @Override
    public Object getItem(int position) {
        return viewCategories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_category_statistic, null);
            holder = new ViewHolder();
            holder.categoryNameView = convertView.findViewById(R.id.financeItemCategoryName);
            holder.amountView = convertView.findViewById(R.id.financeItemAmount);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ViewCategory viewCategorie = this.viewCategories.get(position);
        holder.categoryNameView.setText(viewCategorie.getCategory());
        holder.amountView.setText(Double.toString(viewCategorie.getAmount()));
        convertView.setBackgroundResource(R.drawable.border_box);
        return convertView;
    }

    static class ViewHolder {
        TextView categoryNameView;
        TextView amountView;
    }
}
