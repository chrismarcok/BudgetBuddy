package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class TagsActivity extends AppCompatActivity {
    ListView tagsListView;
    String[] colors;
    String[] titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        getSupportActionBar().setTitle("Your Tags");

        tagsListView = (ListView) findViewById(R.id.tagsListView);

        int numEntries = HomeActivity.tags.size();

        colors = new String[numEntries];
        titles = new String[numEntries];
        ArrayList<Tag> tags = HomeActivity.tags;
        Collections.reverse(tags);
        for (int i = 0; i < numEntries; i++){
            Tag t = tags.get(i);
            colors[i] = t.getCol();
            titles[i] = t.getText();
        }

        TagAdapter tagAdapter = new TagAdapter(this, colors, titles);
        tagsListView.setAdapter(tagAdapter);

        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showDetailActivity = new Intent(getApplicationContext(), TagDetailActivity.class);
                showDetailActivity.putExtra("com.example.chris.mysqliteproject.ITEM_INDEX", i);
                startActivity(showDetailActivity);
            }
        });
    }
}
