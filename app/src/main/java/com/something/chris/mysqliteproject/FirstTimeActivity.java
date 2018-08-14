package com.something.chris.mysqliteproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class FirstTimeActivity extends AppCompatActivity {

    CardView letsGoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //content view must be set after fullscreen
        setContentView(R.layout.activity_first_time);
        getSupportActionBar().hide();

        letsGoButton = (CardView) findViewById(R.id.letsGoButton);

        letsGoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent firstTimeInfoActivityIntent = new Intent(FirstTimeActivity.this, FirstTimeInfoActivity.class);
                startActivity(firstTimeInfoActivityIntent);
                FirstTimeActivity.this.finish();
            }
        });
    }
}
