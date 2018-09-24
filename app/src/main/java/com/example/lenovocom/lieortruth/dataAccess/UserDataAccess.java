package com.example.lenovocom.lieortruth.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.lenovocom.lieortruth.UserActivity;
import com.example.lenovocom.lieortruth.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserDataAccess {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NICKNAME = "nickName";
    public static final String COLUMN_AGE = "age";





    private String[] allColumns = {COLUMN_ID, COLUMN_NICKNAME, COLUMN_AGE};

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_NICKNAME + " TEXT,"
                    + COLUMN_AGE + " INT"
                    + ")";


    public UserDataAccess(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public User Create(String nickName, int age){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NICKNAME, nickName);
        values.put(COLUMN_AGE, age);
        long insertId = database.insert(TABLE_NAME, null, values);
        Cursor cursor = database.query(TABLE_NAME, allColumns,COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        User user = cursorToUser(cursor);
        cursor.close();
        return user;
    }

    public void delete(User user) {
        int id = user.getId();
        System.out.println("User deleted with id: " + id);
        database.delete(TABLE_NAME, COLUMN_ID
                + " = " + id, null);
    }

    public List<User> getAll() {
        List<User> users = new ArrayList<User>();

        Cursor cursor = database.query(TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User comment = cursorToUser(cursor);
            users.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return users;
    }

    public void update(User user){
        int id = user.getId();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NICKNAME, user.getNickName());
        values.put(COLUMN_AGE, user.getAge());
        database.update(TABLE_NAME, values, COLUMN_ID + " = " + id, null);
    }

    private User cursorToUser(Cursor cursor) {
        User user = new User();
        user.setId(cursor.getInt(0));
        user.setNickName(cursor.getString(1));
        user.setAge(cursor.getInt(1));
        return user;
    }


}
