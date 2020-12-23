package com.example.beamin.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.beamin.R;
import com.example.beamin.activity.EmotionActivity;
import com.example.beamin.activity.MainActivity;
import com.example.beamin.activity.RankingActivity;

import java.util.ArrayList;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AutoScrollAdapter extends PagerAdapter {

    Context context;
    ArrayList<String> data;

    public AutoScrollAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.auto_viewpager,null);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("@@@@@@@@@@@", "this page was clicked : " + position);
                if(position == 0){
                    Intent intent = new Intent(MainActivity.context, EmotionActivity.class);
                    context.startActivity(intent);
                }else{
                    Intent intent = new Intent(MainActivity.context, RankingActivity.class);
                    context.startActivity(intent);
                }
            }
        });
        ImageView image_container = (ImageView) v.findViewById(R.id.image_container);
        Glide.with(context).load(data.get(position)).into(image_container);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
//MainActivity auto viewpager banner