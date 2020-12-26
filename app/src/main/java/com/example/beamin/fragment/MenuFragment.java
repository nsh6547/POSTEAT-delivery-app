package com.example.beamin.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.example.beamin.HttpConnection;
import com.example.beamin.R;
import com.example.beamin.activity.MenuDetailActivity;
import com.example.beamin.activity.MenuListActivity;
import com.example.beamin.adapter.MenuDetailListAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MenuFragment extends Fragment {
    public ListView listView;
    public MenuDetailListAdapter menuDetailListAdapter;
    private HttpConnection httpConn = HttpConnection.getInstance();
    private String name;
    private String min;
    private String pay;
    public static String phone;
    private String del;
    private String location;

    public MenuFragment() {
        menuDetailListAdapter = new MenuDetailListAdapter(getContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_fragment_menu, container, false);
        listView = view.findViewById(R.id.menu_detail_listview);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String tmpPrice = menuDetailListAdapter.itemList.get(position).price;
                    String tmpMenu = menuDetailListAdapter.itemList.get(position).menu;
            }
        });

        try {
            httpConn.menuDetail(String.valueOf(MenuDetailActivity.key), 0,callback);
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
                menuDetailListAdapter = new MenuDetailListAdapter(getContext());
                JSONArray arr = new JSONArray(response.body().string());
                name = arr.getJSONObject(0).getString("restaurantName");
                min = String.valueOf(arr.getJSONObject(0).getString("minimum"));
                pay = arr.getJSONObject(0).getString("payment");
                phone = arr.getJSONObject(0).getString("phoneNumber");
                del = arr.getJSONObject(0).getString("deliveryLocation");
                location = arr.getJSONObject(0).getString("restaurantLocation");
                for(int i=0;i<arr.length();i++){
                    //String img = arr.getJSONObject(i).getString("imageURL");
                    String menu = arr.getJSONObject(i).getString("menuName");
                    String price = String.valueOf(arr.getJSONObject(i).getInt("price"));
                    menuDetailListAdapter.addList(0, menu,price);
                }
                MenuListActivity.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        MenuDetailActivity.detailActivity.nameTv.setText(name);
                        MenuDetailActivity.detailActivity.minTv.setText(min+"원");
                        MenuDetailActivity.detailActivity.payHowTv.setText(pay);
                        menuDetailListAdapter.notifyDataSetChanged();
                        listView.setAdapter(menuDetailListAdapter);
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
}
//MenuDetailList menu Fragment