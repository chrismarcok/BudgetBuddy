package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SLEEP_TIMER = 600;
    TextView splashTextView;
    ImageView splashImageView;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String PERFORMED_FIRST_TIME_SETUP = "hasPerformedFirstTimeSetup";
    public static boolean hasPerformedFirstTimeSetup;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //content view must be set after fullscreen
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        //


        splashImageView = (ImageView) findViewById(R.id.splashImageView);
        splashTextView = (TextView) findViewById(R.id.splashTextView);
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
    }

    private class LogoLauncher extends Thread{
        public void run(){
            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            hasPerformedFirstTimeSetup = sharedPreferences.getBoolean(PERFORMED_FIRST_TIME_SETUP, false);

            try{
                sleep(SLEEP_TIMER);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent startIntent;
            //If the app has never been run before, create a firstTimeActivity instead of passing to normal MainActivity
            if (!hasPerformedFirstTimeSetup){
                startIntent = new Intent(SplashScreenActivity.this, FirstTimeActivity.class);
            }
            else{
                startIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);
            }

            //DEBUGGING
            //startIntent = new Intent(SplashScreenActivity.this, HomeActivity.class);

            startActivity(startIntent);
            SplashScreenActivity.this.finish(); //destroy activity so back button doesnt work
        }
    }
}
