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


public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, TextWatcher, SensorEventListener {
    SwipeButton swipeButtonTruth;
    SwipeButton swipeButtonLie;
    TextView textView;
    EditText editText;
    Button button;
    Sensor sensor;
    SensorManager sensorManager;
    double sensorX, sensorY, sensorZ, sensorM;

    long etLastDown = 0;
    long etLastDuration = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();

        swipeButtonTruth.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (active) swipeButtonLie.setEnabled(false);
                else swipeButtonLie.setEnabled(true);
                Toast.makeText(MainActivity.this, swipeButtonTruth.swipeDuration() + " " + active, Toast.LENGTH_SHORT).show();
            }
        });
        swipeButtonLie.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (active) swipeButtonTruth.setEnabled(false);
                else swipeButtonTruth.setEnabled(true);
                Toast.makeText(MainActivity.this, swipeButtonLie.swipeDuration() + " " + active, Toast.LENGTH_SHORT).show();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();
                sensorManager.unregisterListener(MainActivity.this);


            }
        });


        // To calculate the time typing the answer in edittext the time is considered from when the user tap on editttext(Focus) till the last change they make on edittext
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(this);




    }
    void bindViews() {
        swipeButtonTruth = (SwipeButton) findViewById(R.id.swipe_truth_a);
        swipeButtonLie = (SwipeButton) findViewById(R.id.swipe_lie_a);
        textView = (TextView) findViewById(R.id.question_a);
        editText = (EditText) findViewById(R.id.answer_a);
        button = (Button) findViewById(R.id.next_a);
        editText.clearFocus();

        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        etLastDown = 0;
        etLastDown = System.currentTimeMillis();
        Toast.makeText(MainActivity.this, etLastDown + "", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

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
        Toast.makeText(this, "x :  " + sensorX + " y :  " + sensorY + " z :  " + sensorZ + " m :  " + sensorM, Toast.LENGTH_SHORT).show();
//        writeToFile(Double.toString(sensorX) ,MainActivity.this); // on internal storage???
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    private double magnitude(double x, double y, double z) {

        return (double) Math.sqrt(x * x + y * y + z * z);
    }


//https://stackoverflow.com/questions/14376807/how-to-read-write-string-from-a-file-in-android
    // on internal storage???
//    private void writeToFile(String data,Context context) {
//        try {
//            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
//            outputStreamWriter.write(data);
//            outputStreamWriter.close();
//        }
//        catch (IOException e) {
//            Log.e("Exception", "File write failed: " + e.toString());
//        }
//    }
}
