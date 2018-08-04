package com.example.chris.mysqliteproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    /*TODO
    Delete Entry
    Update Entry
     */

    TextView detailActivityTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailActivityTextView = (TextView) findViewById(R.id.detailActivityTextView);
        Intent in = getIntent();
        int index = in.getIntExtra("com.example.chris.mysqliteproject.ITEM_INDEX", -1);
        Entry thisEntry = HomeActivity.entries.get(index);


        detailActivityTextView.setText(String.valueOf(index));
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
