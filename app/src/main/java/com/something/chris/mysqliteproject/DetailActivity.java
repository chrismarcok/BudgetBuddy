package com.something.chris.mysqliteproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ncorti.slidetoact.SlideToActView;

import java.text.ParseException;
import java.util.Date;

public class DetailActivity extends AppCompatActivity {

    TextView detailActivityTextView;
    CardView updateButton;
    EditText amountEditTextDetailActivity;
    EditText dateEditTextDetailActivity;
    EditText tagEditTextDetailActivity;
    Button deleteButton;
    SlideToActView deleteSlider;
    Spinner repeatSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().hide();
        String title;
        final MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);
        updateButton = (CardView) findViewById(R.id.updateButton);
        amountEditTextDetailActivity = (EditText) findViewById(R.id.amountEditTextDetailActivity);
        dateEditTextDetailActivity = (EditText) findViewById(R.id.dateEditTextDetailActivity);
        tagEditTextDetailActivity = (EditText) findViewById(R.id.tagEditTextDetailActivity);
        deleteButton = (Button) findViewById(R.id.deleteButton);
        deleteSlider = (SlideToActView) findViewById(R.id.deleteSlider);
        repeatSpinner = (Spinner) findViewById(R.id.repeatSpinnerDetailActivity);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.repeat_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        repeatSpinner.setAdapter(adapter);

        Intent in = getIntent();
        int index = in.getIntExtra("com.something.chris.mysqliteproject.ITEM_INDEX", -1);
        final Entry thisEntry = HomeActivity.entries.get(index);



        title = thisEntry.get_date().toString();
        getSupportActionBar().setTitle(title);

        amountEditTextDetailActivity.setText(String.valueOf(thisEntry.get_value()));
        dateEditTextDetailActivity.setText(MyDBHandler.DATE_FORMAT.format(thisEntry.get_date()));
        for(int i= 0; i < repeatSpinner.getAdapter().getCount(); i++)
        {
            if(repeatSpinner.getAdapter().getItem(i).toString().contains(thisEntry.get_repeat()))
            {
                repeatSpinner.setSelection(i);
                break;
            }
        }

        deleteSlider.setOnSlideCompleteListener(new SlideToActView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(@NonNull SlideToActView view) {
                dbHandler.deleteEntry(thisEntry.get_id());
                dbHandler.fetchDatabaseEntries();
                Intent returnHome = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(returnHome);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                DetailActivity.this.finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHandler.deleteEntry(thisEntry.get_id());
                dbHandler.fetchDatabaseEntries();
                Toast.makeText(getApplicationContext(), "Entry Deleted", Toast.LENGTH_SHORT).show();
                Intent returnHome = new Intent(DetailActivity.this, HomeActivity.class);
                startActivity(returnHome);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                DetailActivity.this.finish();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amountString =  amountEditTextDetailActivity.getText().toString();
                Boolean canParse = false;
                Date inputDate = new Date();
                Tag t = thisEntry.get_tag();
                if (!dateEditTextDetailActivity.getText().toString().equals("")){
                    try {
                        inputDate = MyDBHandler.DATE_FORMAT_NO_TIME.parse(dateEditTextDetailActivity.getText().toString());
                        canParse = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputDate = MyDBHandler.DATE_FORMAT_NO_TIME_SPACES.parse(dateEditTextDetailActivity.getText().toString());
                        canParse = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputDate = MyDBHandler.DATE_FORMAT_NO_TIME_SLASHES.parse(dateEditTextDetailActivity.getText().toString());
                        canParse = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputDate = MyDBHandler.DATE_FORMAT.parse(dateEditTextDetailActivity.getText().toString());
                        canParse = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (!t.getText().equals(tagEditTextDetailActivity.getText().toString())){ //if the inputted tag is different
                    boolean validTag = false;
                    String tagTitle = tagEditTextDetailActivity.getText().toString();
                    for (int i = 0; i < HomeActivity.tags.size(); i++){
                        if (HomeActivity.tags.get(i).getText().toLowerCase().equals(tagTitle.toLowerCase())){
                            validTag = true;
                            t = HomeActivity.tags.get(i);
                            break;
                        } else if (tagEditTextDetailActivity.getText().toString().equals("")){
                            validTag = true;
                            t = HomeActivity.tags.get(0);
                            break;
                        }
                    }
                    if (!validTag){
                        Toast.makeText(getApplicationContext(), "Invalid Tag!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                if (!dateEditTextDetailActivity.getText().toString().equals("") && !canParse){
                    Toast.makeText(getApplicationContext(), "Could not parse date!", Toast.LENGTH_SHORT).show();
                }
                else if (amountString.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Float amountFloat = Float.parseFloat(amountString);
                    amountFloat = ((int)(amountFloat*100 + 0.5))/100.0f;
                    Entry e = new Entry(thisEntry.get_id(), amountFloat, inputDate, t, repeatSpinner.getSelectedItem().toString(), thisEntry.is_repeated());
                    dbHandler.updateEntry(e);
                    dbHandler.fetchDatabaseEntries();
                    Toast.makeText(getApplicationContext(), "Entry Updated", Toast.LENGTH_SHORT).show();

                    Intent returnHome = new Intent(DetailActivity.this, HomeActivity.class);
                    startActivity(returnHome);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        refresh();

    }

    public void refresh(){
        Intent in = getIntent();
        int index = in.getIntExtra("com.something.chris.mysqliteproject.ITEM_INDEX", -1);
        final Entry thisEntry = HomeActivity.entries.get(index);
        amountEditTextDetailActivity.setText(String.valueOf(thisEntry.get_value()));
        dateEditTextDetailActivity.setText(MyDBHandler.DATE_FORMAT.format(thisEntry.get_date()));
        tagEditTextDetailActivity.setText(thisEntry.get_tag().getText());

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
