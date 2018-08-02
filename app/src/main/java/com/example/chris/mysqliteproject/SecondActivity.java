package com.example.chris.mysqliteproject;


import com.opencsv.CSVReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SecondActivity extends AppCompatActivity {


    Button websiteButton;
    Button githubButton;
    Button projButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);
        getSupportActionBar().hide();


        websiteButton = (Button) findViewById(R.id.websiteButton);
        githubButton = (Button) findViewById(R.id.githubButton);
        projButton = (Button) findViewById(R.id.thisProjButton);


        websiteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webaddress = Uri.parse("http://www.chrismarcok.xyz");

                Intent launchWebsite = new Intent(Intent.ACTION_VIEW, webaddress);
                if (launchWebsite.resolveActivity(getPackageManager()) != null){
                    startActivity(launchWebsite);
                }
            }
        });
        githubButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webaddress = Uri.parse("https://github.com/chrismarcok");

                Intent launchWebsite = new Intent(Intent.ACTION_VIEW, webaddress);
                if (launchWebsite.resolveActivity(getPackageManager()) != null){
                    startActivity(launchWebsite);
                }
            }
        });
        projButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webaddress = Uri.parse("https://github.com/chrismarcok/BudgetBuddy");

                Intent launchWebsite = new Intent(Intent.ACTION_VIEW, webaddress);
                if (launchWebsite.resolveActivity(getPackageManager()) != null){
                    startActivity(launchWebsite);
                }
            }
        });

    }
    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
