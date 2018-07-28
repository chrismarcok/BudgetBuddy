package com.example.chris.mysqliteproject;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SliderAdapter extends PagerAdapter {

    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context){
        this.context = context;
    }

    public int[] slideImages = {
            R.mipmap.ic_whatshot_black_24dp,
            R.mipmap.ic_check_circle_black_24dp,
            R.mipmap.ic_money_off_black_24dp
    };
    public int[] slideBackgrounds = {
            R.drawable.circlebackgroundgreen,
            R.drawable.circlebackgroundyellow,
            R.drawable.circlebackgroundpurple,
    };
    public String[] slideHeadings = {
            "Streaks",
            "Days Under Budget",
            "Total Money Saved"
    };
    public String[] slideDescs = {
            "StreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaksStreaks",
            "Days Under BudgetDays Under BudgetDays Under BudgetDays Under BudgetDays Under BudgetDays Under BudgetDays Under BudgetDays Under BudgetDays Under Budget",
            "Total Money SavedTotal Money SavedTotal Money SavedTotal Money SavedTotal Money SavedTotal Money SavedTotal Money SavedTotal Money Saved"
    };

    @Override
    public int getCount() {
        return slideHeadings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
        return view == (RelativeLayout) o;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = (ImageView) view.findViewById(R.id.slideImageView);
        TextView headingTextView = (TextView) view.findViewById(R.id.headingTextView);
        TextView bodyTextView = (TextView) view.findViewById(R.id.bodyTextView);

        slideImageView.setImageResource(slideImages[position]);
        slideImageView.setBackgroundResource(slideBackgrounds[position]);
        headingTextView.setText(slideHeadings[position]);
        bodyTextView.setText(slideDescs[position]);

        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((RelativeLayout)object);
    }
}
