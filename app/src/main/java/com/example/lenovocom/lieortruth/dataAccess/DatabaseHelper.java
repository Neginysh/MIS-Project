package com.example.lenovocom.lieortruth.dataAccess;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.lenovocom.lieortruth.entities.AnswerFeature;
import com.example.lenovocom.lieortruth.entities.Question;
import com.example.lenovocom.lieortruth.entities.User;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "mis_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create notes table
        db.execSQL(UserDataAccess.CREATE_TABLE);
        db.execSQL(QuestionDataAccess.CREATE_TABLE);
        db.execSQL(TestDataAccess.CREATE_TABLE);
        db.execSQL(AnswerFeatureDataAccess.CREATE_TABLE);


    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + QuestionDataAccess.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

}
