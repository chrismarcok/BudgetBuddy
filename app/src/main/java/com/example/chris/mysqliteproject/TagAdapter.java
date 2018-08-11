package com.example.chris.mysqliteproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TagAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    String[] colors;
    String[] titles;
    Boolean[] defaults;

    public TagAdapter(Context c, String[] cols, String[] tits, Boolean[] defs){
        colors = cols;
        titles = tits; //lol
        defaults = defs;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return colors.length;
    }

    @Override
    public Object getItem(int i) {
        return colors[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View v = mInflater.inflate(R.layout.tag_listview_detail, null);
        TextView titleTextView = (TextView) v.findViewById(R.id.titleTagTextView);
        TextView colorTextView = (TextView) v.findViewById(R.id.colorTagTextView);
        TextView defaultTextView = (TextView) v.findViewById(R.id.defaultBooleanTextView);

        if (defaults[i]){
            defaultTextView.setVisibility(View.VISIBLE);
        } else {
            defaultTextView.setVisibility(View.GONE);
        }

        String thisColor = "#" + colors[i].toUpperCase();
        String thisTitle = titles[i];

        titleTextView.setText(thisTitle);
        colorTextView.setText(thisColor);
        colorTextView.setTextColor(Color.parseColor(thisColor));

        return v;
    }
}
