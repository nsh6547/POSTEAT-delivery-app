package com.example.beamin.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.beamin.JSONParser;
import com.example.beamin.R;
import com.example.beamin.fragment.CustomProgress;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class EmotionActivity extends AppCompatActivity implements View.OnClickListener{
    ImageView emotion_back_view;
    ImageView imageView, upload_img_button, image_upload_button;
    String imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emotion);



        imageView = findViewById(R.id.emotionimage);

        image_upload_button = findViewById(R.id.emotion_button);

        emotion_back_view = findViewById(R.id.emotion_back_tv);
        emotion_back_view.setOnClickListener(this);

        upload_img_button = findViewById(R.id.emotion_picture_upload_imageview);
        upload_img_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageUploadTask().execute(imagePath);
            }
        });
        image_upload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getGallery();
            }
        });
//        image_upload_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ProgressDialog progressDialog;
//                progressDialog = new CustomProgress(EmotionActivity.this);
//                progressDialog.setCancelable(false);
//                progressDialog.setCanceledOnTouchOutside(false);
//                progressDialog.setMessage("\n이미지 업로드중....");
//                progressDialog.show();
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.emotion_back_tv){
            finish();
        }
    }
    private  class ImageUploadTask extends AsyncTask<String, Integer, Boolean> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new CustomProgress(EmotionActivity.this);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setMessage("\n이미지 업로드중....");
            progressDialog.show();
        }

        @Override
        protected Boolean doInBackground(String... params) {

            try {
                JSONObject jsonObject = JSONParser.uploadImage(params[0]);
                if (jsonObject != null)
                    return jsonObject.getString("result").equals("success");

            } catch (JSONException e) {
                Log.i("TAG", "Error : " + e.getLocalizedMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (progressDialog != null)
                progressDialog.dismiss();

            if (aBoolean)
                Toast.makeText(getApplicationContext(), "파일 업로드 성공", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "파일 업로드 실패", Toast.LENGTH_LONG).show();

            imagePath = "";
        }
    }

    // 사진 선택을 위해 갤러리를 호출
    private void getGallery() {
        // File System.
        final Intent galleryIntent = new Intent();
        galleryIntent.setType("image/*");
        galleryIntent.setAction(Intent.ACTION_PICK);

        // Chooser of file system options.
        final Intent chooserIntent = Intent.createChooser(galleryIntent, "사진을 선택하세요.");
        startActivityForResult(chooserIntent, 1010);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // URI 정보를 이용하여 사진 정보를 가져온다.
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 1010) {
            if (data == null) {
                Snackbar.make(findViewById(R.id.emotionView), "Unable to Pickup Image", Snackbar.LENGTH_INDEFINITE).show();
                return;
            }
            Uri selectedImageUri = data.getData();
            imageView.setImageURI(selectedImageUri);
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImageUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imagePath = cursor.getString(columnIndex);
                Toast.makeText(getApplicationContext(), imagePath, Toast.LENGTH_LONG).show();

//                Picasso.with(getApplicationContext()).load(new File(imagePath))
//                        .into(imageView); // 피카소 라이브러를 이용하여 선택한 이미지를 imageView에  전달.
                cursor.close();

            } else {
                Snackbar.make(findViewById(R.id.emotionView), "Unable to Load Image", Snackbar.LENGTH_LONG).setAction("Try Again", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getGallery();
                    }
                }).show();
            }
        }
    }
}