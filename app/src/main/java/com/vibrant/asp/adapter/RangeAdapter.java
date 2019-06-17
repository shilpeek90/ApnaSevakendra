package com.vibrant.asp.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.vibrant.asp.R;
import com.vibrant.asp.model.RangeModel;
import java.util.List;

public class RangeAdapter extends BaseAdapter {
    Context mContext;
    List<RangeModel> rangeModelList;

    public RangeAdapter(Context mContext, List<RangeModel> rangeModels) {
        this.mContext = mContext;
        this.rangeModelList = rangeModels;
    }

    @Override
    public int getCount() {
        return rangeModelList.size();
    }

    @Override
    public RangeModel getItem(int position) {
        return rangeModelList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_spinner_items_range, null);;
        TextView textView =view.findViewById(R.id.tvSubName);
        textView.setText(String.valueOf(rangeModelList.get(position).getRange())+" "+"km");
        return view;
    }
}
