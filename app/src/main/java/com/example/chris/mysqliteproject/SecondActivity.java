package com.example.chris.mysqliteproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

    TextView secondActivityTextView;
    Button websiteButton;

    Button readBtn;
    Button writeBtn;
    TextView resultTextView;
    EditText inputEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        this.setTitle("Second Title");

        readBtn = (Button) findViewById(R.id.readBtn);
        writeBtn = (Button) findViewById(R.id.writeBtn);
        resultTextView = (TextView) findViewById(R.id.resultTextView);
        inputEditText = (EditText) findViewById(R.id.amountEditText);
        secondActivityTextView = (TextView) findViewById(R.id.secondActivityTextView);
        websiteButton = (Button) findViewById(R.id.websiteButton);

        if (getIntent().hasExtra("com.example.chris.mysqliteproject.INFO")){
            secondActivityTextView.setText(getIntent().getExtras().getString("com.example.chris.mysqliteproject.INFO"));
        }

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
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = inputEditText.getText().toString();
                String file_name = "hello_file";
                try {
                    FileOutputStream fileOutputStream = openFileOutput(file_name, MODE_PRIVATE);
                    fileOutputStream.write(message.getBytes());
                    fileOutputStream.close();
                    Toast.makeText(getApplicationContext(), "Message Saved", Toast.LENGTH_SHORT).show();
                    inputEditText.setText("");
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String message = "";
                    FileInputStream fileInputStream = openFileInput("hello_file");
                    InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                    BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                    StringBuffer stringBuffer = new StringBuffer();
                    while ((message = bufferedReader.readLine()) != null){
                        stringBuffer.append(message + "\n");
                    }
                    resultTextView.setText(stringBuffer.toString());
                } catch (FileNotFoundException e){
                    e.printStackTrace();
                } catch (IOException e){
                    e.printStackTrace();
                }

            }
        });
    }
}
