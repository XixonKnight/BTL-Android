package com.example.booking.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.booking.R;
import com.example.booking.constant.Constant;
import com.example.booking.controller.adapter.SlideHotelAdapter;
import com.example.booking.controller.adapter.SlideTypeRoomAdapter;
import com.example.booking.model.TypeRoom;
import com.example.booking.model.dbo.Hotel;
import com.example.booking.model.dbo.ImageRoom;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityInfoHotel extends AppCompatActivity {
    private static final int MESSAGE_SELECT_IMAGE_HOTEL = 1;
    private static final int MESSAGE_SELECT_TYPE_ROOM = 2;
    private ViewPager viewPager;
    private RecyclerView recyclerView;
    private Handler handler;
    //    private int idHotel;
    private Hotel hotel;
    private TabLayout tabLayout;
    private List<ImageRoom> imageRoomList;
    private List<TypeRoom> typeRoomList;
    private TextView lblNameHotel, lblAddressHotel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_hotel);
        init();
        initHandler();
        getImageHotel(Constant.URL_SELECT_IMAGE_HOTEL);
        getTypeRoom(Constant.URL_SELECT_TYPE_ROOM);
    }


    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MESSAGE_SELECT_IMAGE_HOTEL: {
                        imageRoomList = new ArrayList<>();
                        imageRoomList.addAll((Collection<? extends ImageRoom>) msg.obj);
                        customImageHotel(imageRoomList);
                        setupInfoHotel(hotel);
                        break;
                    }
                    case MESSAGE_SELECT_TYPE_ROOM: {
                        typeRoomList = new ArrayList<>();
                        typeRoomList.addAll((Collection<? extends TypeRoom>) msg.obj);
                        customTypeRoom(typeRoomList);
                        break;
                    }
                }
            }
        };

    }

    private void setupInfoHotel(Hotel hotel) {
        lblNameHotel.setText(hotel.getName());
        lblAddressHotel.setText(hotel.getAddress());
    }

    private void customTypeRoom(List<TypeRoom> list) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        DividerItemDecoration decoration = new DividerItemDecoration(this, layoutManager.getOrientation());
        Drawable drawable = getDrawable(R.drawable.custom_divider);
        decoration.setDrawable(drawable);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(decoration);
        SlideTypeRoomAdapter adapter = new SlideTypeRoomAdapter(this, list);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
    }


    private void customImageHotel(List<ImageRoom> list) {
        SlideHotelAdapter adapter = new SlideHotelAdapter(getApplicationContext(), list);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        lblAddressHotel.setVisibility(View.VISIBLE);
    }

    private void init() {
        viewPager = findViewById(R.id.view_pager_hotel);
        tabLayout = findViewById(R.id.tab);
        recyclerView = findViewById(R.id.list_type_room);
        lblNameHotel = findViewById(R.id.lbl_name_hotel);
        lblAddressHotel = findViewById(R.id.lbl_address_hotel);
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");
    }

    private void getImageHotel(String murl) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(ActivityInfoHotel.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, murl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                List<ImageRoom> list = new ArrayList<>();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(new ImageRoom(
                                                jsonArray.getJSONObject(i).getString("image")
                                        ));
                                        Message msg = new Message();
                                        msg.what = MESSAGE_SELECT_IMAGE_HOTEL;
                                        msg.obj = list;
                                        handler.sendMessage(msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("idHotel", String.valueOf(hotel.getIdHotel()));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void getTypeRoom(String murl) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, murl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                List<TypeRoom> list = new ArrayList<>();
                                try {
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        list.add(new TypeRoom(
                                                jsonArray.getJSONObject(i).getString("nameType"),
                                                jsonArray.getJSONObject(i).getInt("price")
                                        ));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Message msg = new Message();
                                msg.what = MESSAGE_SELECT_TYPE_ROOM;
                                msg.obj = list;
                                handler.sendMessage(msg);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> param = new HashMap<>();
                        param.put("idHotel", String.valueOf(hotel.getIdHotel()));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }
    public void setClickTypeRoom(TypeRoom typeRoom){
        Intent intent = new Intent(getApplicationContext(),ActivityBookRoom.class);
        intent.putExtra("typeRoom",typeRoom);
        startActivity(intent);
    }

}
