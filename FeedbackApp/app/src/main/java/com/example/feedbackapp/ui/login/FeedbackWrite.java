package com.example.feedbackapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedbackapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FeedbackWrite extends AppCompatActivity implements View.OnClickListener {

    private EditText message;
    private Spinner dropdown;
    private ArrayAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_write);

        message = findViewById(R.id.message_input);
        dropdown = findViewById(R.id.spinner);

        final String[] items = new String[]{"Informatikk", "Dataingeniør", "Informasjonssystemer"};

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);

        dropdown.setAdapter(adapter);
    }

    private void sendMessage(){
        String url = "http://192.168.56.1/feedbacksystem/api/melding/create.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";

        final String userMessage = this.message.getText().toString().trim();

        String selection = dropdown.getSelectedItem().toString();
        String subjectID = null;
        String reply = "";
        String reported = "0";

        LoginActivity log = new LoginActivity();
        int userID = log.getUserID();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());

        String finalDate = formatter.format(date);
        String finalUserID = String.valueOf(userID);


        if(selection.equals("Informatikk")){
            subjectID = "1";
        }
        else if(selection.equals("Dataingeniør")){
            subjectID = "2";
        }
        else if (selection.equals("Informasjonssystemer")){
            subjectID = "3";
        }

        final JSONObject postData = new JSONObject();

        try {


            postData.put("Student_id", finalUserID);
            postData.put("Tekst", userMessage);
            postData.put("Opprettet", finalDate);
            postData.put("Emne_id", subjectID);
            postData.put("Rapportert", reported);
            postData.put("Svar", reply);

            Log.d("json", postData.toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String finalMessage = postData.toString();

        HttpPostRequest post = new HttpPostRequest();
        Thread thread = new Thread(post);

        post.setURL(url);
        post.setData(finalMessage);

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.write_return_btn) {
            Intent myIntent = new Intent(FeedbackWrite.this, HomeActivity.class);
            FeedbackWrite.this.startActivity(myIntent);
        }
        else if (view.getId() == R.id.send_msg_btn){
            sendMessage();
        }
    }
}
