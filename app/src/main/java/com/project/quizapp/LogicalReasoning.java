package com.project.quizapp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.project.quizapp.database.FirebaseDBHelper;
import com.project.quizapp.database.entities.QuestionCategory;
import com.project.quizapp.databinding.ActivityDashboardBinding;
import com.project.quizapp.databinding.ActivityLogicalReasoningBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LogicalReasoning extends GlobalDrawerLayoutAndBottomNavigation {
    private ActivityLogicalReasoningBinding binding;
    private QuestionSubCategoryRecyclerViewAdapter subCategoryRecyclerViewAdapter;
    private Map<String,Long> subCategoryList =  new HashMap<>();;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = getLayoutInflater().inflate(R.layout.activity_logical_reasoning, findViewById(R.id.content_frame));

        binding = ActivityLogicalReasoningBinding.bind(view);

        FirebaseDBHelper.getQuestionsCategories("/", new FirebaseDBHelper.GetQuestionsCategoriesCallback() {
            @Override
            public void onSuccess(List<QuestionCategory> categories) {
//                binding.recycleView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
////                adapter = new AdapterForCardViewTestList(getApplicationContext(), categories);
//                binding.recycleView.setAdapter(adapter);
                binding.recyclerViewBaseCategories.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                QuestionBaseCategoryRecyclerViewAdapter baseCategoryRecyclerViewAdapter = new QuestionBaseCategoryRecyclerViewAdapter(getApplicationContext(), categories, new QuestionBaseCategoryRecyclerViewAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(QuestionCategory questionCategory) {
                        subCategoryList.clear();
                        subCategoryList.putAll(questionCategory.getSubCategory());
                        subCategoryRecyclerViewAdapter.notifyDataSetChanged();

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
                subCategoryRecyclerViewAdapter = new QuestionSubCategoryRecyclerViewAdapter(getApplicationContext(),subCategoryList);
                binding.recyclerViewSubCategories.setAdapter(subCategoryRecyclerViewAdapter);

            }

            @Override
            public void onFailure(String errMsg) {
                Toast.makeText(LogicalReasoning.this, errMsg, Toast.LENGTH_SHORT).show();
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