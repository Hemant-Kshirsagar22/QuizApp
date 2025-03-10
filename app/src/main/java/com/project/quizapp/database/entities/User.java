package com.project.quizapp.database.entities;

import java.util.Map;

public class User {
    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Boolean isGoogleUser = false;
    private Map<String, Map<String, Float>> MarksMap = null; // it is a map of <Category, <SubCategory, Marks>

    public User()
    {

    }

    public User(String firstName, String lastName, String email, String password) {
        this.password = password;
        this.email = email;
        this.lastName = lastName;
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getGoogleUser() {
        return isGoogleUser;
    }

    public void setGoogleUser(Boolean googleUser) {
        isGoogleUser = googleUser;
    }

    public Map<String, Map<String, Float>> getMarksMap() {
        return MarksMap;
    }

    public void setMarksMap(Map<String, Map<String, Float>> marksMap) {
        MarksMap = marksMap;
    }
}
