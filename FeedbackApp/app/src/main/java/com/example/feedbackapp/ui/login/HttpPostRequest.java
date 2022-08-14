package com.example.feedbackapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostRequest implements Runnable {

    public int response;
    private String url;
    private String data;

    public void setURL(String url){
        this.url = url;
    }

    public void setData(String data){
        this.data = data;
    }


    @Override
    public void run() {
        try {

            URL finalURL = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) finalURL.openConnection();

            connection.setRequestMethod("POST");

            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setRequestProperty("Accept", "application/json");

            connection.setDoOutput(true);
            connection.setDoInput(true);


            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
            writer.write(data);
            writer.flush();
            writer.close();

            Log.e("Response", connection.getResponseMessage() + "");

            response = connection.getResponseCode();

        } catch (Exception e) {
            Log.e(e.toString(), "Something with request");
        }

    }


    public int getResponse(){
        return response;
    }


}
