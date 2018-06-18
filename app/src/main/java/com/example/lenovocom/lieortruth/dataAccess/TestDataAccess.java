package com.example.lenovocom.lieortruth.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.lenovocom.lieortruth.entities.Test;

import java.util.ArrayList;
import java.util.List;

public class TestDataAccess {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public static final String TABLE_NAME = "test";
    public static final String COLUMN_ID = "id";
    public static final String DATE = "date";
    public static final String SENT_TO_SERVER = "reported";





    private String[] allColumns = {COLUMN_ID, DATE, SENT_TO_SERVER};

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + DATE + " TEXT,"
                    + SENT_TO_SERVER + " BOOLEAN"
                    + ")";

    public void TestDataAccess(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Test Create(String date, boolean reported){
        ContentValues values = new ContentValues();
        values.put(DATE, date);
        values.put(SENT_TO_SERVER, reported);
        long insertId = database.insert(TABLE_NAME, null, values);
        Cursor cursor = database.query(TABLE_NAME, allColumns,COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Test test = cursorToTest(cursor);
        cursor.close();
        return test;
    }

    public void delete(Test test) {
        int id = test.getId();
        System.out.println("Test deleted with id: " + id);
        database.delete(TABLE_NAME, COLUMN_ID
                + " = " + id, null);
    }

    public List<Test> getAll() {
        List<Test> tests = new ArrayList<Test>();

        Cursor cursor = database.query(TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Test comment = cursorToTest(cursor);
            tests.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return tests;
    }

    public void update(Test test){
        int id = test.getId();
        ContentValues values = new ContentValues();
        values.put(DATE, test.getDate());
        values.put(SENT_TO_SERVER, test.getSentToServer());
        database.update(TABLE_NAME, values, COLUMN_ID + " = " + id, null);
    }

    private Test cursorToTest(Cursor cursor) {
        Test test = new Test();
        test.setDate(cursor.getString(0));
        test.setSentToServer(cursor.getInt(1) > 0); //getBoolean // ref: https://stackoverflow.com/questions/4088080/get-boolean-from-database-using-android-and-sqlite



        return test;
    }


}
