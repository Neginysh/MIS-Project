package com.example.lenovocom.lieortruth.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.lenovocom.lieortruth.entities.Question;

import java.util.ArrayList;
import java.util.List;

public class QuestionDataAccess {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public static final String TABLE_NAME = "question";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TEXT = "text";

    private String[] allColumns = {COLUMN_ID, COLUMN_TEXT};

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TEXT + " TEXT,"
                    + ")";

    public void QuestionDataAccess(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public Question Create(String text){
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, text);
        long insertId = database.insert(TABLE_NAME, null, values);
        Cursor cursor = database.query(TABLE_NAME, allColumns,COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Question question = cursorToQuestion(cursor);
        cursor.close();
        return question;
    }

    public void delete(Question question) {
        long id = question.getId();
        System.out.println("Question deleted with id: " + id);
        database.delete(TABLE_NAME, COLUMN_ID
                + " = " + id, null);
    }

    public List<Question> getAll() {
        List<Question> questions = new ArrayList<Question>();

        Cursor cursor = database.query(TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Question comment = cursorToQuestion(cursor);
            questions.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return questions;
    }

    public void update(Question question){
        long id = question.getId();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TEXT, question.getText());
        database.update(TABLE_NAME, values, COLUMN_ID + " = " + id, null);
    }

    private Question cursorToQuestion(Cursor cursor) {
        Question question = new Question();
        question.setId(cursor.getInt(0));
        question.setText(cursor.getString(1));
        return question;
    }


}
