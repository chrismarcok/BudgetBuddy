package com.something.chris.mysqliteproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class FirstTimeInfoActivity extends AppCompatActivity {

    CardView onwardsButton;
    TextView onwardsButtonTextView;
    Spinner timePeriodSpinner;
    RadioButton saveMoneyRadioButton;
    RadioButton maintainABudgetRadioButton;
    EditText budgetEditText;


    public String firstName;
    public String lastName;
    Context c;
    //firstname(String),secondname(String),saveMoney(Bool),budgetReset(String),budget(Float),appSetupDate(String),currentBudgetStartDate(String),nextBudgetDate(String)
    public static final int FIRST_NAME = 0;
    public static final int LAST_NAME = 1;
    public static final int SAVE_MONEY = 2;
    public static final int BUDGET_RESET = 3;
    public static final int BUDGET = 4;
    public static final int APP_SETUP_DATE = 5;
    public static final int CURRENT_BUDGET_START_DATE = 6;
    public static final int NEXT_BUDGET_DATE = 7;

    public static Tag[] defaultTags = {
                            new Tag("9E9E9E", "Other"),
                            new Tag("9E9E9E", "Income"),
                            new Tag("FFEB3B", "Gift"),
                            new Tag("FFEB3B", "Donation"),
                            new Tag("795548", "Tax"),
                            new Tag("795548", "Shipping"),
                            new Tag("795548", "Printing"),
                            new Tag("795548", "Office Supplies"),
                            new Tag("795548", "Parking"),
                            new Tag("795548", "Gas"),
                            new Tag("4CAF50", "Flight"),
                            new Tag("4CAF50", "Train"),
                            new Tag("4CAF50", "Presto"),
                            new Tag("607D8B", "Uber"),
                            new Tag("009688", "Rent"),
                            new Tag("009688", "Utilities"),
                            new Tag("009688", "Daycare"),
                            new Tag("009688", "Phone"),
                            new Tag("03A9F4", "Textbooks"),
                            new Tag("03A9F4", "Tuition"),
                            new Tag("3F51B5", "Doctor"),
                            new Tag("3F51B5", "Dentist"),
                            new Tag("3F51B5", "Gym"),
                            new Tag("9C27B0", "Hair"),
                            new Tag("9C27B0", "Accessories"),
                            new Tag("9C27B0", "Clothing"),
                            new Tag("E91E63", "Books"),
                            new Tag("E91E63", "Video Games"),
                            new Tag("E91E63", "Movies"),
                            new Tag("E91E63", "Music"),
                            new Tag("E91E63", "Hobbies"),
                            new Tag("F44336", "Groceries"),
                            new Tag("F44336", "Coffee"),
                            new Tag("F44336", "Fast Food"),
                            new Tag("F44336", "Alcohol")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_first_time_info);
        getSupportActionBar().hide();
        getSupportActionBar().setTitle("Create a Budget");

        budgetEditText = (EditText) findViewById(R.id.budgetEditText);
        onwardsButton = (CardView) findViewById(R.id.onwardsButton);
        onwardsButtonTextView = (TextView) findViewById(R.id.onwardsButtonTextView);
        final EditText firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);

        timePeriodSpinner = (Spinner) findViewById(R.id.timePeriodSpinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.times_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        timePeriodSpinner.setAdapter(adapter);

        saveMoneyRadioButton = (RadioButton) findViewById(R.id.saveMoneyRadioButton);
        maintainABudgetRadioButton = (RadioButton) findViewById(R.id.maintainABudgetRadioButton);
        maintainABudgetRadioButton.setChecked(true);
        saveMoneyRadioButton.setEnabled(false);

        c = this;

        if (getIntent().hasExtra("com.something.chris.mysqliteproject.INFO")){
            onwardsButtonTextView.setText("Save Settings");
            getSupportActionBar().setTitle("Settings");

            try{
                String message = "";
                FileInputStream fileInputStream = openFileInput("user_info");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while ((message = bufferedReader.readLine()) != null){
                    stringBuffer.append(message);
                }
                String result = stringBuffer.toString();
                String strArr[] = result.split(",");

                firstNameEditText.setText(strArr[FIRST_NAME]);
                lastNameEditText.setText(strArr[LAST_NAME]);

                if(Boolean.parseBoolean(strArr[SAVE_MONEY])){
                    saveMoneyRadioButton.setChecked(true);
                    maintainABudgetRadioButton.setChecked(false);
                }
                else{
                    saveMoneyRadioButton.setChecked(false);
                    maintainABudgetRadioButton.setChecked(true);
                }
                for(int i= 0; i < timePeriodSpinner.getAdapter().getCount(); i++)
                {
                    if(timePeriodSpinner.getAdapter().getItem(i).toString().contains(strArr[3]))
                    {
                        timePeriodSpinner.setSelection(i);
                        break;
                    }
                }
                budgetEditText.setText(strArr[BUDGET]);

            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        onwardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                firstName = firstNameEditText.getText().toString();
                lastName = lastNameEditText.getText().toString();
                String resetTimePeriod = timePeriodSpinner.getSelectedItem().toString();
                String budget = budgetEditText.getText().toString();
                String m = "";
                if (!firstName.equals("") &&
                        !lastName.equals("") &&
                        (saveMoneyRadioButton.isChecked() || maintainABudgetRadioButton.isChecked()) &&
                        !budget.equals("")){

                         try {
                            FileInputStream fileInputStream = openFileInput("user_info");
                            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                            StringBuffer stringBuffer = new StringBuffer();
                            while ((m = bufferedReader.readLine()) != null){
                                stringBuffer.append(m);
                            }
                            m = stringBuffer.toString();

                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                        if (m.equals("")){
                            TagDBHandler tagDBHandler = new TagDBHandler(c, null, null, 1);
                            for (Tag t : defaultTags){
                                tagDBHandler.addEntry(t);
                            }
                        }

                    //STORE AS:
                    /*
                    user_info

                        firstname(String),secondname(String),saveMoney(Bool),budgetReset(String),budget(Float),appSetupDate(String),currentBudgetStartDate(String),nextBudgetDate(String)

                     */
                    float budgetFloat = Float.parseFloat(budget);

                    if (m.equals("")){ //THIS IS THE FIRST TIME THISUSER IS BEING CREATED
                        Date nextBudgetDate = new Date();
                        Calendar cal = Calendar.getInstance();
                        cal.setTime(nextBudgetDate);
                        createNextBudgetDate(cal, resetTimePeriod);
                        nextBudgetDate = cal.getTime();

                        HomeActivity.thisUser = new User(firstName, lastName, saveMoneyRadioButton.isChecked(), resetTimePeriod, budgetFloat, new Date(), new Date(), nextBudgetDate);
                    }
                    else{ //THIS USER HAS BEEN PREVIOUSLY CREATED
                        String[] mArray = m.split(",");
                        if (!budget.equals(mArray[BUDGET]) || !resetTimePeriod.equals(mArray[BUDGET_RESET])){ //IF there is a change between this budget and last budget
                            Date nextBudgetDate = new Date();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(nextBudgetDate);
                            createNextBudgetDate(cal, resetTimePeriod);
                            nextBudgetDate = cal.getTime();
                            Date appCreatedDate = new Date();
                            try{
                                appCreatedDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(mArray[APP_SETUP_DATE]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            HomeActivity.thisUser = new User(firstName, lastName, saveMoneyRadioButton.isChecked(), resetTimePeriod, budgetFloat,appCreatedDate, new Date(), nextBudgetDate);
                        }
                        else {
                            Date appCreatedDate = new Date();
                            Date budgetStartDate = new Date();
                            Date nextBudgetDate = new Date();
                            try {
                                appCreatedDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(mArray[APP_SETUP_DATE]);
                                budgetStartDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(mArray[CURRENT_BUDGET_START_DATE]);
                                nextBudgetDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(mArray[NEXT_BUDGET_DATE]);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            HomeActivity.thisUser = new User(firstName, lastName, saveMoneyRadioButton.isChecked(), resetTimePeriod, budgetFloat, appCreatedDate, budgetStartDate, nextBudgetDate);
                        }
                    }


                    String message = "" + HomeActivity.thisUser.getFirstName() + "," +
                            HomeActivity.thisUser.getLastName() + "," +
                            HomeActivity.thisUser.isSaveMoney() + "," +
                            HomeActivity.thisUser.getTimePeriod() + "," +
                            HomeActivity.thisUser.getBudget() + "," +
                            HomeActivity.thisUser.getAppSetupDate() + "," +
                            HomeActivity.thisUser.getCurrentBudgetStartDate() + "," +
                            HomeActivity.thisUser.getNextBudgetStartDate();

                    String file_name = "user_info";
                    try {
                        FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                        fileOutputStream.write(message.getBytes());
                        fileOutputStream.close();
                        Snackbar snackbar = Snackbar.make(view, "User Info Saved/Updated", Snackbar.LENGTH_LONG);
                        View snackbarView = snackbar.getView();
                        TextView sbTextView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                        sbTextView.setTextColor(getResources().getColor(R.color.green));
                        snackbar.show();
                        new CountDownTimer(700, 700) {

                            @Override
                            public void onTick(long millisUntilFinished) {
                                // do something after 1s
                            }

                            @Override
                            public void onFinish() {
                                Intent startMainActivity = new Intent(FirstTimeInfoActivity.this, HomeActivity.class);
                                startActivity(startMainActivity);
                                FirstTimeInfoActivity.this.finish();
                            }

                        }.start();
                        //Toast.makeText(getApplicationContext(), "User Info Saved/Updated", Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    Intent startMainActivity = new Intent(FirstTimeInfoActivity.this, HomeActivity.class);
//                    startActivity(startMainActivity);
//                    FirstTimeInfoActivity.this.finish();
                }
                else{
                    Snackbar snackbar = Snackbar.make(view, "One or more fields is invalid!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }

            }
        });
    }

    private void createNextBudgetDate(Calendar cal, String resetTimePeriod){
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
    }

    @Override
    protected void onStart() {
        super.onStart();
        c = this;
    }

    @Override
    public void finish(){
        super.finish();
        if (getIntent().hasExtra("com.something.chris.mysqliteproject.INFO")) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }

    public static void main(String[] args){
        ArrayList<Tag> t = new ArrayList<>();


        Tag tag = new Tag("FFF", "white");
        t.add(tag);
        System.out.println(t.contains(tag));
    }
}
