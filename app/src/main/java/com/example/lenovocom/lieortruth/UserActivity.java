package com.example.lenovocom.lieortruth;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lenovocom.lieortruth.dataAccess.UserDataAccess;
import com.example.lenovocom.lieortruth.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserActivity extends AppCompatActivity implements View.OnClickListener {

    EditText nickName, age;
    Button register;
    UserDataAccess userDataAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        userDataAccess = new UserDataAccess(this);
        checkUserInDB();
        init();
    }

    private void init() {
        nickName = (EditText) findViewById(R.id.nickname);
        age = (EditText) findViewById(R.id.age);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(this);

    }

    // check if the user exist in DB it does not bring the register page
    private void checkUserInDB() {
        List<User> users = new ArrayList<User>();
        userDataAccess.open();
        users = userDataAccess.getAll();
        userDataAccess.close();
        if (users.size() > 0){
            Intent myIntent = new Intent(UserActivity.this, MainActivity.class);
            UserActivity.this.startActivity(myIntent);

        }else setContentView(R.layout.activity_user);

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.register) {
            userDataAccess.open();
            userDataAccess.Create(nickName.getText().toString(), Integer.valueOf(age.getText().toString()));
            userDataAccess.close();
            Intent myIntent = new Intent(UserActivity.this, MainActivity.class);
            UserActivity.this.startActivity(myIntent);
        }
    }
}
