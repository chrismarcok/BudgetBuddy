package com.something.chris.mysqliteproject;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity {

    PieChart pieChart;

    ConstraintLayout myLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_analysis);
        getSupportActionBar().hide();

        setupPieChart();

    }

    private void setupPieChart(){
        //Populate a list of pieEntries
        List<PieEntry> pieEntries = new ArrayList<>();
        HashMap<String, Float> map = new HashMap<>();
        List<String> hexCodes = new ArrayList<>();
        for (int i = 0; i < HomeActivity.entries.size(); i++){
            if (!map.containsKey(HomeActivity.entries.get(i).get_tag().getText())){
                hexCodes.add("#" + HomeActivity.entries.get(i).get_tag().getCol());
                map.put(HomeActivity.entries.get(i).get_tag().getText(), 0.0f);
            }
            map.put(HomeActivity.entries.get(i).get_tag().getText(), map.get(HomeActivity.entries.get(i).get_tag().getText()) + HomeActivity.entries.get(i).get_value());
        }
        String[] cols = hexCodes.toArray(new String[hexCodes.size()]);
        for (String s : map.keySet()){
            pieEntries.add(new PieEntry(map.get(s), s));
        }
        int[] colors = new int[cols.length];
        for (int i = 0; i < colors.length; i ++){
            colors[i] = Color.parseColor(cols[i]);
        }

        PieDataSet dataSet = new PieDataSet(pieEntries, "");
        dataSet.setColors(colors);
        dataSet.setValueTextSize(16f);
        dataSet.setSliceSpace(3);
        PieData data = new PieData(dataSet);

        //Get the chart
        pieChart = (PieChart) findViewById(R.id.pieChart);
        pieChart.setData(data);
        pieChart.setCenterText("Your Total Spending");
        pieChart.animateY(1000);
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.invalidate();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
