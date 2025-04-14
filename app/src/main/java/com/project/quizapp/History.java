package com.project.quizapp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.QuestionCategory;
import com.project.quizapp.databinding.ActivityAlertBoxForTestStartBinding;
import com.project.quizapp.databinding.HistoryActivityBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class History extends GlobalDrawerLayoutAndBottomNavigation{
    private HistoryTestMarksRecyclerView historyTestMarksRecyclerView;
    private Map<String,Object> subMarksMap =  new HashMap<>();
    private HistoryActivityBinding binding = null;
    private List<String> subCategory = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = getLayoutInflater().inflate(R.layout.history_activity, findViewById(R.id.content_frame));

        binding = HistoryActivityBinding.bind(view);

        FirebaseDBHelper.getMarksMap(new FirebaseDBHelper.GetMarksMapCallback() {
            @Override
            public void onSuccess(Map<String, Object> marksMap) {
                Set<String> categories = new HashSet<>();

                for(Map.Entry<String, Object> entry : marksMap.entrySet())
                {
                    String[] key = entry.getKey().split("/");
                    categories.add(key[1]);
                }
                Log.d("HISTORY", categories.toString());
                binding.recyclerViewBaseCategories.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                HistoryQuestionBaseCategoryRecyclerView historyQuestionBaseCategoryRecyclerView = new HistoryQuestionBaseCategoryRecyclerView(getApplicationContext(), new ArrayList<>(categories), marksMap, new HistoryQuestionBaseCategoryRecyclerView.OnItemClickListener() {
                    @Override
                    public void onItemClick(String baseCategory) {

                        subCategory = new ArrayList<>();
                        for(Map.Entry<String, Object> entry : marksMap.entrySet())
                        {
                            String[] key = entry.getKey().split("/");
                            if(key[1].equals(baseCategory))
                            {
                                Log.d("HISTORY", key[2]);
                                subMarksMap.put(key[2], entry.getValue());
                                subCategory.add(key[2]);
                            }
                        }

//                        historyTestMarksRecyclerView.notifyDataSetChanged();
                        binding.recyclerViewBaseCategories.setVisibility(View.GONE);

                        //
                        historyTestMarksRecyclerView = new HistoryTestMarksRecyclerView(getApplicationContext(), subCategory,subMarksMap, new HistoryTestMarksRecyclerView.OnItemClickListener() {
                            @Override
                            public void onItemClick() {

                            }
                        });

                        binding.recyclerViewTestMarks.setAdapter(historyTestMarksRecyclerView);

                        if(binding.recyclerViewTestMarks.getVisibility() == View.VISIBLE)
                        {
                            binding.recyclerViewTestMarks.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.recyclerViewTestMarks.setVisibility(View.VISIBLE);
                        }
                    }
                });

                binding.recyclerViewBaseCategories.setAdapter(historyQuestionBaseCategoryRecyclerView);

                binding.recyclerViewTestMarks.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(History.this, errMsg, Toast.LENGTH_SHORT).show();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Check if the subcategories RecyclerView is visible
                if (binding.recyclerViewTestMarks.getVisibility() == View.VISIBLE) {
                    // If it's visible, hide the subcategories view and show the base categories view
                    binding.recyclerViewTestMarks.setVisibility(View.GONE);
                    binding.recyclerViewBaseCategories.setVisibility(View.VISIBLE);
                }
                else
                {
                    finish();
                }
            }
        });

    }
}
