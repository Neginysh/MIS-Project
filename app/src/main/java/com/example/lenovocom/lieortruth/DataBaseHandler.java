package com.example.lenovocom.lieortruth;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataBaseHandler extends SQLiteOpenHelper {

    String makeTable =
            "CREATE TABLE features ( " +
                    " _id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " avgSensorX REAL," +
                    " avgSensorY REAL, " +
                    " avgSensorZ REAL, " +
                    " avgSensorM REAL, " +
                    " etLastDuration REAL, " +
                    " swipeButtonTruthDuration REAL, " +
                    " swipeButtonLieDuration REAL" +
                    ")";

    public DataBaseHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(makeTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insertToDB (double avgSensorX,
                            double avgSensorY,
                            double avgSensorZ ,
                            double avgSensorM,
                            long etLastDuration,
                            double swipeButtonTruthDuration,
                            double swipeButtonLieDuration){

        SQLiteDatabase db = this.getWritableDatabase();
        String insertQuery =
                " INSERT INTO features (avgSensorX, avgSensorY,avgSensorZ, avgSensorM, etLastDuration, swipeButtonTruthDuration, swipeButtonLieDuration)"
                + " VALUES( '"+avgSensorX+"' , '"+avgSensorY+"' , '"+avgSensorZ+"' , '"+avgSensorM+"' , '"+etLastDuration+"', '"+swipeButtonTruthDuration+"' , '"+swipeButtonLieDuration+"')";
        db.execSQL(insertQuery);
        db.close();
    }
}
