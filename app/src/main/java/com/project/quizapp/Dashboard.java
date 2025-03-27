package com.project.quizapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.QuestionCategory;
import com.project.quizapp.databinding.ActivityAlertBoxForTestStartBinding;
import com.project.quizapp.databinding.ActivityLogicalReasoningBinding;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Dashboard extends GlobalDrawerLayoutAndBottomNavigation {
    private ActivityLogicalReasoningBinding binding;
    private QuestionSubCategoryRecyclerViewAdapter subCategoryRecyclerViewAdapter;
    private static QuestionCategory questionCategory = null;
    private Map<String,Long> subCategoryList =  new HashMap<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_logical_reasoning, findViewById(R.id.content_frame));

        binding = ActivityLogicalReasoningBinding.bind(view);

        FirebaseDBHelper.getQuestionsCategories("/", new FirebaseDBHelper.GetQuestionsCategoriesCallback() {
            @Override
            public void onSuccess(List<QuestionCategory> categories) {
                binding.recyclerViewBaseCategories.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                QuestionBaseCategoryRecyclerViewAdapter baseCategoryRecyclerViewAdapter = new QuestionBaseCategoryRecyclerViewAdapter(getApplicationContext(), categories, new QuestionBaseCategoryRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(QuestionCategory questionCategory) {

                        subCategoryList.clear();
                        Dashboard.questionCategory = questionCategory;
                        subCategoryList.putAll(questionCategory.getSubCategory());
                        subCategoryRecyclerViewAdapter.notifyDataSetChanged();
                        binding.recyclerViewBaseCategories.setVisibility(View.GONE);

                        if(binding.recyclerViewSubCategories.getVisibility() == View.VISIBLE)
                        {
                           binding.recyclerViewSubCategories.setVisibility(View.GONE);
                        }
                        else
                        {
                            binding.recyclerViewSubCategories.setVisibility(View.VISIBLE);
                        }
                    }
                });

                binding.recyclerViewBaseCategories.setAdapter(baseCategoryRecyclerViewAdapter);

                binding.recyclerViewSubCategories.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                subCategoryRecyclerViewAdapter = new QuestionSubCategoryRecyclerViewAdapter(getApplicationContext(), subCategoryList, new QuestionSubCategoryRecyclerViewAdapter.QuestionSubCategoryOnClickCallback() {
                    @Override
                    public void OnSubCategorySelected(String subCategory) {
                        String selectedCategory = questionCategory.getBaseCategory() + "/" +subCategory;

                        // show alert to start the test
                        AlertDialog.Builder builder = new AlertDialog.Builder(Dashboard.this);
                        ActivityAlertBoxForTestStartBinding alertBoxForTestStartBinding = ActivityAlertBoxForTestStartBinding.inflate(LayoutInflater.from(Dashboard.this));
                        builder.setView(alertBoxForTestStartBinding.getRoot());

                        AlertDialog dialog = builder.create();
                        dialog.show();

                        alertBoxForTestStartBinding.next.setOnClickListener(v -> {
                            if(alertBoxForTestStartBinding.checkInstruction.isChecked()) {
                                dialog.dismiss();
                                IntentManager.toQuestionPanelView(getApplicationContext(), selectedCategory, false);
                            }
                            else
                            {
                                Toast.makeText(Dashboard.this, "PLEASE MARK CHECKBOX", Toast.LENGTH_SHORT).show();
                            }
                        });

                        alertBoxForTestStartBinding.cancelTest.setOnClickListener(v -> {
                            dialog.cancel();
                        });

                    }
                });
                binding.recyclerViewSubCategories.setAdapter(subCategoryRecyclerViewAdapter);

            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(Dashboard.this, errMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Check if the subcategories RecyclerView is visible
        if (binding.recyclerViewSubCategories.getVisibility() == View.VISIBLE) {
            // If it's visible, hide the subcategories view and show the base categories view
            binding.recyclerViewSubCategories.setVisibility(View.GONE);
            binding.recyclerViewBaseCategories.setVisibility(View.VISIBLE);
        } else {
            // If subcategories view is not visible, call the default onBackPressed to finish the activity
            super.onBackPressed();
        }
    }

}