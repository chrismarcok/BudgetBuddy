package com.something.chris.mysqliteproject;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.takusemba.spotlight.OnSpotlightStateChangedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.Spotlight;
import com.takusemba.spotlight.shape.Circle;
import com.takusemba.spotlight.target.CustomTarget;
import com.takusemba.spotlight.target.SimpleTarget;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    /*
    TODO:
    Format Logs Update Activity
    Format Tags Update Activity

     */

    Dialog myDialog;
    Dialog infoDialog;

    CardView newEntryCardView;
    CardView analysisCardView;
    CardView logsCardView;
    CardView budgetLeftCardView;
    CardView tagsCardView;
    CardView settingsCardView;
    CardView aboutCardView;
    MyDBHandler dbHandler;
    TagDBHandler tagDBHandler;

    TextView budgetNumTextView;
    TextView underOverTextView;
    TextView resetTimeLeftEditText;
    SwipeRefreshLayout swipe;

    Long millisecondsLeft;


    public static ArrayList<Entry> entries = new ArrayList<>();
    public static ArrayList<Tag> tags = new ArrayList<>();
    public static User thisUser = new User();
    public static Date now;
    public static float amountSpent = 0;
    public static float totalAmountSaved = 0.0f;
    public static int totalDays = 0;
    public static int daysUnderBudget = 0;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String FIRST_TIME = "firstTime";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        tagDBHandler = new TagDBHandler(this, null, null, 1);
        tagDBHandler.fetchDatabaseEntries();
        dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.fetchDatabaseEntries();
        final SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();


        myDialog = new Dialog(this);

        refresh();


        newEntryCardView = (CardView) findViewById(R.id.newEntryCardView);
        analysisCardView = (CardView) findViewById(R.id.analysisCardView);
        logsCardView =(CardView) findViewById(R.id.logsCardView);
        budgetLeftCardView = (CardView) findViewById(R.id.budgetLeftCardView);
        tagsCardView =(CardView) findViewById(R.id.tagsCardView);
        settingsCardView = (CardView) findViewById(R.id.settingsCardView);
        aboutCardView =(CardView) findViewById(R.id.aboutCardView);

        swipe = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipe.setRefreshing(false);

            }
        });

        budgetLeftCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, BudgetLeftActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });


        logsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, LogsActivity.class);
                if (entries.size() > 0) {
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Add some entries first!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tagsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, TagsActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        analysisCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, AnalysisActivity.class);
                if (entries.size() > 0){
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                } else {
                    Toast.makeText(getApplicationContext(), "Add some entries first!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        settingsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, FirstTimeInfoActivity.class);
                startIntent.putExtra("com.something.chris.mysqliteproject.INFO", "a");
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        aboutCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, SecondActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        boolean firstTime = sharedPreferences.getBoolean(FIRST_TIME, true);

        if (firstTime) {
            swipe.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    swipe.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    SimpleTarget tagTarget = new SimpleTarget.Builder(HomeActivity.this).setPoint(findViewById(R.id.tagsImageView))
                            .setShape(new Circle(122f))
                            .setTitle("What do you like to buy?")
                            .setDescription("Start off by creating some tags for your entries!")
                            .build();
                    SimpleTarget newEntryTarget = new SimpleTarget.Builder(HomeActivity.this).setPoint(findViewById(R.id.newEntryImageView))
                            .setShape(new Circle(122f))
                            .setTitle("Start making purchases!")
                            .setDescription("Use the tags you created to log your purchases!")
                            .build();
                    SimpleTarget logsTarget = new SimpleTarget.Builder(HomeActivity.this).setPoint(findViewById(R.id.logsImageView))
                            .setShape(new Circle(122f))
                            .setTitle("Review your purchases here!")
                            .setDescription("Get out there and start saving!")
                            .build();
                    Spotlight spotlight = Spotlight.with(HomeActivity.this)
                            .setOverlayColor(R.color.background)
                            .setDuration(1000L)
                            .setAnimation(new DecelerateInterpolator(1f))
                            .setTargets(tagTarget, newEntryTarget, logsTarget)
                            .setClosedOnTouchedOutside(true)
                            .setOnSpotlightStateListener(new OnSpotlightStateChangedListener() {
                                @Override
                                public void onStarted() {
                                }

                                @Override
                                public void onEnded() {
                                }
                            });
                    spotlight.start();
                    editor.putBoolean(FIRST_TIME, false);
                    editor.apply();
                }
            });
        }
    }

    public void showPopup(View v){
        TextView txtclose;
        CardView submitButton;
        final EditText amountEditText;
        final EditText dateEditText;
        final EditText locationEditText;
        final EditText detailsEditText;
        final EditText tagEditText;
        myDialog.setContentView(R.layout.newentrypopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        submitButton = (CardView) myDialog.findViewById(R.id.submitButton);
        amountEditText = (EditText) myDialog.findViewById(R.id.amountEditText);
        dateEditText = (EditText) myDialog.findViewById(R.id.dateEditText);
        locationEditText = (EditText) myDialog.findViewById(R.id.locationEditText);
        detailsEditText = (EditText) myDialog.findViewById(R.id.detailsEditText);
        tagEditText = (EditText) myDialog.findViewById(R.id.tagEditText);
        final MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        infoDialog = new Dialog(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tagTitle = tagEditText.getText().toString();
                Tag thisTag = tags.get(0); // "No Tag" tag
                boolean validTag = false;
                for (int i = 0; i < tags.size(); i++){
                    if (tags.get(i).getText().toLowerCase().equals(tagTitle.toLowerCase())){
                        validTag = true;
                        thisTag = tags.get(i);
                        break;
                    }
                }

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
                    try {
                        inputDate = MyDBHandler.DATE_FORMAT.parse(dateEditText.getText().toString());
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
                } else if (!tagTitle.equals("")  && !validTag){
                    Toast.makeText(getApplicationContext(), "No tag exists with that title!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Float amountFloat = Float.parseFloat(amountString);
                    amountFloat = ((int)(amountFloat*100 + 0.5))/100.0f;

                    Entry newEntry = new Entry(amountFloat, inputDate, thisTag);
                    dbHandler.addEntry(newEntry);
                    Toast.makeText(getApplicationContext(), "Entry added", Toast.LENGTH_SHORT).show();
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
        now = new Date();
        loadThisUser();
        tagDBHandler.fetchDatabaseEntries();
        dbHandler.fetchDatabaseEntries();
        millisecondsLeft = thisUser.getNextBudgetStartDate().getTime() - now.getTime();
        if (millisecondsLeft < 0){
            createNewBudget();
        }
        budgetNumTextView = (TextView) findViewById(R.id.budgetNumTextView);
        underOverTextView = (TextView) findViewById(R.id.underOverTextView);
        resetTimeLeftEditText = (TextView) findViewById(R.id.resetTimeLeftEditText);

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

        millisecondsLeft = thisUser.getNextBudgetStartDate().getTime() - now.getTime();

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
            Entry e = entries.get(i);
            if (e.get_date().after(thisUser.getCurrentBudgetStartDate()) && e.get_date().before(thisUser.getNextBudgetStartDate())) {
                x += e.get_value();
            }
        }
        return x;
    }

    private void createNewBudget(){

        Date nextBudgetDate = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(nextBudgetDate);
        String resetTimePeriod = thisUser.getTimePeriod();

        if (resetTimePeriod.equals("24 Hours")){
            cal.add(Calendar.DATE, 1);
        } else if (resetTimePeriod.equals("15 Seconds")) {
            cal.add(Calendar.SECOND, 15);
        } else if (resetTimePeriod.equals("3 Days")){
            cal.add(Calendar.DATE, 3);
        } else if (resetTimePeriod.equals("1 Week")){
            cal.add(Calendar.DATE, 7);
        } else if (resetTimePeriod.equals("2 Weeks")){
            cal.add(Calendar.DATE, 14);
        } else if (resetTimePeriod.equals("1 Month")){
            cal.add(Calendar.MONTH, 1);
        } else if (resetTimePeriod.equals("3 Months")){
            cal.add(Calendar.MONTH, 3);
        } else {
            cal.add(Calendar.YEAR, 1);
        }
        nextBudgetDate = cal.getTime();
        String message = "" + thisUser.getFirstName() + "," +
                thisUser.getLastName() + "," +
                thisUser.isSaveMoney() + "," +
                thisUser.getTimePeriod() + "," +
                thisUser.getBudget() + "," +
                thisUser.getAppSetupDate() + "," +
                now + "," +
                nextBudgetDate;

        thisUser.setCurrentBudgetStartDate(now);
        thisUser.setNextBudgetStartDate(nextBudgetDate);

        String file_name = "user_info";
        try {
            FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
            fileOutputStream.write(message.getBytes());
            fileOutputStream.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
