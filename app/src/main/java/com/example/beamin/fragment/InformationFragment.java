package com.example.beamin.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import com.example.beamin.HttpConnection;
import com.example.beamin.R;
import com.example.beamin.activity.MenuDetailActivity;
import com.example.beamin.activity.MenuListActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class InformationFragment extends Fragment {
    public ListView listView;

    private HttpConnection httpConn = HttpConnection.getInstance();
    private String name;
    private String min;
    private String payment;
    private String phone;
    private String time;
    private String delLocation;
    private String dayoff;
    private String restLocation;
    private int status;
    private TextView timeTv;
    private TextView dayoffTv;
    private TextView phoneTv;
    private TextView delLcationTv;
    private TextView restLocationTv;

    public InformationFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_information, container, false);

        listView = view.findViewById(R.id.menu_detail_listview);
        timeTv = view.findViewById(R.id.fragment_information_time);
        dayoffTv = view.findViewById(R.id.fragment_information_dayoff);
        phoneTv = view.findViewById(R.id.fragment_information_phone);
        delLcationTv = view.findViewById(R.id.fragment_information_del_location);
        restLocationTv = view.findViewById(R.id.fragment_information_rest_location);

        try {
            Log.d("@@@@@@@@@@", String.valueOf(MenuDetailActivity.key));
            httpConn.menuDetail(String.valueOf(MenuDetailActivity.key),1,callback);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
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
                name = arr.getJSONObject(0).getString("restaurantName");
                min = String.valueOf(arr.getJSONObject(0).getInt("minimum"));
                payment = arr.getJSONObject(0).getString("payment");
                phone = arr.getJSONObject(0).getString("phoneNumber");
                time = arr.getJSONObject(0).getString("businessTime");
                dayoff = arr.getJSONObject(0).getString("dayOff");
                delLocation = arr.getJSONObject(0).getString("deliveryLocation");
                restLocation = arr.getJSONObject(0).getString("restaurantLocation");
                //JSON Passing
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MenuListActivity.activity.runOnUiThread(new Runnable() {
                public void run() {
                    MenuDetailActivity.detailActivity.nameTv.setText(name);
                    MenuDetailActivity.detailActivity.minTv.setText(min+"원");
                    MenuDetailActivity.detailActivity.payHowTv.setText(payment);
                    timeTv.setText(time);
                    dayoffTv.setText(dayoff);
                    phoneTv.setText(phone);
                    delLcationTv.setText(delLocation);
                    restLocationTv.setText(restLocation);
                }
            });
            //Set MenuListActivity UI to use runOnUiThread for to use callback function
        }
    };
    //Menu Information Callback function
}
//MenuDetailList Fragment