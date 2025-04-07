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
    private static QuestionCategory questionCategory = null;
    private Map<String,Object> subMarksMap =  new HashMap<>();
    private HistoryActivityBinding binding = null;
    private List<String> subCategory = null;

    private boolean doubleBackToExitPressedOnce = false;
    private Toast exitToast;

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



//                binding.recyclerViewSubCategories.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                subCategoryRecyclerViewAdapter = new QuestionSubCategoryRecyclerViewAdapter(getApplicationContext(), subCategoryList, new QuestionSubCategoryRecyclerViewAdapter.QuestionSubCategoryOnClickCallback() {
//                    @Override
//                    public void OnSubCategorySelected(String subCategory) {
//                        String selectedCategory = questionCategory.getBaseCategory() + "/" +subCategory;
//
//
//                    }
//                });
//
//                binding.recyclerViewSubCategories.setAdapter(subCategoryRecyclerViewAdapter);
//                binding.recyclerViewBaseCategories.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                QuestionBaseCategoryRecyclerViewAdapter baseCategoryRecyclerViewAdapter = new QuestionBaseCategoryRecyclerViewAdapter(getApplicationContext(), categories, new QuestionBaseCategoryRecyclerViewAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(QuestionCategory questionCategory) {
//
//                        subCategoryList.clear();
//                        History.questionCategory = questionCategory;
//                        subCategoryList.putAll(questionCategory.getSubCategory());
//                        subCategoryRecyclerViewAdapter.notifyDataSetChanged();
//                        binding.recyclerViewBaseCategories.setVisibility(View.GONE);
//
//                        if(binding.recyclerViewSubCategories.getVisibility() == View.VISIBLE)
//                        {
//                            binding.recyclerViewSubCategories.setVisibility(View.GONE);
//                        }
//                        else
//                        {
//                            binding.recyclerViewSubCategories.setVisibility(View.VISIBLE);
//                        }
//                    }
//                });
//
//                binding.recyclerViewBaseCategories.setAdapter(baseCategoryRecyclerViewAdapter);
//
//                binding.recyclerViewSubCategories.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//                subCategoryRecyclerViewAdapter = new QuestionSubCategoryRecyclerViewAdapter(getApplicationContext(), subCategoryList, new QuestionSubCategoryRecyclerViewAdapter.QuestionSubCategoryOnClickCallback() {
//                    @Override
//                    public void OnSubCategorySelected(String subCategory) {
//                        String selectedCategory = questionCategory.getBaseCategory() + "/" +subCategory;
//
//                        // show alert to start the test
//                        AlertDialog.Builder builder = new AlertDialog.Builder(History.this);
//                        ActivityAlertBoxForTestStartBinding alertBoxForTestStartBinding = ActivityAlertBoxForTestStartBinding.inflate(LayoutInflater.from(History.this));
//                        builder.setView(alertBoxForTestStartBinding.getRoot());
//
//                        AlertDialog dialog = builder.create();
//                        dialog.show();
//
//                        alertBoxForTestStartBinding.next.setOnClickListener(v -> {
//                            if(alertBoxForTestStartBinding.checkInstruction.isChecked()) {
//                                dialog.dismiss();
//                                finish();
//                                IntentManager.toQuestionPanelView(getApplicationContext(), selectedCategory, false, false);
//                            }
//                            else
//                            {
//                                Toast.makeText(History.this, "PLEASE MARK CHECKBOX", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//
//                        alertBoxForTestStartBinding.cancelTest.setOnClickListener(v -> {
//                            dialog.cancel();
//                        });
//
//                    }
//                });
//                binding.recyclerViewSubCategories.setAdapter(subCategoryRecyclerViewAdapter);

            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(History.this, errMsg, Toast.LENGTH_SHORT).show();
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    if (doubleBackToExitPressedOnce) {
                        if (exitToast != null) exitToast.cancel();
                        return;
                    }

                    // Check if the subcategories RecyclerView is visible
                    if (binding.recyclerViewTestMarks.getVisibility() == View.VISIBLE) {
                        // If it's visible, hide the subcategories view and show the base categories view
                        binding.recyclerViewTestMarks.setVisibility(View.GONE);
                        binding.recyclerViewBaseCategories.setVisibility(View.VISIBLE);
                    }
                    doubleBackToExitPressedOnce = true;
                    exitToast = Toast.makeText(History.this, "Press again to exit", Toast.LENGTH_SHORT);
                    exitToast.show();

                    new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
                }
            }
        });

    }
}
