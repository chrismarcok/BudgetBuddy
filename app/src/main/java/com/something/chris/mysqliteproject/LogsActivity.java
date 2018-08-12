package com.something.chris.mysqliteproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class LogsActivity extends AppCompatActivity {

    /*TODO
    Show tag on log
    Edit and delete tags
    Show tag on tag listview
    can change tag when editing an entry
     */


    ListView logsListView;
    String[] items;
    String[] amounts;
    Tag[] tags;
    Boolean[] newBool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logs);

        getSupportActionBar().setTitle("Your Budget Buddy Logs");

        logsListView = (ListView) findViewById(R.id.logsListView);

        //Populate Arrays
        int numEntries = HomeActivity.entries.size();

        items = new String[numEntries];
        amounts = new String[numEntries];
        newBool = new Boolean[numEntries];
        tags = new Tag[numEntries];
        ArrayList<Entry> entries = HomeActivity.entries;
        Collections.reverse(entries);
        for (int i = 0; i < numEntries; i++){
            Entry e = entries.get(i);
            tags[i] = e.get_tag();
            items[i] = MyDBHandler.DATE_FORMAT_LOGS.format(e.get_date());
            if (e.get_value() < 0){
                amounts[i] = "-$" + String.format("%.2f", -e.get_value());
            }
            else {
                amounts[i] = "$" + String.format("%.2f", e.get_value());
            }
            newBool[i] = (e.get_date().before(new Date()))&&(HomeActivity.now.getTime() - e.get_date().getTime() < 1000 * 60 * 60); //1000ms * 60sec * 60min = 1 Hour
        }

        ItemAdapter itemAdapter = new ItemAdapter(this, items, amounts, newBool, tags);
        logsListView.setAdapter(itemAdapter);

        logsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showDetailActivity = new Intent(getApplicationContext(), DetailActivity.class);
                showDetailActivity.putExtra("com.something.chris.mysqliteproject.ITEM_INDEX", i);
                startActivity(showDetailActivity);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


}
