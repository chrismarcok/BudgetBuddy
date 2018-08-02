package com.example.chris.mysqliteproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class LogsActivity extends AppCompatActivity {

    ListView logsListView;
    String[] items;
    String[] amounts;
    String[] details;
    String[] locations;

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
        details = new String[numEntries];
        locations = new String[numEntries];

        for (int i = 0; i < numEntries; i++){
            Entry e = HomeActivity.entries.get(i);
            items[i] = MyDBHandler.DATE_FORMAT.format(e.get_date());
            amounts[i] = "$" + String.format("%.2f", e.get_value());
            if (e.get_location().equals("")){
                locations[i] = "N/A";
            }
            else {
                locations[i] = e.get_location();
            }
            if (e.get_details().equals("")){
                details[i] = "N/A";
            }
            else {
                details[i] = e.get_details();
            }
        }

        ItemAdapter itemAdapter = new ItemAdapter(this, items, amounts, details, locations);
        logsListView.setAdapter(itemAdapter);


        logsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showDetailActivity = new Intent(getApplicationContext(), DetailActivity.class);
                showDetailActivity.putExtra("com.example.chris.mysqliteproject.ITEM_INDEX", i);
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
