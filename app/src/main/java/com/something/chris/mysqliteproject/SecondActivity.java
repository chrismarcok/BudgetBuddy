package com.something.chris.mysqliteproject;


import com.opencsv.CSVReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class SecondActivity extends AppCompatActivity {


    CardView websiteButton;
    CardView githubButton;
    CardView GPlayButton;
    ImageView BSJ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_second);
        getSupportActionBar().hide();


        websiteButton = (CardView) findViewById(R.id.websiteButton);
        githubButton = (CardView) findViewById(R.id.githubButton);
        GPlayButton = (CardView) findViewById(R.id.GPlayButton);
        BSJ = (ImageView) findViewById(R.id.BSJImageView);
        final MediaPlayer BSJMP = MediaPlayer.create(this, R.raw.thanks);

        BSJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BSJMP.start();
            }
        });

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
        GPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri webaddress = Uri.parse("https://play.google.com/store/apps/developer?id=Cutlass+Studios");

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
