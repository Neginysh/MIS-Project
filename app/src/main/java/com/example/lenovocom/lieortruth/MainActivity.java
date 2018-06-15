package com.example.lenovocom.lieortruth;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, TextWatcher, SensorEventListener, View.OnClickListener, View.OnTouchListener {
    SwipeButton swipeButtonTruth;
    SwipeButton swipeButtonLie;
    TextView textView;
    EditText editText;
    Button button;

    Sensor sensor;
    SensorManager sensorManager;
    float sensorX, sensorY, sensorZ, sensorM;
    List<Float> listSensorX =  new ArrayList<>();
    List<Float>listSensorY = new ArrayList<>();
    List<Float>listSensorZ = new ArrayList<>();
    List<Float>listSensorM = new ArrayList<>();
    float avgSensorX, avgSensorY, avgSensorZ, avgSensorM;

    List<Float> btnPressureList = new ArrayList<>();

    double swipeButtonTruthDuration = 0;
    double swipeButtonLieDuration = 0;
    long etLastDown = 0;
    long etLastDuration = 0;


    DataBaseHandler dataBaseHandler;
    static final String DB_NAME = "featuresDB.sql";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        // To calculate the time typing the answer in edittext the time is considered from when the user tap on editttext(Focus) till the last change they make on edittext
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(this);

        button.setOnClickListener(this);
        button.setOnTouchListener(this);


    }


    void init() {
        swipeButtonTruth = (SwipeButton) findViewById(R.id.swipe_truth_a);
        swipeButtonLie = (SwipeButton) findViewById(R.id.swipe_lie_a);
        textView = (TextView) findViewById(R.id.question_a);
        editText = (EditText) findViewById(R.id.answer_a);
        button = (Button) findViewById(R.id.next);
        editText.clearFocus();

        swipeButtonTruth.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (active) swipeButtonLie.setEnabled(false);
                else swipeButtonLie.setEnabled(true);
                Toast.makeText(MainActivity.this, swipeButtonTruth.swipeDuration() + " " + active, Toast.LENGTH_SHORT).show();
                swipeButtonTruthDuration = swipeButtonTruth.swipeDuration();
            }
        });
        swipeButtonLie.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (active) swipeButtonTruth.setEnabled(false);
                else swipeButtonTruth.setEnabled(true);
                Toast.makeText(MainActivity.this, swipeButtonLie.swipeDuration() + " " + active, Toast.LENGTH_SHORT).show();
                swipeButtonLieDuration = swipeButtonLie.swipeDuration();
            }
        });

        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);

        dataBaseHandler = new DataBaseHandler(MainActivity.this, DB_NAME, null, 1);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        etLastDown = 0;                                     // if edidtext is focused again
        etLastDown = System.currentTimeMillis();
        Toast.makeText(MainActivity.this, etLastDown + "", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}
    @Override
    public void afterTextChanged(Editable s) {
        if (etLastDown == 0)
            etLastDown = System.currentTimeMillis();
        etLastDuration = System.currentTimeMillis() - etLastDown;
        Toast.makeText(MainActivity.this, etLastDuration + "", Toast.LENGTH_SHORT).show();

    }




    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorX = event.values[0];
        sensorY = event.values[1];
        sensorZ = event.values[2];
        sensorM = magnitude(event.values[0], event.values[1], event.values[2]);
//        Toast.makeText(this, "x :  " + sensorX + " y :  " + sensorY + " z :  " + sensorZ + " m :  " + sensorM, Toast.LENGTH_SHORT).show();
//        writeToFile(Double.toString(sensorX) ,MainActivity.this); // on internal storage???
        listSensorX.add(sensorX);
        listSensorY.add(sensorY);
        listSensorZ.add(sensorZ);
        listSensorM.add(sensorM);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private float magnitude(double x, double y, double z) {

        return (float) Math.sqrt(x * x + y * y + z * z);
    }

    private float average(List<Float> sensorDataList) {
        float temp = 0;
        if (sensorDataList == null)
            return 0;
        else {
            for (int i = 0; i < sensorDataList.size(); i++) {
                temp = temp + sensorDataList.get(i);
            }
            return temp / sensorDataList.size();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.next)
//            Toast.makeText(MainActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();

        sensorManager.unregisterListener(MainActivity.this);

        avgSensorX = average(listSensorX);
        avgSensorY = average(listSensorY);
        avgSensorZ = average(listSensorZ);
        avgSensorM = average(listSensorM);


        dataBaseHandler.insertToDB(avgSensorX,
                avgSensorY,
                avgSensorZ,
                avgSensorM,
                etLastDuration,
                swipeButtonTruthDuration,
                swipeButtonLieDuration);
    }


    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

//measure pressure on button when touched
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float pressure = 1000;
        if (v.getId() == R.id.next) {
            pressure = event.getPressure();
            Toast.makeText(this, pressure + "", Toast.LENGTH_SHORT).show();
            btnPressureList.add(pressure);
        }
        return false;
    }
}
