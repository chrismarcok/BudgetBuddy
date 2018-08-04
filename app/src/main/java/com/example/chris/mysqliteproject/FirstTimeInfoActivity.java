package com.example.chris.mysqliteproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class FirstTimeInfoActivity extends AppCompatActivity {

    Button onwardsButton;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_info);

        getSupportActionBar().setTitle("Create a Budget");

        budgetEditText = (EditText) findViewById(R.id.budgetEditText);
        onwardsButton = (Button) findViewById(R.id.onwardsButton);
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

        if (getIntent().hasExtra("com.example.chris.mysqliteproject.INFO")){
            onwardsButton.setText("Save Settings");
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

                firstNameEditText.setText(strArr[0]);
                lastNameEditText.setText(strArr[1]);

                if(Boolean.parseBoolean(strArr[2])){
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
                    }
                }
                budgetEditText.setText(strArr[4]);

            } catch (FileNotFoundException e){
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

        onwardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                            tagDBHandler.addEntry(new Tag(-1,"red", "supermarket"));
                            tagDBHandler.addEntry(new Tag(-1,"blue", "entertainment"));
                            tagDBHandler.addEntry(new Tag(-1,"green", "clothing"));
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

                        HomeActivity.thisUser = new User(firstName, lastName, saveMoneyRadioButton.isChecked(), resetTimePeriod, budgetFloat, new Date(), new Date(), nextBudgetDate);
                    }
                    else{ //THIS USER HAS BEEN PREVIOUSLY CREATED
                        String[] mArray = m.split(",");
                        if (!budget.equals(mArray[4]) || !resetTimePeriod.equals(mArray[3])){ //IF there is a change between this budget and last budget
                            Date nextBudgetDate = new Date();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(nextBudgetDate);
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
                            Date appCreatedDate = new Date();
                            try{
                                appCreatedDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(mArray[5]);
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
                                appCreatedDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(mArray[5]);
                                budgetStartDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(mArray[6]);
                                nextBudgetDate = MyDBHandler.DATE_FORMAT_CALENDAR.parse(mArray[7]);
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
                        Toast.makeText(getApplicationContext(), "User Info Saved/Updated", Toast.LENGTH_SHORT).show();

                    } catch (FileNotFoundException e){
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Intent startMainActivity = new Intent(FirstTimeInfoActivity.this, HomeActivity.class);
                    startActivity(startMainActivity);
                    FirstTimeInfoActivity.this.finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "One or more fields are empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        c = this;
    }

    @Override
    public void finish(){
        super.finish();
        if (getIntent().hasExtra("com.example.chris.mysqliteproject.INFO")) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
