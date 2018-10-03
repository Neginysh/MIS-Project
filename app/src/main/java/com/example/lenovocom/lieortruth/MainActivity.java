package com.example.lenovocom.lieortruth;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovocom.lieortruth.dataAccess.AnswerFeatureDataAccess;
import com.example.lenovocom.lieortruth.dataAccess.QuestionDataAccess;
import com.example.lenovocom.lieortruth.dataAccess.TestDataAccess;
import com.example.lenovocom.lieortruth.entities.Question;
import com.example.lenovocom.lieortruth.entities.Test;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, TextWatcher, SensorEventListener, View.OnClickListener, View.OnTouchListener {

    SwipeButton swipeButtonTruth;
    SwipeButton swipeButtonLie;
    TextView textView;
    EditText editText;
    Button button;
    TextView lieCounterView;
    TextView truthCounterView;
    Sensor sensor;
    SensorManager sensorManager;
    float sensorX, sensorY, sensorZ, sensorM;
    List<Float> listSensorX = new ArrayList<>();
    List<Float> listSensorY = new ArrayList<>();
    List<Float> listSensorZ = new ArrayList<>();
    List<Float> listSensorM = new ArrayList<>();
    float avgSensorX, avgSensorY, avgSensorZ, avgSensorM;
    double swipeButtonTruthDuration = 0;
    double swipeButtonLieDuration = 0;
    long etLastDown = 0;
    long etLastDuration = 0;
    long answerTimeLastDown = 0;
    double answerTime = 0;
    String etAnswer = "";
    float pressure = 0;
    List<Float> btnPressureList = new ArrayList<>();
    int btnPressureListNo = 0;
    float btnPressureListMax = 0;
    float btnPressureListAvg = 0;
    int isTruth = 1;
    int lieCounter = 0;
    int truthCounter = 0;


    List<Question> questions;
    QuestionDataAccess questionDataAccess;
    int currentQuestionNo = 0;

    TestDataAccess testDataAccess;
    Test test;

    AnswerFeatureDataAccess answerFeatureDataAccess = new AnswerFeatureDataAccess(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        questionsInit();

        // To calculate the time typing the answer in edittext the time is considered from when the user tap on editttext(Focus) till the last change they make on edittext
        editText.setOnFocusChangeListener(this);
        editText.addTextChangedListener(this);

        button.setOnClickListener(this);
        button.setOnTouchListener(this);


    }


    void init() {

        int swTCounter = 0;
        int swLCounter = 0;


        swipeButtonTruth = (SwipeButton) findViewById(R.id.swipe_truth_a);
        swipeButtonLie = (SwipeButton) findViewById(R.id.swipe_lie_a);
        textView = (TextView) findViewById(R.id.question_a);
        editText = (EditText) findViewById(R.id.answer_a);
        button = (Button) findViewById(R.id.next);
        lieCounterView = (TextView) findViewById(R.id.lieCounterView);
        truthCounterView = (TextView) findViewById(R.id.truthCounterView);

        editText.clearFocus();

        swipeButtonTruth.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {
                if (active){
                    truthCounter++;
                    swipeButtonLie.setEnabled(false);
                }
                else swipeButtonLie.setEnabled(true);
//                Toast.makeText(MainActivity.this, swipeButtonTruth.swipeDuration() + " " + active, Toast.LENGTH_SHORT).show();
                swipeButtonLieDuration = 0;
                swipeButtonTruthDuration = swipeButtonTruth.swipeDuration();
                isTruth = 1;
                truthCounterView.setText(String.valueOf(truthCounter));

            }
        });
        swipeButtonLie.setOnStateChangeListener(new OnStateChangeListener() {
            @Override
            public void onStateChange(boolean active) {

                if (active) {
                    lieCounter++;
                    swipeButtonTruth.setEnabled(false);
                }
                else swipeButtonTruth.setEnabled(true);
//                Toast.makeText(MainActivity.this, swipeButtonLie.swipeDuration() + " " + active, Toast.LENGTH_SHORT).show();
                swipeButtonTruthDuration = 0;
                swipeButtonLieDuration = swipeButtonLie.swipeDuration();
                isTruth = 0;
                lieCounterView.setText(String.valueOf(lieCounter));
            }
        });


        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        etLastDown = 0;                                     // if editText is focused again the measured time is reset
        etLastDown = System.currentTimeMillis();
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
//        Toast.makeText(MainActivity.this, etLastDuration + "", Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, String.valueOf(s.length()), Toast.LENGTH_SHORT).show();
        if (s.length()>0) {
           etLastDuration = etLastDuration / s.length();    // the spending time on editText is divided by the number of characters
        }
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        sensorX = event.values[0];
        sensorY = event.values[1];
        sensorZ = event.values[2];
        sensorM = magnitude(event.values[0], event.values[1], event.values[2]);
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

    private float average(List<Float> dataList) {
        float temp = 0;
        if (dataList == null)
            return 0;
        else {
            for (int i = 0; i < dataList.size(); i++) {
                temp = temp + dataList.get(i);
            }
            return temp / dataList.size();
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.next) {
//            Toast.makeText(MainActivity.this, editText.getText().toString(), Toast.LENGTH_SHORT).show();

            sensorManager.unregisterListener(MainActivity.this);

            avgSensorX = average(listSensorX);
            avgSensorY = average(listSensorY);
            avgSensorZ = average(listSensorZ);
            avgSensorM = average(listSensorM);

            etAnswer = editText.getText().toString();

            answerTime = System.currentTimeMillis() - answerTimeLastDown;

            // store features of each question on DB table
            answerFeatureDataAccess.open();
            answerFeatureDataAccess.Create(test.getId(), questions.get(currentQuestionNo).getId(), avgSensorX, avgSensorY, avgSensorZ, avgSensorM, etLastDuration, swipeButtonTruthDuration, swipeButtonLieDuration, answerTime, btnPressureListMax, etAnswer, btnPressureListAvg, btnPressureListNo, isTruth);
            answerFeatureDataAccess.close();

            fetchNextQuestion();
        }


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
        if (v.getId() == R.id.next) {
            pressure = event.getPressure();
//            Toast.makeText(this, pressure + "", Toast.LENGTH_SHORT).show();
            btnPressureList.add(pressure);
            btnPressureListNo = btnPressureList.size();
            btnPressureListMax = Collections.max(btnPressureList);
            btnPressureListAvg = average(btnPressureList);
        }
        return false;
    }


    protected void questionsInit() {
        testDataAccess = new TestDataAccess(this);
        testDataAccess.open();
        test = testDataAccess.Create(Calendar.getInstance().getTime().toString(), false);
        testDataAccess.close();

        questionDataAccess = new QuestionDataAccess(this);
        questions = new ArrayList<Question>();
        questionDataAccess.open();
        questions = questionDataAccess.getAll();
        questionDataAccess.close();
        Collections.shuffle(questions);
        showQuestion();

    }

    private void showQuestion() {
        textView.setText(questions.get(currentQuestionNo).getText());
        resetFormStats();

    }

    private void resetFormStats() {
        avgSensorX = 0;
        avgSensorY = 0;
        avgSensorZ = 0;
        avgSensorM = 0;
        swipeButtonTruthDuration = 0;
        swipeButtonLieDuration = 0;
        etLastDown = 0;
        etLastDuration = 0;
        etAnswer = "";
        answerTime = 0;
        pressure = 0;
        btnPressureList = new ArrayList<>();
        btnPressureListNo = 0;
        btnPressureListMax = 0;
        btnPressureListAvg = 0;
        editText.clearFocus();
        editText.setText("");

        swipeButtonTruth.collapseButton(); //make initial state of swipe button disabled
        swipeButtonLie.collapseButton();
        isTruth = 1;
        answerTimeLastDown = System.currentTimeMillis();


    }

    private void fetchNextQuestion() {

        if (currentQuestionNo < questions.size() - 1) {
            currentQuestionNo = currentQuestionNo + 1;
            sensorManager.registerListener(this, sensor, sensorManager.SENSOR_DELAY_NORMAL);
        }

        else {
            TestResult testResult = new TestResult(this); // to make a json out of the features list to post to server
            PostToServer server = new PostToServer();
            try {
                server.Post_JSON(testResult.toJson());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Intent myIntent = new Intent(MainActivity.this, FinishActivity.class);
            MainActivity.this.startActivity(myIntent);
        }
        showQuestion();
    }


}
