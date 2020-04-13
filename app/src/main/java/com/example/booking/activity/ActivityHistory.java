package com.example.booking.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.booking.R;
import com.example.booking.constant.Constant;
import com.example.booking.controller.adapter.AdapterHistory;
import com.example.booking.controller.adapter.AdapterHotel;
import com.example.booking.model.ItemHistory;
import com.example.booking.model.dbo.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityHistory extends AppCompatActivity {
    private static final int MESSAGE_LIST_HISTORY = 1;
    private RecyclerView listHistory;
    private Handler handler;
    private List<ItemHistory> itemHistoryList;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        init();
        initHandler();
        getListHistory(Constant.URL_GET_LIST_HISTORY_BY_CUSTOMER);
    }

    private void getListHistory(String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    List<ItemHistory> list = new ArrayList<>();
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i=0;i<jsonArray.length();i++){
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        list.add(new ItemHistory(
                                                jsonObject.getString("nameHotel"),
                                                jsonObject.getString("nameTypeRoom"),
                                                jsonObject.getString("codeRoom"),
                                                jsonObject.getString("timeRegister"),
                                                jsonObject.getString("timeGoRoom"),
                                                jsonObject.getInt("countRoom"),
                                                jsonObject.getInt("countPerson")
                                        ));
                                    }
                                    Message msg = new Message();
                                    msg.what = MESSAGE_LIST_HISTORY;
                                    msg.obj = list;
                                    handler.sendMessage(msg);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("idCustomer",String.valueOf(customer.getId()));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what){
                    case MESSAGE_LIST_HISTORY:{
                        itemHistoryList.addAll((Collection<? extends ItemHistory>) msg.obj);
                        customAdapterHistory(itemHistoryList);
                        break;
                    }
                }
            }
        };
    }

    private void customAdapterHistory(List<ItemHistory> list) {
        listHistory.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayout.getOrientation());
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.custom_divider);
        dividerItemDecoration.setDrawable(drawable);
        listHistory.setLayoutManager(linearLayout);
        listHistory.addItemDecoration(dividerItemDecoration);
        listHistory.setItemAnimator(new DefaultItemAnimator());
        AdapterHistory adapterHistory = new AdapterHistory(this, list);
        listHistory.setAdapter(adapterHistory);
    }

    private void init() {
        listHistory = findViewById(R.id.listHistory);
        itemHistoryList = new ArrayList<>();
        customer = (Customer) getIntent().getSerializableExtra("customer");
    }
}
