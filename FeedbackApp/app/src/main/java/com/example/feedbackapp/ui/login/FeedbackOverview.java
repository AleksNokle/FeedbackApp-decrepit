package com.example.feedbackapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedbackapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class FeedbackOverview extends AppCompatActivity implements View.OnClickListener {
    private TextView mTextViewResult;
    public ArrayList<Integer> meldingValues = new ArrayList<Integer>();

    public ArrayList<String> kommentarID = new ArrayList<String>();
    public ArrayList<String> kommentarTekst = new ArrayList<String>();
    public ArrayList<String> kommentarOpprettet = new ArrayList<String>();
    public ArrayList<Integer> kommentarMeldingID = new ArrayList<Integer>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feedback_overview);
        mTextViewResult = findViewById(R.id.textView6);

        getKommentarer();
        getMeldinger();

    }

    private void getKommentarer() {
        String myUrl = "http://192.168.56.1/feedbacksystem/api/kommentar/read.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";

        String result = null;

        HttpGetRequest getRequest = new HttpGetRequest();

        try {
            result = getRequest.execute(myUrl).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            JSONObject jsonObject = new JSONObject(result);

            JSONArray jsonArray = jsonObject.getJSONArray("kommentarer");
            for (int i = 0; i < jsonArray.length(); i++){
                JSONObject explrObject = jsonArray.getJSONObject(i);

                String kommentar_id = explrObject.getString("Kommentar_id");
                String tekst = explrObject.getString("Tekst");
                String opprettet = explrObject.getString("Opprettet");
                String melding_id = explrObject.getString("Melding_id");

                if (opprettet == "null"){
                    opprettet = "";
                }

                int meldingIdValue = Integer.parseInt(melding_id);

                kommentarID.add(kommentar_id);
                kommentarTekst.add(tekst);
                kommentarOpprettet.add(opprettet);
                kommentarMeldingID.add(meldingIdValue);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    private void getMeldinger() {
        String meldingURL = "http://192.168.56.1/feedbacksystem/api/melding/read.php?api=aa3744394aa3014641d6ae73cbc8937438d495f5ec9c567d49152376f846493d";

        String meldinger = null;

        HttpGetRequest getRequest = new HttpGetRequest();



        try {
            meldinger = getRequest.execute(meldingURL).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            JSONObject jsonObject1 = new JSONObject(meldinger);

            JSONArray jsonArray1 = jsonObject1.getJSONArray("meldinger");
            for (int i = 0; i < jsonArray1.length(); i++){
                JSONObject meldingerObject = jsonArray1.getJSONObject(i);

                String melding_id = meldingerObject.getString("Melding_id");
                String student_id = meldingerObject.getString("Student_id");
                String tekst = meldingerObject.getString("Tekst");
                String opprettet = meldingerObject.getString("Opprettet");
                String emne_id = meldingerObject.getString("Emne_id");
                String rapportert = meldingerObject.getString("Rapportert");
                String svar = meldingerObject.getString("Svar");

                if (svar.equals("null")){
                    svar = "";
                }

                int meldingInt = Integer.parseInt(melding_id);

                meldingValues.add(meldingInt);

                String m = "Melding #" + melding_id;
                SpannableString m1 = new SpannableString(m);
                m1.setSpan(new AbsoluteSizeSpan(64),0,m.length(),0);
                mTextViewResult.append(m1);
                mTextViewResult.append("\n\n");

                mTextViewResult.append(tekst + "\n" + "Emne ID: " + emne_id + "\n" + "Rapportert: " + rapportert + "\n" + "Svar: " + svar + "\n\n");

                String k = "Kommentarer";
                SpannableString k1 = new SpannableString(k);
                k1.setSpan(new AbsoluteSizeSpan(64), 0,k.length(),0);
                mTextViewResult.append("\t\t\t");
                mTextViewResult.append(k1);
                mTextViewResult.append("\n\n");


                for (int x = 0; x < kommentarTekst.size(); x++){
                    if (meldingInt == kommentarMeldingID.get(x)){
                        mTextViewResult.append("\t\t\t" + kommentarTekst.get(x) + "\n");
                    }

                }

                mTextViewResult.append("\n\n");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("msg", kommentarMeldingID.toString());
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.overview_return_btn) {
            Intent myIntent = new Intent(FeedbackOverview.this, HomeActivity.class);
            FeedbackOverview.this.startActivity(myIntent);
        }
    }
}
