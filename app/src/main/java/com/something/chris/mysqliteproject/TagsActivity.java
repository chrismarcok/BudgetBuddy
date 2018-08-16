package com.something.chris.mysqliteproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
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

import petrov.kristiyan.colorpicker.ColorPicker;
import yuku.ambilwarna.AmbilWarnaDialog;

public class TagsActivity extends AppCompatActivity {
    ListView tagsListView;
    String[] colors;
    String[] titles;
    Boolean[] defaults;
    Toolbar toolbar;
    Dialog tagDialog;
    FloatingActionButton addTagFab;

    EditText colourEditText;

    int mDefaultColor = Color.parseColor("#2062AF");
    CardView mButton;

    String col;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_tags);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Your Tags");



        tagsListView = (ListView) findViewById(R.id.tagsListView);
        tagDialog = new Dialog(this);

        int numEntries = HomeActivity.tags.size();

        colors = new String[numEntries];
        titles = new String[numEntries];
        defaults = new Boolean[numEntries];
        ArrayList<Tag> tags = HomeActivity.tags;
        Collections.reverse(tags);
        for (int i = 0; i < numEntries; i++){
            Tag t = tags.get(i);
            colors[i] = t.getCol();
            titles[i] = t.getText();
            if (t.getId() == 1){
                defaults[i] = true;
            }
            else{
                defaults[i] = false;
            }

        }

        TagAdapter tagAdapter = new TagAdapter(this, colors, titles, defaults);
        tagsListView.setAdapter(tagAdapter);

        tagsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent showDetailActivity = new Intent(getApplicationContext(), TagDetailActivity.class);
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

    public void showTagInfo(View v){
        TextView txtclose;
        CardView submitButton;
        final EditText titleEditText;


        tagDialog.setContentView(R.layout.tag_popup);
        txtclose = (TextView) tagDialog.findViewById(R.id.txtclose);
        submitButton = (CardView) tagDialog.findViewById(R.id.submitButton);
        titleEditText = (EditText) tagDialog.findViewById(R.id.tagPopupTitleEditText);
        colourEditText = (EditText) tagDialog.findViewById(R.id.tagPopupColourEditText);
        mDefaultColor = ContextCompat.getColor(TagsActivity.this, R.color.black);
        mButton = (CardView) tagDialog.findViewById(R.id.colorPickerButton);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        final TagDBHandler tagDBHandler = new TagDBHandler(this, null, null, 1);

        colourEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                int col;
                if (validColour(colourEditText.getText().toString())){
                    String strCol = formatHexCode(colourEditText.getText().toString());
                    col = Color.parseColor("#" + strCol);
                } else {
                    col = Color.parseColor("#000000");
                }
                colourEditText.setTextColor(col);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

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

    public static boolean validColour(String col){
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

    public static String formatHexCode(String hex){
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

    public void openColorPicker(){
        ColorPicker colorPicker = new ColorPicker(TagsActivity.this);
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
                    colourEditText.setText(String.format("%02X%02X%02X", r, g, b));
                    colourEditText.setTextColor(Color.parseColor(String.format("#%02X%02X%02X", r, g, b)));
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
//                colourEditText.setText(String.format("%02X%02X%02X",r,g,b));
//                colourEditText.setTextColor(Color.parseColor(String.format("#%02X%02X%02X",r,g,b)));
//            }
//        });
//        colorPicker.show();
    }
}
