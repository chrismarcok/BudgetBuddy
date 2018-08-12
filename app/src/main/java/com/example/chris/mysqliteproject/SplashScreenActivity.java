package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SLEEP_TIMER = 150;
    ImageView splashImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //content view must be set after fullscreen
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        splashImageView = (ImageView) findViewById(R.id.splashImageView);
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
    }

    private class LogoLauncher extends Thread{
        public void run(){
            String message = "";

            try {
                FileInputStream fileInputStream = openFileInput("user_info");
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                StringBuffer stringBuffer = new StringBuffer();
                while ((message = bufferedReader.readLine()) != null){
                    stringBuffer.append(message);
                }
                message = stringBuffer.toString();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e){
                e.printStackTrace();
            }


            try{
                sleep(SLEEP_TIMER);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent startIntent;
            //If the app has never been run before, create a firstTimeActivity instead of passing to normal MainActivity
            if (message.equals("")){
                startIntent = new Intent(SplashScreenActivity.this, FirstTimeActivity.class);
            }
            else{
                startIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            }


            startActivity(startIntent);
            SplashScreenActivity.this.finish(); //destroy activity so back button doesnt work
        }
    }
}
