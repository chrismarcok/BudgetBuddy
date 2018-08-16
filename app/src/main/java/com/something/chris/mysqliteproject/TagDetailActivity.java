package com.something.chris.mysqliteproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ncorti.slidetoact.SlideToActView;

import org.w3c.dom.Text;

import petrov.kristiyan.colorpicker.ColorPicker;
import yuku.ambilwarna.AmbilWarnaDialog;

public class TagDetailActivity extends AppCompatActivity {

    int mDefaultColor;
//    Button deleteButton;
    CardView updateButton;
    CardView colorPickerButton;
    EditText titleEditText;
    EditText colorEditText;
    TextView defaultTextView;
    SlideToActView deleteTagSlider;
    public final int DEFAULT_TAG = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_detail);
        final TagDBHandler tagDBHandler = new TagDBHandler(this, null, null, 1);
//        deleteButton = (Button) findViewById(R.id.deleteTagDetailButton);
        updateButton = (CardView) findViewById(R.id.updateTagDetailButton);
        titleEditText = (EditText) findViewById(R.id.titleEditTextTagDetailActivity);
        colorEditText = (EditText) findViewById(R.id.colorEditTextTagDetailActivity);
        defaultTextView = (TextView) findViewById(R.id.defaultTagTextView);
        colorPickerButton = (CardView) findViewById(R.id.colorPickerButton);
        deleteTagSlider = (SlideToActView) findViewById(R.id.deleteTagSlider);

        Intent in = getIntent();
        final int index = in.getIntExtra("com.something.chris.mysqliteproject.ITEM_INDEX", -1);
        final Tag thisTag = HomeActivity.tags.get(index);

        getSupportActionBar().setTitle(thisTag.getText());
        titleEditText.setText(thisTag.getText());
        colorEditText.setText(thisTag.getCol().toUpperCase());

        colorEditText.setTextColor(Color.parseColor("#"+thisTag.getCol()));

        if (thisTag.getId() == DEFAULT_TAG){
            defaultTextView.setVisibility(View.VISIBLE);
        }
        else{
            defaultTextView.setVisibility(View.GONE);
        }


        colorPickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });
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
                    Tag t = new Tag(thisTag.getId(), color, title);
                    tagDBHandler.updateEntry(t);
                    tagDBHandler.fetchDatabaseEntries();
                    Toast.makeText(getApplicationContext(), "Tag Updated", Toast.LENGTH_SHORT).show();
                    Intent returnHome = new Intent(TagDetailActivity.this, HomeActivity.class);
                    startActivity(returnHome);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            }
        });

        colorEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int col;
                if (TagsActivity.validColour(colorEditText.getText().toString())){
                    String strCol = TagsActivity.formatHexCode(colorEditText.getText().toString());
                    col = Color.parseColor("#" + strCol);
                } else {
                    col = Color.parseColor("#000000");
                }
                colorEditText.setTextColor(col);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        if (thisTag.getId() == DEFAULT_TAG){
            deleteTagSlider.setLocked(true);
        }
        deleteTagSlider.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView view) {
                tagDBHandler.deleteEntry(thisTag.getId());
                tagDBHandler.fetchDatabaseEntries();
                Intent returnHome = new Intent(TagDetailActivity.this, HomeActivity.class);
                startActivity(returnHome);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (thisTag.getId() == DEFAULT_TAG){
//                    Toast.makeText(getApplicationContext(), "You may not delete the default tag.", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    tagDBHandler.deleteEntry(thisTag.getId());
//                    tagDBHandler.fetchDatabaseEntries();
//                    Toast.makeText(getApplicationContext(), "Tag Deleted", Toast.LENGTH_SHORT).show();
//                    Intent returnHome = new Intent(TagDetailActivity.this, HomeActivity.class);
//                    startActivity(returnHome);
//                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                }
//            }
//        });


    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
    public void openColorPicker(){
        ColorPicker colorPicker = new ColorPicker(TagDetailActivity.this);
        colorPicker.setDefaultColorButton(mDefaultColor);
        colorPicker.setTitle("Choose a Tag Color");
        colorPicker.setRoundColorButton(true);
        colorPicker.show();
        colorPicker.setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
            @Override
            public void onChooseColor(int position,int color) {
                if (color  == 0){
                    return;
                } else {
                    mDefaultColor = color;
                    int r = Color.red(mDefaultColor);
                    int g = Color.green(mDefaultColor);
                    int b = Color.blue(mDefaultColor);
                    colorEditText.setText(String.format("%02X%02X%02X", r, g, b));
                    colorEditText.setTextColor(Color.parseColor(String.format("#%02X%02X%02X", r, g, b)));
                }
            }

            @Override
            public void onCancel(){
                // put code
            }
        });
//        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(this, mDefaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
//            @Override
//            public void onCancel(AmbilWarnaDialog dialog) {
//            }
//
//            @Override
//            public void onOk(AmbilWarnaDialog dialog, int color) {
//                mDefaultColor = color;
//                int r = Color.red(mDefaultColor);
//                int g = Color.green(mDefaultColor);
//                int b = Color.blue(mDefaultColor);
//                colorEditText.setText(String.format("%02X%02X%02X",r,g,b));
//                colorEditText.setTextColor(Color.parseColor(String.format("#%02X%02X%02X",r,g,b)));
//            }
//        });
//        colorPicker.show();
    }
}
