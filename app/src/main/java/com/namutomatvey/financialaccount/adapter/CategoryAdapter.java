package com.namutomatvey.financialaccount.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.dto.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public Object getItem(int position) {
        return categories.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView;

        if (convertView == null || ((TextView) convertView).getId() != (Integer) position) {
            textView = new TextView(context);
            textView.setText(categories.get(position).getName());
            textView.setTextColor(Color.BLACK);
            textView.setTextSize(18);
            textView.setPadding(35, 10, 15, 10);
            textView.setBackgroundResource(R.drawable.border_box);
        } else {
            textView = (TextView) convertView;
        }
        textView.setId(position);
        return textView;
    }

    public void addingItemAdapter(Category category){
        this.categories.add(category);
    }
}