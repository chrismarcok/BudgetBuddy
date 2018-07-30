package com.example.chris.mysqliteproject;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;

public class HomeActivity extends AppCompatActivity {

    Dialog myDialog;
    Dialog infoDialog;

    CardView cardOneCardView;
    CardView cardTwoCardView;
    CardView cardThreeCardView;
    CardView cardFourCardView;
    CardView cardFiveCardView;
    CardView cardSixCardView;
    MyDBHandler dbHandler;

    public static User thisUser = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();

        loadThisUser();
        dbHandler = new MyDBHandler(this, null, null, 1);
        dbHandler.fetchDatabaseEntries();

        myDialog = new Dialog(this);


        cardOneCardView = (CardView) findViewById(R.id.cardOneCardView);
        cardTwoCardView = (CardView) findViewById(R.id.cardTwoCardView);
        cardThreeCardView =(CardView) findViewById(R.id.cardThreeCardView);
        cardFourCardView = (CardView) findViewById(R.id.cardFourCardView);
        cardFiveCardView = (CardView) findViewById(R.id.cardFiveCardView);
        cardSixCardView =(CardView) findViewById(R.id.cardSixCardView);


        cardTwoCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, MainActivity.class);
                startActivity(startIntent);
            }
        });
        cardThreeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, LogsActivity.class);
                if (MainActivity.entries.size() > 0) {
                    startActivity(startIntent);
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else{
                    Toast.makeText(getApplicationContext(), "No logs to show!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        cardFourCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, RecordsActivity.class);
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
        cardFiveCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(HomeActivity.this, FirstTimeInfoActivity.class);
                startIntent.putExtra("com.example.chris.mysqliteproject.INFO", "This information was passed from the first activity.");
                startActivity(startIntent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

    }

    public void showPopup(View v){
        TextView txtclose;
        Button submitButton;
        final EditText amountEditText;
        final EditText dateEditText;
        final EditText locationEditText;
        final EditText detailsEditText;

        myDialog.setContentView(R.layout.newentrypopup);
        txtclose = (TextView) myDialog.findViewById(R.id.txtclose);
        submitButton = (Button) myDialog.findViewById(R.id.submitButton);
        amountEditText = (EditText) myDialog.findViewById(R.id.amountEditText);
        dateEditText = (EditText) myDialog.findViewById(R.id.dateEditText);
        locationEditText = (EditText) myDialog.findViewById(R.id.locationEditText);
        detailsEditText = (EditText) myDialog.findViewById(R.id.detailsEditText);
        final MyDBHandler dbHandler = new MyDBHandler(this, null, null, 1);

        infoDialog = new Dialog(this);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String amountString =  amountEditText.getText().toString();
                if (amountString.equals("")){
                    Toast.makeText(getApplicationContext(), "One or more required fields is empty!", Toast.LENGTH_SHORT).show();
                }
                else{
                    Date now = new Date();
                    Float amountFloat = Float.parseFloat(amountString);
                    amountFloat = ((int)(amountFloat*100 + 0.5))/100.0f;


                    Entry newEntry = new Entry(amountFloat, now, locationEditText.getText().toString(), detailsEditText.getText().toString());
                    dbHandler.addEntry(newEntry);
                    Toast.makeText(getApplicationContext(), "Entry added", Toast.LENGTH_SHORT).show();
                    dbHandler.fetchDatabaseEntries();
                    myDialog.dismiss();
                }
            }
        });

        txtclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDialog.dismiss();
            }
        });
        myDialog.show();
    }

    public void showInfo(View v){
        infoDialog.setContentView(R.layout.infopopup);
        TextView infoTxtClose;

        infoTxtClose = (TextView) infoDialog.findViewById(R.id.infoTxtClose);

        infoTxtClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoDialog.dismiss();
            }
        });
        infoDialog.show();
    }

    public void loadThisUser() {
        try {
            String message = "";
            FileInputStream fileInputStream = openFileInput("user_info");
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            StringBuffer stringBuffer = new StringBuffer();
            while ((message = bufferedReader.readLine()) != null) {
                stringBuffer.append(message);
            }
            String result = stringBuffer.toString();
            String strArr[] = result.split(",");

            thisUser.setFirstName(strArr[0]);
            thisUser.setLastName(strArr[1]);
            thisUser.setSaveMoney(Boolean.parseBoolean(strArr[2]));
            thisUser.setTimePeriod(strArr[3]);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

//    public void showConfirmPopUp(View v){
//        confirmDialog.setContentView(R.layout.confirmpopup);
//        TextView confirmTxtClose;
//        TextView okayTextView;
//
//        okayTextView = (TextView) findViewById(R.id.okayTextView);
//        confirmTxtClose = (TextView) findViewById(R.id.confirmTxtClose);
//
//        okayTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
//        confirmTxtClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                confirmDialog.dismiss();
//            }
//        });
//
//        confirmDialog.show();
//    }
    /*
    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setEnabled(false);
        editText.setCursorVisible(false);
        editText.setKeyListener(null);
        editText.setBackgroundColor(Color.TRANSPARENT);
    }
    */
}
