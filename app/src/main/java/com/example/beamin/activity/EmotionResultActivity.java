package com.example.beamin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.beamin.HttpConnection;
import com.example.beamin.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class EmotionResultActivity extends AppCompatActivity implements View.OnClickListener{
    int key;
    Class<R.drawable> drawable = R.drawable.class;
    ImageView emotion_result_back_view, emotion_result_rating_aceept, emotion_result_image;
    String category1, category2;
    TextView rest_name_text;
    LinearLayout resultlayout;
    int restaurantNumber;
    private HttpConnection httpConn = HttpConnection.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion_result);
        Intent intent = getIntent();
        key = intent.getExtras().getInt("key");
        Log.d("@@@@@@@@@@", String.valueOf(key));
        switch (key){
            case 0:
                category1 = "chiness";
                category2 = "fastfood";
                break;
            case 1:
                category1 = "japanese";
                category2 = "pizza";
                break;
            case 2:
                category1 = "fastfood";
                category2 = "korean";
                break;
            case 3:
                category1 = "snack";
                category2 = "korean";
                break;
            case 4:
                category1 = "snack";
                category2 = "chicken";
                break;
            case 5:
                category1 = "soup";
                category2 = "korean";
                break;
            case 6:
                category1 = "soup";
                category2 = "chicken";
                break;
            case 7:
                category1 = "jokbo";
                category2 = "japanese";
                break;
        }
        resultlayout = findViewById(R.id.emotion_layout);
        resultlayout.setOnClickListener(this);

        emotion_result_image = findViewById(R.id.emotion_result_image);
        rest_name_text = findViewById(R.id.emotion_result_restaurant_name);

        emotion_result_back_view = findViewById(R.id.emotion_result_back_tv);
        emotion_result_back_view.setOnClickListener(this);

        emotion_result_rating_aceept = findViewById(R.id.emotion_result_accept_rating);
        emotion_result_rating_aceept.setOnClickListener(this);

        try {
            Log.d("쿼리문을 보자", category1 + category2);
            httpConn.emotion_result(category1, category2, callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.emotion_result_back_tv:
                finish();
                break;
            case R.id.emotion_result_accept_rating:
                Toast.makeText(getApplicationContext(), "별점 저장 완료!", Toast.LENGTH_LONG).show();
                break;
            case R.id.emotion_layout:
                Intent intent  = new Intent(getApplicationContext(), MenuDetailActivity.class);
                intent.putExtra("key",restaurantNumber);
                startActivity(intent);
                break;
        }
    }

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("Error", "콜백오류:"+e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            try {
                JSONArray arr = new JSONArray(response.body().string());
                final String rest_name = arr.getJSONObject(0).getString("restaurantName");
                restaurantNumber = arr.getJSONObject(0).getInt("restaurantNumber");
                Field field;
                field = drawable.getField("a" + restaurantNumber);
                final int imgurl = field.getInt(null);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        rest_name_text.setText(rest_name);
                        emotion_result_image.setImageResource(imgurl);
                    }
                });
            } catch (JSONException | NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    };
}