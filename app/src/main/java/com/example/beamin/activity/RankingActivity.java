package com.example.beamin.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.example.beamin.R;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView ranking_back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        ranking_back_button = findViewById(R.id.ranking_back_tv);
        ranking_back_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ranking_back_tv){
            finish();
        }
    }
}