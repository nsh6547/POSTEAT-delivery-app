package com.example.beamin.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.beamin.HttpConnection;
import com.example.beamin.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView backView;
    private EditText nickName;
    private EditText id;
    private EditText pw;
    private Button signupBt;
    private HttpConnection httpConn = HttpConnection.getInstance();
    public Activity activity;
    private String tmp;
    private Spinner spinner_age_group, spinner_gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        final String[] age_group = {"-연령대 선택-", "10대", "20대", "30대", "40대", "50대"};
        final String[] gender = {"-성별 선택-", "남성", "여성"};

        backView = findViewById(R.id.signup_back_tv);
        nickName = findViewById(R.id.signup_nickname_et);
        id = findViewById(R.id.signup_id_et);
        pw = findViewById(R.id.signup_pw_et);
        signupBt = findViewById(R.id.signup_finish_bt);
        spinner_age_group = (Spinner)findViewById(R.id.signup_age_group);
        spinner_gender = (Spinner)findViewById(R.id.signup_gender);

        ArrayAdapter adapter_age_group = new ArrayAdapter(this, android.R.layout.simple_spinner_item, age_group);
        spinner_age_group.setAdapter(adapter_age_group);
        ArrayAdapter adapter_gender = new ArrayAdapter(this, android.R.layout.simple_spinner_item, gender);
        spinner_gender.setAdapter(adapter_gender);


        backView.setOnClickListener(this);
        signupBt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.signup_back_tv){
            finish();
        }
        else if(v.getId() == R.id.signup_finish_bt){
           sendData();
            //Synchronous action
        }
    }

    private void sendData() {
        new Thread() {
            public void run() {
                try {
                    httpConn.signUp(id.getText().toString(),pw.getText().toString(),nickName.getText().toString(), spinner_age_group.getSelectedItem().toString(), spinner_gender.getSelectedItem().toString(), callback);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }.start();
        //use thread for Asynchronous action
    }

    private final Callback callback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.d("Error", "콜백오류:"+e.getMessage());
        }
        @Override
        public void onResponse(Call call, Response response) throws IOException {

            try {
                final JSONObject jsonObject = new JSONObject(response.body().string());

                if (jsonObject.getInt("code") == 200) {
                    SignupPhoneActivity.signupPhoneActivity.finish();
                    finish();
                }
                else{
                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                tmp = jsonObject.getString("message");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(getApplicationContext(), tmp, Toast.LENGTH_SHORT).show();
                        }
                    });
                    //Use handler because callback function is Asynchronous function.
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            //JSON Passing
        }
    };
    //callbak function of signup action
}