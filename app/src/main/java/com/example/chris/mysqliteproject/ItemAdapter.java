package com.example.chris.mysqliteproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    String[] dates;
    String[] amounts;
    String[] details;
    String[] locations;

    public ItemAdapter(Context c, String[] i, String[] p, String[] d, String[] l){
        dates = i;
        amounts = p;
        details = d;
        locations = l;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dates.length;
    }

    @Override
    public Object getItem(int i) {
        return dates[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate(R.layout.log_listview_detail, null);
        TextView mainTextView = (TextView) v.findViewById(R.id.logMainTextView);
        TextView amountTextView = (TextView) v.findViewById(R.id.logAmountTextView);
        TextView detailsTextView = (TextView) v.findViewById(R.id.logDetailsTextView);
        TextView locationsTextView = (TextView) v.findViewById(R.id.logLocationTextView);

        String name = dates[i];
        String cost = amounts[i];
        String desc = details[i];
        String loc = locations[i];

        mainTextView.setText(name);
        detailsTextView.setText(desc);
        amountTextView.setText(cost);
        locationsTextView.setText(loc);

        return v;
    }
}
