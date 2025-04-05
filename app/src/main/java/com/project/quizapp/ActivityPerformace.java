package com.project.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ActivityPerformace extends AppCompatActivity {

    BarChart barChart;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_performace); // Uses layout with included bar chart

        barChart = findViewById(R.id.barChart);
        setupBarChart();
    }

    private void setupBarChart() {
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 30f));  // verbal
        entries.add(new BarEntry(1f, 80f));  // reasoning
        entries.add(new BarEntry(2f, 60f));  // aptitude

        BarDataSet dataSet = new BarDataSet(entries, "Questions Answered");
        dataSet.setColors(
                Color.parseColor("#FFD6E7"),
                Color.parseColor("#C7D7FF"),
                Color.parseColor("#BFC6DF")
        );

        //color compatibility
        dataSet.setValueTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));

        //Text style bold
        dataSet.setValueTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        // Set legend label color to textColorPrimary
        Legend legend = barChart.getLegend();
        legend.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary));

        dataSet.setValueTextSize(12f);

        BarData data = new BarData(dataSet);
        data.setBarWidth(0.5f);
        barChart.setData(data);

        // X-Axis labels
        final String[] categories = new String[]{"Verbal", "Aptitude", "Reasoning"};
        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(categories));
        xAxis.setGranularity(1f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawGridLines(false);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setTextSize(12f);

        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisLeft().setTextSize(12f);
        barChart.getAxisRight().setEnabled(false);

        barChart.getDescription().setEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(1500);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawLabels(true);                  // ensure labels are drawn
        leftAxis.setTextColor(ContextCompat.getColor(context, R.color.textColorPrimary)); // visible color
        leftAxis.setTextSize(12f);                     // readable size
        leftAxis.setGranularity(10f);                  // spacing between labels
        leftAxis.setGranularityEnabled(true);
        leftAxis.setAxisMinimum(0f);                   // start from 0
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format(Locale.getDefault(), "%.0f", value); // no decimal
            }
        });
        barChart.invalidate();
    }
}
