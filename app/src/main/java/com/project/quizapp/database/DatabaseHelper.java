package com.project.quizapp.database;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.project.quizapp.database.DatabaseStrings;
import com.project.quizapp.database.Status;

public class DatabaseHelper extends SQLiteOpenHelper implements DatabaseStrings, Status {
    private Context context;
    private static final int DATABASE_VERSION = 1;
    public DatabaseHelper(Context context)
    {
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(USER_TABLE_CREATION_QUERY);
        createAdmin(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(USER_TABLE_DROP_QUERY);
        onCreate(db);
    }

    public int addUser(String firstName,String lastName,String email, String pass)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(USER_COLUMN_FIRST_NAME,firstName);
        contentValues.put(USER_COLUMN_LAST_NAME,lastName);
        contentValues.put(USER_COLUMN_EMAIL,email);
        contentValues.put(USER_COLUMN_PASSWORD,pass);

        long result = db.insert(USER_TABLE_NAME,null,contentValues);

        if(result == -1)
        {
            return (INSERT_FAILED);
        }
        else
        {
            return (INSERT_SUCCESS);
        }
    }

    private void createAdmin(SQLiteDatabase db)
    {

        ContentValues contentValues = new ContentValues();
        contentValues.put(USER_COLUMN_FIRST_NAME,ADMIN_USER_NAME);
        contentValues.put(USER_COLUMN_LAST_NAME,ADMIN_USER_NAME);
        contentValues.put(USER_COLUMN_EMAIL,ADMIN_USER_NAME);
        contentValues.put(USER_COLUMN_PASSWORD,ADMIN_PASS);

        if(db.insert(USER_TABLE_NAME, null, contentValues) == -1)
        {
            Toast.makeText(context, "Admin Failed", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(context, "Admin Success", Toast.LENGTH_SHORT).show();
        }
    }

    public Cursor readUsers()
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;

        if(db != null)
        {
            cursor = db.rawQuery(USER_QUERY_READ_ALL,null);
        }
        return (cursor);
    }
}

