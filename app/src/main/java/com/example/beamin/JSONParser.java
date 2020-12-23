package com.example.beamin;

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JSONParser {
    public static JSONObject uploadImage(String sourceImageFile) {
        HttpUrl url = new HttpUrl.Builder()
                .scheme("http")
                .host("192.168.0.14")
                .port(2555)
                .addPathSegment("imgupload")
                .build();

        try {
            File sourceFile = new File(sourceImageFile);
            Log.d("TAG", "File...::::" + sourceFile + " : " + sourceFile.exists());
            final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
            String filename = sourceImageFile.substring(sourceImageFile.lastIndexOf("/")+1);

            // OKHTTP3
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("img", filename, RequestBody.create(MEDIA_TYPE_PNG, sourceFile))
                    .addFormDataPart("result", "photo_image")
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Response response = client.newCall(request).execute();
            String res = response.body().string();
            Log.e("TAG", "Error: " + res);
            return new JSONObject(res);

        } catch (UnknownHostException | UnsupportedEncodingException e) {
            Log.e("TAG", "Error: " + e.getLocalizedMessage());
        } catch (Exception e) {
            Log.e("TAG", "Other Error: " + e.getLocalizedMessage());
        }
        return null;
    }
}
