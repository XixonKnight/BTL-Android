package com.example.booking.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.booking.R;
import com.example.booking.constant.Constant;
import com.example.booking.controller.adapter.AdapterComment;
import com.example.booking.controller.adapter.SlideHotelAdapter;
import com.example.booking.controller.adapter.SlideTypeRoomAdapter;
import com.example.booking.model.CommentOfCustomer;
import com.example.booking.model.TypeRoom;
import com.example.booking.model.dbo.Customer;
import com.example.booking.model.dbo.Hotel;
import com.example.booking.model.dbo.ImageRoom;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityInfoHotel extends AppCompatActivity {
    private static final int MESSAGE_SELECT_IMAGE_HOTEL = 1;
    private static final int MESSAGE_SELECT_TYPE_ROOM = 2;
    private static final int MESSAGE_GET_COMMENT = 3;
    private ViewPager viewPager;
    private RecyclerView recyclerView, listCmt;
    private Handler handler;
    //    private int idHotel;
    private Hotel hotel;
    private Customer customer;
    private ImageView iconAvt;
    private TabLayout tabLayout;
    private Button btnSend;
    private TextView ed_cmt;
    private List<ImageRoom> imageRoomList;
    private List<TypeRoom> typeRoomList;
    private TextView lblNameHotel, lblAddressHotel;
    private ProgressDialog progressDialog;
    private List<CommentOfCustomer> listComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_hotel);
        init();
        initHandler();
        initDialog();
        setupIconAvt(customer);
        setEventSendCmt();
        getImageHotel(Constant.URL_SELECT_IMAGE_HOTEL);
        getTypeRoom(Constant.URL_SELECT_TYPE_ROOM);
        getCommentByHotel(Constant.URL_GET_CMT_BY_HOTEL);
    }


    private void initDialog() {
        progressDialog = new ProgressDialog(ActivityInfoHotel.this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Gửi nhận xét");
        progressDialog.setMessage("Vui lòng chờ...");
    }

    private void setEventSendCmt() {
        btnSend.setOnClickListener(v -> {
            if (ed_cmt.getText().toString().isEmpty()) {
                Toast.makeText(this, "Mời nhập comment", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.show();
                sendComment(Constant.URL_INSERT_CMT);
            }
        });
    }

    private void sendComment(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.equals("Successfully")) {
                            Toast.makeText(ActivityInfoHotel.this, "Comment thành công", Toast.LENGTH_SHORT).show();
                            loadComment();
                            ed_cmt.setText("");
                        } else {
                            Toast.makeText(ActivityInfoHotel.this, "Comment thất bại", Toast.LENGTH_SHORT).show();
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
                Map<String, String> params = new HashMap<>();
                params.put("id_cus", String.valueOf(customer.getId()));
                params.put("id_hotel", String.valueOf(hotel.getIdHotel()));
                params.put("content", ed_cmt.getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void loadComment() {
        listComment.add(0,new CommentOfCustomer(customer.getAvatar(),ed_cmt.getText().toString()));
        customComment(listComment);
    }

    private void setupIconAvt(Customer customer) {
        if (customer.getAvatar() != null || customer.getAvatar().isEmpty()) {
            Picasso.get().load(customer.getAvatar()).into(iconAvt);
        }
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
                    case MESSAGE_GET_COMMENT: {
                        listComment = new ArrayList<>();
                        listComment.addAll((Collection<? extends CommentOfCustomer>) msg.obj);
                        customComment(listComment);
                        break;
                    }
                }
            }
        };

    }

    private void customComment(List<CommentOfCustomer> listComment) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(this,layoutManager.getOrientation());
        Drawable drawable =getDrawable(R.drawable.custom_divider);
        itemDecoration.setDrawable(drawable);
        listCmt.setLayoutManager(layoutManager);
        listCmt.addItemDecoration(itemDecoration);
        AdapterComment adapterComment = new AdapterComment(this,listComment);
        listCmt.setAdapter(adapterComment);
        listCmt.setHasFixedSize(true);
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
        listCmt = findViewById(R.id.list_cmt);
        ed_cmt = findViewById(R.id.ed_cmt);
        btnSend = findViewById(R.id.btn_send);
        iconAvt = findViewById(R.id.icon_avt);
        viewPager = findViewById(R.id.view_pager_hotel);
        tabLayout = findViewById(R.id.tab);
        recyclerView = findViewById(R.id.list_type_room);
        lblNameHotel = findViewById(R.id.lbl_name_hotel);
        lblAddressHotel = findViewById(R.id.lbl_address_hotel);
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");
        customer = (Customer) getIntent().getSerializableExtra("customer");
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

    public void setClickTypeRoom(TypeRoom typeRoom) {
        Intent intent = new Intent(getApplicationContext(), ActivityBookRoom.class);
        intent.putExtra("typeRoom", typeRoom);
        startActivity(intent);
    }

    private void getCommentByHotel(String mUrl) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, mUrl,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    List<CommentOfCustomer> list = new ArrayList<>();
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        list.add(new CommentOfCustomer(
                                                jsonObject.getString("image"),
                                                jsonObject.getString("cmt")
                                        ));
                                    }
                                    Message msg = new Message();
                                    msg.what = MESSAGE_GET_COMMENT;
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
                        param.put("idHotel", String.valueOf(hotel.getIdHotel()));
                        return param;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }
}
