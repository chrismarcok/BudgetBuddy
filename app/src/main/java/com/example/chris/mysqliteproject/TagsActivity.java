package com.example.chris.mysqliteproject;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class TagsActivity extends AppCompatActivity {
    ListView tagsListView;
    String[] colors;
    String[] titles;
    Toolbar toolbar;
    Dialog tagDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        getSupportActionBar().setTitle("Your Tags");

        tagsListView = (ListView) findViewById(R.id.tagsListView);
        tagDialog = new Dialog(this);

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
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    public void showTagInfo(View v){
        TextView txtclose;
        Button submitButton;
        final EditText titleEditText;
        final EditText colourEditText;


        tagDialog.setContentView(R.layout.tag_popup);
        txtclose = (TextView) tagDialog.findViewById(R.id.txtclose);
        submitButton = (Button) tagDialog.findViewById(R.id.submitButton);
        titleEditText = (EditText) tagDialog.findViewById(R.id.tagPopupTitleEditText);
        colourEditText = (EditText) tagDialog.findViewById(R.id.tagPopupColourEditText);

        final TagDBHandler tagDBHandler = new TagDBHandler(this, null, null, 1);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title =  titleEditText.getText().toString();
                String colour = colourEditText.getText().toString();
                boolean isInTagList = false;
                for (int i = 0; i < HomeActivity.tags.size(); i++){
                    if (title.toLowerCase().equals(HomeActivity.tags.get(i).getText().toLowerCase())){
                        isInTagList = true;
                    }
                }
                if (isInTagList){
                    Toast.makeText(getApplicationContext(), "Cannot create tag with duplicate name!", Toast.LENGTH_SHORT).show();
                }
                else if (title.equals("") || !validColour(colour)){
                    Toast.makeText(getApplicationContext(), "One or more fields is invalid!", Toast.LENGTH_SHORT).show();
                }

                else{
                    colour = formatHexCode(colour);
                    Tag tag = new Tag(colour, title);
                    tagDBHandler.addEntry(tag);
                    Toast.makeText(getApplicationContext(), "Tag added", Toast.LENGTH_SHORT).show();
                    tagDialog.dismiss();
                    Intent returnHome = new Intent(TagsActivity.this, HomeActivity.class);
                    startActivity(returnHome);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagDialog.dismiss();
            }
        });
        tagDialog.show();
    }

    public boolean validColour(String col){
        if (col.length() == 6 || col.length() == 3){
            for (int i = 0; i < col.length(); i++){
                if ("abcdefABCDEF0123456789".indexOf(col.charAt(i)) == -1){
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public String formatHexCode(String hex){
        if (hex.length() == 6){
            return hex;
        } else{
            String result = "";

            for (int i = 0; i < hex.length(); i++){
                result += hex.charAt(i);
                result += hex.charAt(i);
            }
            return result;
        }
    }
}
