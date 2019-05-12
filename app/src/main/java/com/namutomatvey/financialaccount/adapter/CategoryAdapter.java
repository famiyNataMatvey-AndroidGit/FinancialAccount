package com.namutomatvey.financialaccount.adapter;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.namutomatvey.financialaccount.dto.Category;

import java.util.List;

public class CategoryAdapter extends BaseAdapter {

    private Context context;
    private List<Category> categories;

    public CategoryAdapter(Context context, List<Category> categories) {
        this.context = context;
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;

        if (convertView == null) {
            button = new Button(context);
            button.setText(categories.get(position).getName());
        } else {
            button = (Button) convertView;
        }
        button.setId(position);

        return button;
    }
}