package com.example.chris.mysqliteproject;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ItemAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    String[] dates;
    String[] amounts;
    Boolean[] news;
    Tag[] tags;


    public ItemAdapter(Context c, String[] i, String[] p, Boolean[] b, Tag[] t){
        dates = i;
        amounts = p;
        news = b;
        tags = t;
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
        TextView newTextView = (TextView) v.findViewById(R.id.newTextView);
        TextView tagTextView = (TextView) v.findViewById(R.id.logTagTextView);


        String name = dates[i];
        String cost = amounts[i];
        Boolean newBool = news[i];
        Tag t = tags[i];

        int hex = Integer.parseInt(t.getCol(), 16);
        int r = (hex & 0xFF0000) >> 16;
        int g = (hex & 0xFF00) >> 8;
        int b = (hex & 0xFF);
        double lumin = (0.2126*r + 0.7152*g + 0.0722*b);

        if(newBool){
            newTextView.setVisibility(View.VISIBLE);
        }
        else{
            newTextView.setVisibility(View.INVISIBLE);
        }

        mainTextView.setText(name);
        amountTextView.setText(cost);
        tagTextView.setText(t.getText());
        tagTextView.setBackgroundColor(Color.parseColor("#"+t.getCol()));

        if (lumin > 255/2.0){
            tagTextView.setTextColor(v.getResources().getColor(R.color.black));
        } else{
            tagTextView.setTextColor(v.getResources().getColor(R.color.white));
        }

        return v;
    }

}
