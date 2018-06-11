package com.example.lenovocom.lieortruth;

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


public class MainActivity extends AppCompatActivity implements View.OnFocusChangeListener, TextWatcher {
    SwipeButton swipeButtonTruth;
    SwipeButton swipeButtonLie;
    TextView textView;
    EditText editText;
    Button button;

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
        findViewById(R.id.question_a).requestFocus();
        editText.clearFocus();
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
}
