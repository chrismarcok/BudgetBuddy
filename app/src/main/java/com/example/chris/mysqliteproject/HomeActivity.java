package com.example.chris.mysqliteproject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    Dialog myDialog;
    Dialog infoDialog;

    CardView cardOneCardView;
    CardView cardTwoCardView;
    CardView cardThreeCardView;
    CardView cardFourCardView;
    CardView cardFiveCardView;
    CardView cardSixCardView;
    MyDBHandler dbHandler;

    TextView budgetNumTextView;
    TextView underOverTextView;
    TextView resetTimeLeftEditText;
    SwipeRefreshLayout swipe;



    float amountSpent = 0;
    public static ArrayList<Entry> entries = new ArrayList<>();
    public static User thisUser = new User();
    public static ArrayList<Tag> tags = new ArrayList<>();
    public static Date now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();


        dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.fetchDatabaseEntries();

        myDialog = new Dialog(this);

        refresh();


        cardOneCardView = (CardView) findViewById(R.id.cardOneCardView);
        cardTwoCardView = (CardView) findViewById(R.id.cardTwoCardView);
        cardThreeCardView =(CardView) findViewById(R.id.cardThreeCardView);
        cardFourCardView = (CardView) findViewById(R.id.cardFourCardView);
        cardFiveCardView = (CardView) findViewById(R.id.cardFiveCardView);
        cardSixCardView =(CardView) findViewById(R.id.cardSixCardView);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipe.setRefreshing(false);

            }
        });

        cardTwoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        cardThreeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, LogsActivity.class);
                if (entries.size() > 0) {
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No logs to show!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cardFourCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, RecordsActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        cardFiveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, FirstTimeInfoActivity.class);
                startIntent.putExtra("com.example.chris.mysqliteproject.INFO", "a");
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        cardSixCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, SecondActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    public void showPopup(View v){
        TextView txtclose;
        Button submitButton;
        final EditText amountEditText;
        final EditText dateEditText;
        final EditText locationEditText;
        final EditText detailsEditText;

        myDialog.setContentView(R.layout.newentrypopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        submitButton = (Button) myDialog.findViewById(R.id.submitButton);
        amountEditText = (EditText) myDialog.findViewById(R.id.amountEditText);
        dateEditText = (EditText) myDialog.findViewById(R.id.dateEditText);
        locationEditText = (EditText) myDialog.findViewById(R.id.locationEditText);
        detailsEditText = (EditText) myDialog.findViewById(R.id.detailsEditText);
        final MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        infoDialog = new Dialog(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amountString =  amountEditText.getText().toString();
                Boolean canParse = false;
                Date inputDate = new Date();
                if (!dateEditText.getText().toString().equals("")){
                    try {
                        inputDate = MyDBHandler.DATE_FORMAT_NO_TIME.parse(dateEditText.getText().toString());
                        canParse = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputDate = MyDBHandler.DATE_FORMAT_NO_TIME_SPACES.parse(dateEditText.getText().toString());
                        canParse = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    try {
                        inputDate = MyDBHandler.DATE_FORMAT_NO_TIME_SLASHES.parse(dateEditText.getText().toString());
                        canParse = true;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

                if (!dateEditText.getText().toString().equals("") && !canParse){
                    Toast.makeText(getApplicationContext(), "Could not parse date!", Toast.LENGTH_SHORT).show();
                }
                else if (amountString.equals("")){
                    Toast.makeText(getApplicationContext(), "Please enter an amount!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Float amountFloat = Float.parseFloat(amountString);
                    amountFloat = ((int)(amountFloat*100 + 0.5))/100.0f;

                    Entry newEntry = new Entry(amountFloat, inputDate, locationEditText.getText().toString(), detailsEditText.getText().toString());
                    dbHandler.addEntry(newEntry);
                    Toast.makeText(getApplicationContext(), "Entry added", Toast.LENGTH_SHORT).show();
                    dbHandler.fetchDatabaseEntries();
                    refresh();
                    myDialog.dismiss();
                }
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        now = new Date();
        refresh();
    }

    public void showInfo(View v){
        infoDialog.setContentView(R.layout.infopopup);
        TextView infoTxtClose;

        infoTxtClose = (TextView) infoDialog.findViewById(R.id.infoTxtClose);

        infoTxtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });
        infoDialog.show();
    }

    public void loadThisUser() {
        try {
            String message = "";
            FileInputStream fileInputStream = openFileInput("user_info");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((message = bufferedReader.readLine()) != null) {
                stringBuffer.append(message);
            }
            String result = stringBuffer.toString();
            String strArr[] = result.split(",");

            Date appCreatedDate = new Date();
            Date budgetStartDate = new Date();
            Date nextBudgetDate = new Date();
            try {
                appCreatedDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(strArr[5]);
                budgetStartDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(strArr[6]);
                nextBudgetDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(strArr[7]);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            thisUser.setFirstName(strArr[0]);
            thisUser.setLastName(strArr[1]);
            thisUser.setSaveMoney(Boolean.parseBoolean(strArr[2]));
            thisUser.setTimePeriod(strArr[3]);
            thisUser.setBudget(Float.parseFloat(strArr[4]));
            thisUser.setAppSetupDate(appCreatedDate);
            thisUser.setCurrentBudgetStartDate(budgetStartDate);
            thisUser.setNextBudgetStartDate(nextBudgetDate);

        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void refresh(){
        budgetNumTextView = (TextView) findViewById(R.id.budgetNumTextView);
        underOverTextView = (TextView) findViewById(R.id.underOverTextView);
        resetTimeLeftEditText = (TextView) findViewById(R.id.resetTimeLeftEditText);
        dbHandler.fetchDatabaseEntries();
        loadThisUser();
        amountSpent = getAmountSpent();
        float num = thisUser.getBudget() - amountSpent;

        if (num >= 1000){
            budgetNumTextView.setText("$" + String.format("%.0f", num));
        }
        else if (num <= -1000){
            budgetNumTextView.setText("-$" + String.format("%.0f", -num));
        }
        else if (num > 0){
            budgetNumTextView.setText("$" + String.format("%.2f", num));
        }
        else{
            budgetNumTextView.setText("-$" + String.format("%.2f", -num));
        }

        String underOver = "Under Budget";
        if (num < 0){
            underOver = "Over Budget";
            budgetNumTextView.setTextColor(getResources().getColor(R.color.deficit));
        }
        else{
            budgetNumTextView.setTextColor(getResources().getColor(R.color.green));
        }
        underOverTextView.setText(underOver);

        Long millisecondsLeft = thisUser.getNextBudgetStartDate().getTime() - new Date().getTime();
        String value = "";
        if (millisecondsLeft < 60000){ //if there is less than a minute left, show seconds
            value = String.valueOf(millisecondsLeft/1000) + " sec(s)";
        } else if (millisecondsLeft < 3600000){ // If there is less than an hour left, show minutes
            value = String.valueOf(millisecondsLeft/1000/60) + " min(s)";
        } else if (millisecondsLeft < 86400000){ //if there is less than a day left, show hours
            value = String.valueOf(millisecondsLeft/1000/60/60) + " hr(s)";
        } else if (millisecondsLeft < 604800000){ //if there is less than a week left, show days
            value = String.valueOf(millisecondsLeft/1000/60/60/24) + " day(s)";
        } else{ // else show weeks
            value = String.valueOf(millisecondsLeft/1000/60/60/24/7) + " week(s)";
        }

        resetTimeLeftEditText.setText("Resets in "+ value);
    }

    private float getAmountSpent(){
        float x = 0;
        for (int i = 0; i < entries.size(); i++){
            x += entries.get(i).get_value();
        }
        return x;
    }

    /*
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    */
}
