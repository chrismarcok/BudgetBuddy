package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class TagDetailActivity extends AppCompatActivity {

    Button deleteButton;
    Button updateButton;
    EditText titleEditText;
    EditText colorEditText;
    TextView defaultTextView;
    public final int DEFAULT_TAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);
        final TagDBHandler tagDBHandler = new TagDBHandler(this, null, null, 1);
        deleteButton = (Button) findViewById(R.id.deleteTagDetailButton);
        updateButton = (Button) findViewById(R.id.updateTagDetailButton);
        titleEditText = (EditText) findViewById(R.id.titleEditTextTagDetailActivity);
        colorEditText = (EditText) findViewById(R.id.colorEditTextTagDetailActivity);
        defaultTextView = (TextView) findViewById(R.id.defaultTagTextView);

        Intent in = getIntent();
        final int index = in.getIntExtra("com.example.chris.mysqliteproject.ITEM_INDEX", -1);
        final Tag thisTag = HomeActivity.tags.get(index);

        getSupportActionBar().setTitle(thisTag.getText());
        titleEditText.setText(thisTag.getText());
        colorEditText.setText(thisTag.getCol());

        if (thisTag.getId() == DEFAULT_TAG){
            defaultTextView.setVisibility(View.VISIBLE);
        }
        else{
            defaultTextView.setVisibility(View.GONE);
        }

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = titleEditText.getText().toString();
                String color = colorEditText.getText().toString();
                boolean isValidColor = false;
                isValidColor = TagsActivity.validColour(color);

                if (title.equals("") || color.equals("") || !isValidColor){
                    Toast.makeText(getApplicationContext(), "One or more fields is invalid!", Toast.LENGTH_SHORT).show();
                }
                else{
                    color = TagsActivity.formatHexCode(color);
                    Tag t = new Tag(thisTag.getId(), title, color);
                    tagDBHandler.updateEntry(t);
                    tagDBHandler.fetchDatabaseEntries();
                    Toast.makeText(getApplicationContext(), "Tag Updated", Toast.LENGTH_SHORT).show();
                    Intent returnHome = new Intent(TagDetailActivity.this, HomeActivity.class);
                    startActivity(returnHome);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (thisTag.getId() == DEFAULT_TAG){
                    Toast.makeText(getApplicationContext(), "You may not delete the default tag.", Toast.LENGTH_SHORT).show();
                }
                else {
                    tagDBHandler.deleteEntry(thisTag.getId());
                    tagDBHandler.fetchDatabaseEntries();
                    Toast.makeText(getApplicationContext(), "Tag Deleted", Toast.LENGTH_SHORT).show();
                    Intent returnHome = new Intent(TagDetailActivity.this, HomeActivity.class);
                    startActivity(returnHome);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
