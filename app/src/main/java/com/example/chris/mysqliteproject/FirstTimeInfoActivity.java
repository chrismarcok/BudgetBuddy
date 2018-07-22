package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FirstTimeInfoActivity extends AppCompatActivity {

    Button onwardsButton;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PERFORMED_FIRST_TIME_SETUP = "hasPerformedFirstTimeSetup";
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastName";
    public String firstName;
    public String lastName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_info);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        onwardsButton = (Button) findViewById(R.id.onwardsButton);
        final EditText firstNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        final EditText lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);

        onwardsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                firstName = firstNameEditText.getText().toString();
                lastName = lastNameEditText.getText().toString();

                if (!firstName.equals("") && !lastName.equals("")){
                    editor.putBoolean(PERFORMED_FIRST_TIME_SETUP, true);
                    editor.putString(FIRST_NAME, firstName);
                    editor.putString(LAST_NAME, lastName);
                    editor.apply();
                    Intent startMainActivity = new Intent(FirstTimeInfoActivity.this, HomeActivity.class);
                    startActivity(startMainActivity);
                }
                else{
                    Toast.makeText(getApplicationContext(), "One or more fields are empty!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
