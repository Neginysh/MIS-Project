package com.example.lenovocom.lieortruth;

import android.content.Context;

import com.example.lenovocom.lieortruth.dataAccess.AnswerFeatureDataAccess;
import com.example.lenovocom.lieortruth.dataAccess.UserDataAccess;
import com.example.lenovocom.lieortruth.entities.AnswerFeature;
import com.example.lenovocom.lieortruth.entities.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

public class TestResult extends JSONObject{

    // user and list of answer features are here turned to json format for posting to server

    private List<User> userInfo;
    private List<AnswerFeature> answerFeaturesList;
    JSONObject j = new JSONObject();

    AnswerFeatureDataAccess answerFeatureDataAccess;

    UserDataAccess userDataAccess;

    public TestResult(Context context) {
        userDataAccess = new UserDataAccess(context);
        answerFeatureDataAccess = new AnswerFeatureDataAccess(context);
        userDataAccess.open();
        this.userInfo = userDataAccess.getAll();
        userDataAccess.close();
        answerFeatureDataAccess.open();
        this.answerFeaturesList = answerFeatureDataAccess.getAll();
        answerFeatureDataAccess.close();

    }

    public String toJson() throws JSONException {


        final StringWriter sw = new StringWriter();
        final ObjectMapper mapper = new ObjectMapper();
        final StringWriter sw2 = new StringWriter();
        final ObjectMapper mapper2 = new ObjectMapper();

        try {
            mapper.writeValue(sw, answerFeaturesList);
            j.put("answerFeatures", sw.toString());

            mapper2.writeValue(sw2, userInfo);
            j.put("user", sw2.toString());

            sw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String stringJson = j.toString();


        return stringJson;
    }



}
