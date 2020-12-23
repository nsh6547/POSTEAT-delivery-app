package com.example.beamin.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.beamin.R;

public class CustomProgress extends ProgressDialog {

    public CustomProgress(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_progress);
        ImageView progress_image = (ImageView)findViewById(R.id.progress_image);
        Glide.with(getContext()).load(R.raw.faceid).into(progress_image);
    }
}