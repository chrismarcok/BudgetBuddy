package com.example.chris.mysqliteproject;

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

public class FirstTimeInfoActivity extends AppCompatActivity {

    Button onwardsButton;
    Spinner timePeriodSpinner;
    RadioButton saveMoneyRadioButton;
    RadioButton maintainABudgetRadioButton;
    EditText budgetEditText;

    public String firstName;
    public String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_info);

        getSupportActionBar().setTitle("Upload your info to Skynet");

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
        saveMoneyRadioButton.setChecked(true);

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

                if (!firstName.equals("") &&
                        !lastName.equals("") &&
                        (saveMoneyRadioButton.isChecked() || maintainABudgetRadioButton.isChecked()) &&
                        !budget.equals("")){

                    //STORE AS:
                    /*
                    user_info

                        firstnameString,secondnameString,saveMoneyBool,budgetResetString,budgetFloat

                     */
                    float budgetFloat = Float.parseFloat(budget);
                    HomeActivity.thisUser = new User(firstName, lastName, saveMoneyRadioButton.isChecked(), resetTimePeriod, budgetFloat);

                    String message = "" + HomeActivity.thisUser.getFirstName() + "," +
                            HomeActivity.thisUser.getLastName() + "," +
                            HomeActivity.thisUser.isSaveMoney() + "," +
                            HomeActivity.thisUser.getTimePeriod() + "," +
                            HomeActivity.thisUser.getBudget();

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
    public void finish(){
        super.finish();
        if (getIntent().hasExtra("com.example.chris.mysqliteproject.INFO")) {
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }
    }
}
