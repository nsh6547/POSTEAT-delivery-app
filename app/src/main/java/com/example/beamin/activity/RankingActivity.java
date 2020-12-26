package com.example.beamin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.beamin.HttpConnection;
import com.example.beamin.R;
import com.example.beamin.data.RankingData;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class RankingActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ranking_back_button;
    private TextView ranking_first, ranking_second, ranking_third, ranking_forth, ranking_fiveth;
    private HttpConnection httpConn = HttpConnection.getInstance();
    private int key;
    final ArrayList<RankingData> rankingData_arr = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        httpConn.ranking(callback);

        ranking_back_button = findViewById(R.id.ranking_back_tv);
        ranking_back_button.setOnClickListener(this);

        ranking_first = findViewById(R.id.ranking_text_first);
        ranking_second = findViewById(R.id.ranking_text_second);
        ranking_third = findViewById(R.id.ranking_text_third);
        ranking_forth = findViewById(R.id.ranking_text_forth);
        ranking_fiveth = findViewById(R.id.ranking_text_fiveth);
        ranking_first.setOnClickListener(this);
        ranking_second.setOnClickListener(this);
        ranking_third.setOnClickListener(this);
        ranking_forth.setOnClickListener(this);
        ranking_fiveth.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ranking_back_tv:
                finish();
                break;
            case R.id.ranking_text_first:
                Intent intent  = new Intent(getApplicationContext(), MenuDetailActivity.class);
                intent.putExtra("key",rankingData_arr.get(0).getRestnumber());
                startActivity(intent);
                break;
            case R.id.ranking_text_second:
                Intent intent1  = new Intent(getApplicationContext(), MenuDetailActivity.class);
                intent1.putExtra("key",rankingData_arr.get(1).getRestnumber());
                startActivity(intent1);
                break;
            case R.id.ranking_text_third:
                Intent intent2  = new Intent(getApplicationContext(), MenuDetailActivity.class);
                intent2.putExtra("key",rankingData_arr.get(2).getRestnumber());
                startActivity(intent2);
                break;
            case R.id.ranking_text_forth:
                Intent intent3  = new Intent(getApplicationContext(), MenuDetailActivity.class);
                intent3.putExtra("key",rankingData_arr.get(3).getRestnumber());
                startActivity(intent3);
                break;
            case R.id.ranking_text_fiveth:
                Intent intent4  = new Intent(getApplicationContext(), MenuDetailActivity.class);
                intent4.putExtra("key",rankingData_arr.get(4).getRestnumber());
                startActivity(intent4);
                break;

        }
    }

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("Error", "콜백 오류 : 랭킹 불러오기 실패" + e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                JSONArray arr = new JSONArray(response.body().string());
                for(int i = 0; i < arr.length(); i++){
                    String name = arr.getJSONObject(i).getString("restaurantName");
                    key = arr.getJSONObject(i).getInt("restaurantNumber");
                    RankingData rankingData = new RankingData(name, key);
                    Log.d("@@@@@@@@@@@@", rankingData.getRestname());
                    Log.d("@@@@@@@@@@@@", String.valueOf(rankingData.getRestnumber()));
                    Log.d("@@@@@@@@@@@@", String.valueOf(i));
                    rankingData_arr.add(rankingData);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ranking_first.setText(rankingData_arr.get(0).getRestname());
                    ranking_second.setText(rankingData_arr.get(1).getRestname());
                    ranking_third.setText(rankingData_arr.get(2).getRestname());
                    ranking_forth.setText(rankingData_arr.get(3).getRestname());
                    ranking_fiveth.setText(rankingData_arr.get(4).getRestname());
                }
            });
        }
    };
}