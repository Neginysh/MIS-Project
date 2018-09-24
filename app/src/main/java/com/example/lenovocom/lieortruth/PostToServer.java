package com.example.lenovocom.lieortruth;

import android.os.AsyncTask;

import org.apache.commons.io.IOUtils;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PostToServer {

//    public void postToServer(String answerString) {
//
//        AsyncHttpClient client = new AsyncHttpClient();
//        String url = "http://webislab16.medien.uni-weimar.de:5000/saveAnswers";
//
//
//        client.addHeader("Accept", "application/json");
//        StringEntity jsonParams = new StringEntity(answerString);
//
//        client.post(url, jsonParams, new TextHttpResponseHandler() {
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//
//            }
//
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, String responseString) {
//
//            }
//        });
////
////            @Override
////            public void onSuccess(int statusCode, Header[] headers, JSONObject obj) {
////                String jsonResponse = obj.toString();
////                Log.i("TAG", "onSuccess: " + jsonResponse);
////            }
////
////            @Override
////            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
////                Log.e("TAG", "onFailure: " + errorResponse);
////            }
//
//    }

    public void Post_JSON(String json) {
       new RetrieveFeedTask().execute(json);
    }



    // Reference: https://stackoverflow.com/questions/9671546/asynctask-android-example

    class RetrieveFeedTask extends AsyncTask<String, String, String> {

        private Exception exception;

        protected String doInBackground(String... params) {

            String query_url = "http://webislab16.medien.uni-weimar.de:5000/saveAnswers";
            String result = "";

            try {
                URL url = new URL(query_url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestMethod("POST");
                OutputStream os = conn.getOutputStream();
                os.write(params[0].getBytes("UTF-8"));
                os.close();
                // read the response
                InputStream in = new BufferedInputStream(conn.getInputStream());
                result = IOUtils.toString(in, "UTF-8");

                JSONObject myResponse = new JSONObject(result);
                in.close();
                conn.disconnect();
            } catch (Exception e) {
                System.out.println(e);
            }
            return result;
        }


        protected void onPostExecute(String feed) {
            // TODO: check this.exception
            // TODO: do something with the feed
        }
    }
}
