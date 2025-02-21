package com.project.quizapp.database.entities;

import java.util.List;
import java.util.Map;

public class QuestionCategory {
    private String baseCategory;
    private Map<String,Long> subCategory;
    public String getBaseCategory() {
        return baseCategory;
    }

    @Override
    public String toString() {
        return "QuestionCategory{" +
                "baseCategory='" + baseCategory + '\'' +
                ", subCategory=" + subCategory +
                '}';
    }

    public QuestionCategory() {
    }

    public void setBaseCategory(String baseCategory) {
        this.baseCategory = baseCategory;
    }

    public Map<String, Long> getSubCategory() {
        return subCategory;
    }

    public void setSubCategory(Map<String, Long> subCategory) {
        this.subCategory = subCategory;
    }
}
