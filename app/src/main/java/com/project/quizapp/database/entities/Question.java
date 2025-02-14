package com.project.quizapp.database.entities;

import java.util.ArrayList;

public class Question {
    private String question;
    private ArrayList<String> options;
    private String answer;
    private String explanation;

    public Question() {}

    public Question(String question, ArrayList<String> options, String answer, String explanation) {
        this.question = question;
        this.options = options;
        this.answer = answer;
        this.explanation = explanation;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public ArrayList<String> getOptions() {
        return options;
    }

    public void setOptions(ArrayList<String> options) {
        this.options = options;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    @Override
    public String toString() {
        return "Question{" +
                "question='" + question + '\'' +
                ", options=" + options +
                ", answer='" + answer + '\'' +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}
