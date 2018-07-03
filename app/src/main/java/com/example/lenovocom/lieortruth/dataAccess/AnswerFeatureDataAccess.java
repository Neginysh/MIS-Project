package com.example.lenovocom.lieortruth.dataAccess;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.example.lenovocom.lieortruth.entities.AnswerFeature;

import java.util.ArrayList;
import java.util.List;

public class AnswerFeatureDataAccess {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public static final String TABLE_NAME = "answer_features";
    public static final String COLUMN_ID = "id";
    public static final String TEST_ID = "testId";
    public static final String QUESTION_ID = "questionId";
    public static final String SENSOR_X = "avgSensorX";
    public static final String SENSOR_Y = "avgSensorY";
    public static final String SENSOR_Z = "avgSensorZ";
    public static final String SENSOR_M = "avgSensorM";
    public static final String ET_DURATION = "etDuration";
    public static final String SB_TRUTH_DURATION = "swipeButtonTruthDuration";
    public static final String SB_LIE_DURATION = "swipeButtonLieDuration";
    public static final String ANSWER_TIME = "answerTime";
    public static final String BUTTON_PRESSURE_MAX = "btnPressure";
    public static final String ET_ANSWER = "etAnswer";
    public static final String BUTTON_PRESSURE_AVG = "btnPressureListAvg";
    public static final String BUTTON_PRESSURE_NO = "btnPressureListNo";


    private String[] allColumns = {COLUMN_ID, TEST_ID, QUESTION_ID, SENSOR_X, SENSOR_Y, SENSOR_Z, SENSOR_M, ET_DURATION, SB_TRUTH_DURATION, SB_LIE_DURATION, ANSWER_TIME, BUTTON_PRESSURE_MAX, ET_ANSWER, BUTTON_PRESSURE_AVG, BUTTON_PRESSURE_NO};

    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + TEST_ID + " INT,"
                    + QUESTION_ID + " INT,"
                    + SENSOR_X + " REAL,"
                    + SENSOR_Y + " REAL,"
                    + SENSOR_Z + " REAL,"
                    + SENSOR_M + " REAL,"
                    + ET_DURATION + " REAL,"
                    + SB_TRUTH_DURATION + " REAL,"
                    + SB_LIE_DURATION + " REAL,"
                    + ANSWER_TIME + " REAL,"
                    + BUTTON_PRESSURE_MAX + " REAL,"
                    + ET_ANSWER + " TEXT,"
                    + BUTTON_PRESSURE_AVG + " REAL,"
                    + BUTTON_PRESSURE_NO + " INT"
                    + ")";

    public AnswerFeatureDataAccess(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close(){
        dbHelper.close();
    }

    public AnswerFeature Create(int testID, int questionId, double sensorX, double sensorY, double sensorZ, double sensorM,
                                long etDuration, double sbTruth, double sbLie, double answerTime, float btnPressure, String etAnswer, float btnPressureListAvg, int btnPressureListNo)
    {
        ContentValues values = new ContentValues();
        values.put(TEST_ID, testID);
        values.put(QUESTION_ID, questionId);
        values.put(SENSOR_X, sensorX);
        values.put(SENSOR_Y, sensorY);
        values.put(SENSOR_Z, sensorZ);
        values.put(SENSOR_M, sensorM);
        values.put(ET_DURATION, etDuration);
        values.put(SB_TRUTH_DURATION, sbTruth);
        values.put(SB_LIE_DURATION, sbLie);
        values.put(ANSWER_TIME, answerTime);
        values.put(BUTTON_PRESSURE_MAX, btnPressure);
        values.put(ET_ANSWER, etAnswer);
        values.put(BUTTON_PRESSURE_AVG, btnPressureListAvg);
        values.put(BUTTON_PRESSURE_NO, btnPressureListNo);

        long insertId = database.insertOrThrow(TABLE_NAME, null, values);
        Cursor cursor = database.query(TABLE_NAME, allColumns,COLUMN_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        AnswerFeature answerFeature = cursorToAnswerFeature(cursor);
        cursor.close();
        return answerFeature;
    }

    public void delete(AnswerFeature answerFeature) {
        int id = answerFeature.getId();
        System.out.println("Answer Feature deleted with id: " + id);
        database.delete(TABLE_NAME, COLUMN_ID
                + " = " + id, null);
    }

    public List<AnswerFeature> getAll() {
        List<AnswerFeature> answerFeatures = new ArrayList<AnswerFeature>();

        Cursor cursor = database.query(TABLE_NAME,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            AnswerFeature comment = cursorToAnswerFeature(cursor);
            answerFeatures.add(comment);
            cursor.moveToNext();
        }
        // make sure to close the cursor
        cursor.close();
        return answerFeatures;
    }

    public void update(AnswerFeature answerFeature){
        int id = answerFeature.getId();
        ContentValues values = new ContentValues();
        values.put(TEST_ID, answerFeature.getTestId());
        values.put(QUESTION_ID, answerFeature.getQuestionId());
        values.put(SENSOR_X, answerFeature.getAvgSensorX());
        values.put(SENSOR_Y, answerFeature.getAvgSensorY());
        values.put(SENSOR_Z, answerFeature.getAvgSensorZ());
        values.put(SENSOR_M, answerFeature.getAvgSensorM());
        values.put(ET_DURATION, answerFeature.getEtDuration());
        values.put(SB_TRUTH_DURATION, answerFeature.getSwipeButtonTruthDuration());
        values.put(SB_LIE_DURATION, answerFeature.getSwipeButtonLieDuration());
        values.put(ANSWER_TIME, answerFeature.getAnswerTime());
        values.put(BUTTON_PRESSURE_MAX, answerFeature.getBtnPressureMax());
        values.put(ET_ANSWER, answerFeature.getEtAnswer());
        values.put(BUTTON_PRESSURE_AVG, answerFeature.getBtnPressureListAvg());
        values.put(BUTTON_PRESSURE_NO, answerFeature.getEtAnswer());
        database.update(TABLE_NAME, values, COLUMN_ID + " = " + id, null);
    }


    private AnswerFeature cursorToAnswerFeature(Cursor cursor) {
        AnswerFeature answerFeature = new AnswerFeature();

        answerFeature.setId(cursor.getInt(0));
        answerFeature.setTestId(cursor.getInt(1));
        answerFeature.setQuestionId(cursor.getInt(2));
        answerFeature.setAvgSensorX(cursor.getDouble(3));
        answerFeature.setAvgSensorY(cursor.getDouble(4));
        answerFeature.setAvgSensorZ(cursor.getDouble(5));
        answerFeature.setAvgSensorM(cursor.getDouble(6));
        answerFeature.setEtDuration(cursor.getLong(7));
        answerFeature.setSwipeButtonTruthDuration(cursor.getDouble(8));
        answerFeature.setSwipeButtonLieDuration(cursor.getDouble(9));
        answerFeature.setAnswerTime(cursor.getDouble(10));
        answerFeature.setBtnPressureMax(cursor.getFloat(11));
        answerFeature.setEtAnswer(cursor.getString(12));
        answerFeature.setBtnPressureListAvg(cursor.getFloat(13));
        answerFeature.setBtnPressureListNo(cursor.getInt(14));

        return answerFeature;
    }


}
