package com.namutomatvey.financialaccount.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.namutomatvey.financialaccount.ConversionData;
import com.namutomatvey.financialaccount.R;
import com.namutomatvey.financialaccount.dto.Finance;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CheckAdapter extends BaseAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private List<Finance> finances;


    public CheckAdapter(Context context, List<Finance> finances) {
        this.context = context;
        this.finances = finances;
        layoutInflater = LayoutInflater.from(context);
    }

    public void setFinances(List<Finance> finances) {
        this.finances = finances;
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
        ViewHolder holder = null;
        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder == null || (Integer) holder.QRChoice.getTag() != position) {
            convertView = layoutInflater.inflate(R.layout.item_check_layout, null);
            holder = new ViewHolder();
            holder.QRChoice = convertView.findViewById(R.id.imageViewQRChoice);
            holder.QRComment = convertView.findViewById(R.id.textViewQRComment);
            holder.QRAmount = convertView.findViewById(R.id.textViewQRAmount);
            holder.QRCurrency = convertView.findViewById(R.id.textViewQRCurrency);
            convertView.setTag(holder);
        }

        final Finance finance = this.getItem(position);
        holder.QRCurrency.setText(finance.getCurrency());
        holder.QRComment.setText(finance.getComment());
        holder.QRAmount.setText(ConversionData.conversionDoubleToString(finance.getAmount()));
        ImageView imageViewTemp = (ImageView) holder.QRChoice;
        if (finance.box) {
            imageViewTemp.setImageResource(R.drawable.ic_check_true);
        } else {
            imageViewTemp.setImageResource(R.drawable.ic_check_false);
        }
        holder.QRChoice.setTag(position);
        holder.QRChoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Finance tempItem = getItem((Integer) v.getTag());
                ImageView imageViewTemp = (ImageView) v;
                if (tempItem.box) {
                    tempItem.box = false;
                    imageViewTemp.setImageResource(R.drawable.ic_check_false);
                } else {
                    tempItem.box = true;
                    imageViewTemp.setImageResource(R.drawable.ic_check_true);
                }
            }
        });
        return convertView;
    }


    static class ViewHolder {
        ImageView QRChoice;
        TextView QRComment;
        TextView QRAmount;
        TextView QRCurrency;
    }

    public ArrayList<Finance> getCheckFinance() {
        ArrayList<Finance> arrayFinances = new ArrayList<Finance>();
        for (Finance finance : finances) {
            if (finance.box)
                arrayFinances.add(finance);
        }
        return arrayFinances;
    }
}