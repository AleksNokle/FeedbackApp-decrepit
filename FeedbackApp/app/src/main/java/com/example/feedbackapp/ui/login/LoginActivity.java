package com.example.feedbackapp.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.feedbackapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //private static final String USER_URL = "http://192.168.56.1/tilbakemeldingsystem/api/bruker/read.php";
    private EditText userBox;
    private EditText passBox;
    public static int userID;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        userBox = findViewById(R.id.username);
        passBox = findViewById(R.id.password);

    }



    private void loginSystem(){
        Toast toast;
        String userInput = this.userBox.getText().toString().trim();
        String passInput = this.passBox.getText().toString().trim();

        if (userInput.equals("") || passInput.equals("")){
            toast = Toast.makeText(this, "Begge feltene må fylles ut.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        String brukerLoginURL = "http://192.168.56.1/feedbacksystem/api/bruker/login.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";
        String brukerReadURL = "http://192.168.56.1/feedbacksystem/api/bruker/read.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";

        String result = null;

        HttpGetRequest getRequest = new HttpGetRequest();

        final JSONObject postData = new JSONObject();

        try {

            postData.put("Epost", userInput);
            postData.put("Passord", passInput);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String credentials = postData.toString();

        HttpPostRequest post = new HttpPostRequest();
        Thread thread = new Thread(post);

        post.setURL(brukerLoginURL);
        post.setData(credentials);

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int resp = post.getResponse();

        if (resp == 200){
            Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
            LoginActivity.this.startActivity(myIntent);
        }
        else{
            return;
        }


        try {
            result = getRequest.execute(brukerReadURL).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = jsonObject.getJSONArray("brukere");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject explrObject = jsonArray.getJSONObject(i);

                String bruker_id = explrObject.getString("Bruker_id");
                String epost = explrObject.getString("Epost");
                String fornavn = explrObject.getString("Fornavn");
                String etternavn = explrObject.getString("Etternavn");
                String passord = explrObject.getString("Passord");

                int brukerValue = Integer.parseInt(bruker_id);

                if (userInput.equals(epost)){
                    userID = brukerValue;
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



    }

    public int getUserID(){
        return userID;
    }


    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.regButton) {
            Intent myIntent = new Intent(LoginActivity.this, RegisterActivity.class);
            LoginActivity.this.startActivity(myIntent);
            //loginSystem();
        }
        else if(view.getId() == R.id.button2) {
            Intent myIntent = new Intent(LoginActivity.this, HomeActivity.class);
            LoginActivity.this.startActivity(myIntent);
        }
        else if(view.getId() == R.id.login) {
            loginSystem();
        }

    }

}