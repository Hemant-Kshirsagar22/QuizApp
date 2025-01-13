package com.project.quizapp.database;

public interface DatabaseStrings {
    String DATABASE_NAME = "QuizApp.db";

    // User Table
    String USER_TABLE_NAME = "users";
    String USER_COLUMN_ID = "uid";
    String USER_COLUMN_FIRST_NAME = "first_name";
    String USER_COLUMN_LAST_NAME = "last_name";
    String USER_COLUMN_EMAIL = "email";
    String USER_COLUMN_PASSWORD = "pass";


    // user table queries
    String USER_TABLE_CREATION_QUERY = "CREATE TABLE " +USER_TABLE_NAME +" ("+ USER_COLUMN_ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " + USER_COLUMN_FIRST_NAME + " TEXT, " + USER_COLUMN_LAST_NAME + " TEXT, " + USER_COLUMN_EMAIL + " TEXT UNIQUE, " + USER_COLUMN_PASSWORD + " TEXT);";

    // readAddData
    String USER_QUERY_READ_ALL = "SELECT * FROM " + USER_TABLE_NAME + ";";
    // drop users
    String USER_TABLE_DROP_QUERY = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;

    // admin
    String ADMIN_USER_NAME = "admin@gmail.com";
    String ADMIN_PASS = "123";


}
