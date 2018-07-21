package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class SplashScreenActivity extends AppCompatActivity {

    private final int SLEEP_TIMER = 600;
    TextView splashTextView;
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
        splashTextView = (TextView) findViewById(R.id.splashTextView);
        LogoLauncher logoLauncher = new LogoLauncher();
        logoLauncher.start();
    }

    private class LogoLauncher extends Thread{
        public void run(){
            try{
                sleep(SLEEP_TIMER);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
            Intent startIntent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(startIntent);
            SplashScreenActivity.this.finish(); //destroy activity so back button doesnt work
        }
    }
}
