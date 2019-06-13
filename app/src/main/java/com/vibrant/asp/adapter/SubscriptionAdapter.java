package com.vibrant.asp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vibrant.asp.R;
import com.vibrant.asp.model.SubscriptionModel;

import java.util.List;

public class SubscriptionAdapter extends BaseAdapter {
    Context mContext;
    List<SubscriptionModel> subscriptionModels;

    public SubscriptionAdapter(Context mContext, List<SubscriptionModel> subscriptionModels) {
        this.mContext = mContext;
        this.subscriptionModels = subscriptionModels;
    }

    @Override
    public int getCount() {
        return subscriptionModels.size();
    }

    @Override
    public SubscriptionModel getItem(int position) {
        return subscriptionModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_spinner_items, null);;
        TextView textView =view.findViewById(R.id.tvSubName);
        textView.setText(subscriptionModels.get(position).getSubName());
        return view;
    }
}
