package com.example.feedbackapp.ui.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.feedbackapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText name, lastname, email, password, cpassword, kull, studie;
    private Button btn_register;
    private static String URL_REGISTER = "http://192.168.56.1/feedbacksystem/api/bruker/create.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";
    private TextView results;
    public Context context = this;
    public String username;
    public int studentID;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        kull = findViewById(R.id.reg_kull);
        studie = findViewById(R.id.reg_studie);
        name = findViewById(R.id.reg_name);
        lastname = findViewById(R.id.reg_lastname);
        email = findViewById(R.id.reg_email);
        password = findViewById(R.id.reg_password);
        cpassword = findViewById(R.id.reg_cpassword);

    }


    private void registerUser() {

        String url = "http://192.168.56.1/feedbacksystem/api/bruker/create.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";

        Toast toast;
        final String Fornavn = this.name.getText().toString().trim();
        final String Etternavn = this.lastname.getText().toString().trim();
        final String Epost = this.email.getText().toString().trim();
        final String Passord = this.password.getText().toString().trim();
        final String cPassord = this.cpassword.getText().toString().trim();

        if(Fornavn.equals("") || Etternavn.equals("") || Epost.equals("") || Passord.equals("") || cPassord.equals("")){
            toast = Toast.makeText(context, "Alle felt må fylles ut.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }

        if(!Passord.equals(cPassord)){
            toast = Toast.makeText(context, "Passord må være likt i begge felt.", Toast.LENGTH_LONG);
            toast.show();
            return;
        }


        final JSONObject postData = new JSONObject();

        try {


            postData.put("Epost", Epost);
            postData.put("Fornavn", Fornavn);
            postData.put("Etternavn", Etternavn);
            postData.put("Passord", Passord);

            Log.d("json", postData.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String account = postData.toString();

        HttpPostRequest post = new HttpPostRequest();
        Thread thread = new Thread(post);

        post.setURL(url);
        post.setData(account);

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int value = post.getResponse();


        if (value == 406){
            toast = Toast.makeText(context, "Ugyldig info, prøv igjen.", Toast.LENGTH_LONG);
            toast.show();
        }
        else if (value == 201){
            toast = Toast.makeText(context, "Bruker var opprettet!", Toast.LENGTH_LONG);
            toast.show();
            username = Epost;
            registerStudent();
        }


    }

    private void registerStudent(){
        String brukerReadURL = "http://192.168.56.1/feedbacksystem/api/bruker/read.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";
        String studentCreateURL = "http://192.168.56.1/feedbacksystem/api/student/create.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";

        final String Kull = this.kull.getText().toString().trim();
        final String Studieretning = this.studie.getText().toString().trim();


        String result = null;

        HttpGetRequest getRequest = new HttpGetRequest();



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

                if (username.equals(epost)){
                    studentID = brukerValue;
                }


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalStudentID = String.valueOf(studentID);

        Log.d("finalStudentID", finalStudentID);


        final JSONObject postData2 = new JSONObject();

        try {


            postData2.put("Student_id", finalStudentID);
            postData2.put("Studieretning", Studieretning);
            postData2.put("Kull", Kull);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String student = postData2.toString();

        HttpPostRequest post = new HttpPostRequest();
        Thread thread = new Thread(post);

        post.setURL(studentCreateURL);
        post.setData(student);

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public Context getContext(){
        return context;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.reg_submit_btn) {
            registerUser();
        }
    }
}
