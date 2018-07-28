package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

public class FirstTimeInfoActivity extends AppCompatActivity {

    Button onwardsButton;
    Spinner timePeriodSpinner;
    RadioButton saveMoneyRadioButton;
    RadioButton maintainABudgetRadioButton;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PERFORMED_FIRST_TIME_SETUP = "hasPerformedFirstTimeSetup";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public static final String TIME_PERIOD = "timePeriod";
    public static final String SAVE_MONEY = "saveMoney";
    public String firstName;
    public String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_info);

        getSupportActionBar().setTitle("Upload your info to Skynet");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
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
            firstNameEditText.setText(sharedPreferences.getString(FIRST_NAME, "unnamed"));
            lastNameEditText.setText(sharedPreferences.getString(LAST_NAME, "unnamed"));
            if(sharedPreferences.getBoolean(SAVE_MONEY, true)){
                saveMoneyRadioButton.setChecked(true);
                maintainABudgetRadioButton.setChecked(false);
            }
            else{
                saveMoneyRadioButton.setChecked(false);
                maintainABudgetRadioButton.setChecked(true);
            }
            String time = sharedPreferences.getString(TIME_PERIOD, "24 Hours");
            for(int i= 0; i < timePeriodSpinner.getAdapter().getCount(); i++)
            {
                if(timePeriodSpinner.getAdapter().getItem(i).toString().contains(time))
                {
                    timePeriodSpinner.setSelection(i);
                }
            }
        }





        onwardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = firstNameEditText.getText().toString();
                lastName = lastNameEditText.getText().toString();
                String resetTimePeriod = timePeriodSpinner.getSelectedItem().toString();

                if (!firstName.equals("") && !lastName.equals("") && (saveMoneyRadioButton.isChecked() || maintainABudgetRadioButton.isChecked())){
                    editor.putBoolean(PERFORMED_FIRST_TIME_SETUP, true);
                    editor.putString(FIRST_NAME, firstName);
                    editor.putString(LAST_NAME, lastName);
                    editor.putString(TIME_PERIOD, resetTimePeriod);
                    editor.putBoolean(SAVE_MONEY, saveMoneyRadioButton.isChecked());
                    editor.apply();
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
