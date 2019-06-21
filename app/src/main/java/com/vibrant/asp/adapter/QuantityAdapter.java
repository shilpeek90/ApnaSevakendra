package com.vibrant.asp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibrant.asp.R;
import com.vibrant.asp.model.QuantityModel;
import com.vibrant.asp.model.RangeModel;

import java.util.List;

public class QuantityAdapter extends BaseAdapter {
    Context mContext;
    List<QuantityModel> quantityArray;

    public QuantityAdapter(Context mContext, List<QuantityModel> quantityArray) {
        this.mContext = mContext;
        this.quantityArray = quantityArray;
    }

    @Override
    public int getCount() {
        return quantityArray.size();
    }

    @Override
    public QuantityModel getItem(int position) {
        return quantityArray.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.spinner_items_quantity, null);;
        TextView tvQuantity =view.findViewById(R.id.tvQuantity);
        tvQuantity.setText(String.valueOf(quantityArray.get(position).getQuantity()));
        return view;
    }
}
