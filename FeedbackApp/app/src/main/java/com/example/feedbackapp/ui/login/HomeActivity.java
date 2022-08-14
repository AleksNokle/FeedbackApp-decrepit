package com.example.feedbackapp.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.feedbackapp.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.home_logout_btn) {
            Intent myIntent = new Intent(HomeActivity.this, LoginActivity.class);
            HomeActivity.this.startActivity(myIntent);
        }
        else if(view.getId() == R.id.home_overview_btn) {
            Intent myIntent = new Intent(HomeActivity.this, FeedbackOverview.class);
            HomeActivity.this.startActivity(myIntent);
        }
        else if(view.getId() == R.id.home_write_btn) {
            Intent myIntent = new Intent(HomeActivity.this, FeedbackWrite.class);
            HomeActivity.this.startActivity(myIntent);
        }
    }
}
