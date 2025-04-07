package com.project.quizapp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

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
import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.QuestionCategory;
import com.project.quizapp.databinding.PerformanceActivityBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Performance extends GlobalDrawerLayoutAndBottomNavigation {

    BarChart barChart;
    Context context = this;
    PerformanceActivityBinding binding;

    Map<String, Float> verbalReasoning = null;
    Map<String, Float> logicalReasoning = null;
    Map<String, Float> aptitude = null;

    Float totalPercentage = 0.0f;

    Float logicalReasoningAvgPercentage = 0.0f;
    Float verbalReasoningAvgPercentage = 0.0f;
    Float aptitudeAvgPercentage = 0.0f;

    List<BarEntry> entries = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.performance_activity, findViewById(R.id.content_frame));

        binding = PerformanceActivityBinding.bind(view);
        barChart = findViewById(R.id.barChart);

        FirebaseDBHelper.getMarksMap(new FirebaseDBHelper.GetMarksMapCallback() {
            @Override
            public void onSuccess(Map<String, Object> marksMap) {

                logicalReasoning = new HashMap<>();
                verbalReasoning = new HashMap<>();
                aptitude = new HashMap<>();


                for (Map.Entry<String, Object> entry : marksMap.entrySet()) {
                    String key = entry.getKey();
                    Object marks = entry.getValue();
                    String[] path = key.split("/");

                    if(path[1].equals("Logical-Reasoning"))
                    {
                        logicalReasoning.put(path[2], Float.parseFloat(marks.toString()));
                    }
                    if(path[1].equals("Verbal-Reasoning"))
                    {
                        verbalReasoning.put(path[2], Float.parseFloat(marks.toString()));
                    }
                    if(path[1].equals("aptitude"))
                    {
                        aptitude.put(path[2], Float.parseFloat(marks.toString()));
                    }
                }

                Log.d("PERF_Logi", logicalReasoning.toString());
                Log.d("PERF_Verb", verbalReasoning.toString());
                Log.d("PERF_Apt", aptitude.toString());

                calculateAvgCategoryMarks();
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void calculateAvgCategoryMarks()
    {
        FirebaseDBHelper.getQuestionsCategories("/", new FirebaseDBHelper.GetQuestionsCategoriesCallback() {
            @Override
            public void onSuccess(List<QuestionCategory> categories) {
                int totalLogicalTest = 0;
                int totalVerbalTest = 0;
                int totalApptitude = 0;

                for(int i = 0; i < categories.size(); i++)
                {
                   Log.d("CAT_PER", categories.get(i).getSubCategory().size() + "");
                   if(categories.get(i).getBaseCategory().equals("Logical-Reasoning"))
                   {
                       totalLogicalTest = categories.get(i).getSubCategory().size();
                   }

                    if(categories.get(i).getBaseCategory().equals("Verbal-Reasoning"))
                    {
                        totalVerbalTest = categories.get(i).getSubCategory().size();
                    }

                    if(categories.get(i).getBaseCategory().equals("aptitude"))
                    {
                        totalApptitude = categories.get(i).getSubCategory().size();
                    }
                }

                if(logicalReasoning != null)
                {
                    totalPercentage = 0.0f;

                    for(Map.Entry<String, Float> entry : logicalReasoning.entrySet())
                    {
                        totalPercentage = totalPercentage + entry.getValue();
                    }
                    logicalReasoningAvgPercentage = totalPercentage / (float) totalLogicalTest;
                }

                if(verbalReasoning != null)
                {
                    totalPercentage = 0.0f;

                    for(Map.Entry<String, Float> entry : verbalReasoning.entrySet())
                    {
                        totalPercentage = totalPercentage + entry.getValue();
                    }

                    verbalReasoningAvgPercentage = (totalPercentage / (float) totalVerbalTest);
                }

                if(aptitude != null)
                {
                    totalPercentage = 0.0f;

                    for(Map.Entry<String, Float> entry : aptitude.entrySet())
                    {
                        totalPercentage = totalPercentage + entry.getValue();
                    }

                    aptitudeAvgPercentage = (totalPercentage / (float) totalApptitude);
                }

                logicalReasoningAvgPercentage = Float.parseFloat(String.format("%.2f", logicalReasoningAvgPercentage));
                verbalReasoningAvgPercentage = Float.parseFloat(String.format("%.2f", verbalReasoningAvgPercentage));
                aptitudeAvgPercentage = Float.parseFloat(String.format("%.2f", aptitudeAvgPercentage));

                // fill the graph entries
                entries = new ArrayList<>();
                entries.add(new BarEntry(0f, verbalReasoningAvgPercentage));  // verbal
                entries.add(new BarEntry(1f, logicalReasoningAvgPercentage));  // reasoning
                entries.add(new BarEntry(2f, aptitudeAvgPercentage));  // aptitude

                setupBarChart();
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBarChart() {

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
        final String[] categories = new String[]{"Verbal", "Logical", "Aptitude"};
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
                return String.format(Locale.getDefault(), "%.2f", value); // no decimal
            }
        });
        barChart.invalidate();
    }
}
