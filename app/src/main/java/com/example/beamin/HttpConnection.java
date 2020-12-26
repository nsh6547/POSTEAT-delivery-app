package com.example.beamin;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import java.net.URLEncoder;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpConnection {

    private OkHttpClient client;
    private static HttpConnection instance = new HttpConnection();

    public static HttpConnection getInstance() {
        return instance;
    }

    private HttpConnection() {
        this.client = new OkHttpClient();
    }

    public void signUp(String id, String pw, String name,String age_group, String gender, Callback callback) throws JSONException { //POST
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("175.121.94.182")
                .port(2555)
                .addPathSegment("signup")
                .build();
        JSONObject object = new JSONObject();

        object.put("userEmailId", id);
        object.put("userPw", pw);
        object.put("userNickname",name);
        object.put("user_age_group", age_group);
        object.put("user_gender", gender);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                object.toString()
        );


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void signUpPhoneSend(String phone, Callback callback) throws JSONException { //POST
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("hostURL")
                .addPathSegment("Segment")
                .build();
        JSONObject object = new JSONObject();

        object.put("phoneNumber", phone);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                object.toString()
        );


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void signUpPhoneReceive(String number,String phone, Callback callback) throws JSONException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("hostURL")
                .addPathSegment("Segment")
                .build();
        JSONObject object = new JSONObject();

        object.put("phoneNumber", phone);
        object.put("authNumber",number);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                object.toString()
        );


        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void login(String id, String pw,Callback callback) throws JSONException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("175.121.94.182")
                .port(2555)
                .addPathSegment("login")
                .build();
        JSONObject object = new JSONObject();

        object.put("userEmailId", id);
        object.put("userPw", pw);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                object.toString()
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void mapSearchList(String query,Callback callback){
        try {
            OkHttpClient client = new OkHttpClient();
            String input = URLEncoder.encode(query,"utf-8");
            String url = "https://dapi.kakao.com/v2/local/search/keyword.json?query="+input;
            Request request = new Request.Builder()
                    .addHeader("Authorization", "KakaoAK 88c39b0286d1283f27781198a6095bb5")
                    .url(url)
                    .build();
            client.newCall(request).enqueue(callback);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void menuList(String query, Callback callback) throws JSONException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("175.121.94.182")
                .port(2555)
                .addPathSegment("menuList")
                .build();
        JSONObject object = new JSONObject();

        object.put("category", query);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                object.toString()
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }

    public void menuDetail(String query,int a, Callback callback) throws JSONException {
        if(a == 1){
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host("175.121.94.182")
                    .port(2555)
                    .addPathSegment("menuDetail")
                    .build();
            JSONObject object = new JSONObject();
            Log.d("이게뭐람", query);
            object.put("restaurantNumber", query);
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    object.toString()
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        }else{
            HttpUrl url = new HttpUrl.Builder()
                    .scheme("http")
                    .host("175.121.94.182")
                    .port(2555)
                    .addPathSegment("menuDetail")
                    .addPathSegment("menu")
                    .build();
            JSONObject object = new JSONObject();
            Log.d("이게뭐람", query);
            object.put("restaurantNumber", query);
            RequestBody requestBody = RequestBody.create(
                    MediaType.parse("application/json; charset=utf-8"),
                    object.toString()
            );

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            client.newCall(request).enqueue(callback);
        }

    }
    public void ranking(Callback callback) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("175.121.94.182")
                .port(2555)
                .addPathSegment("ranking")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }
    public void emotion_result(String query1, String query2, Callback callback) throws JSONException {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("175.121.94.182")
                .port(2555)
                .addPathSegment("emotionresult")
                .build();
        JSONObject object = new JSONObject();

        object.put("category1", query1);
        object.put("category2", query2);
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"),
                object.toString()
        );

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);
    }
}