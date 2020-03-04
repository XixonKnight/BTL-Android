package com.example.booking.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.booking.R;
import com.example.booking.constant.Constant;
import com.example.booking.controller.adapter.AdapterHotel;
import com.example.booking.controller.adapter.SlidePagerAdapter;
import com.example.booking.model.dbo.Area;
import com.example.booking.model.dbo.Categories;
import com.example.booking.model.dbo.Customer;
import com.example.booking.model.ItemSlide;
import com.example.booking.model.dbo.Hotel;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCustomer extends AppCompatActivity {
    private static final int MESSAGE_GET_INFO_CUSTOMER = 1;
    private static final int MESSAGE_GET_INFO_AREA = 2;
    private static final int MESSAGE_GET_INFO_CATEGORY_ROOM = 3;
    private static final int MESSAGE_GET_DATA_HOTEL = 4;
    private static final int MESSAGE_SELECT_HOTEL_BY_AREA = 5;
    private static final String TAG = "Message";

    private DrawerLayout drawerLayout;
    private Toolbar nav_bar;
    private ActionBarDrawerToggle toggle;
    private List<ItemSlide> itemSlides;
    private ViewPager slide_page;
    private TabLayout tab_slide;
    private NavigationView nav_page;
    private CircleImageView avatar_customer;
    private TextView name_customer, gmail_customer;
    private Customer customer;
    private Handler handler;
    private String gmail;
    private Spinner spinnerArea;
    private List<Area> areaList;
    private List<Hotel> hotelList = new ArrayList<>();
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
        init();
        initHandler();
        getInfoCustomer(Constant.URL_SELECT_INFO_CUSTOMER);
        getAllArea(Constant.URL_SELECT_ALL_AREA);
        getAllDataHotel(Constant.URL_SELECT_ALL_DATA_HOTEL);
        customPagerAdapter();
        eventNav();
        Log.d(TAG, "onCreate: ");
    }


    private void initHandler() {
        handler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MESSAGE_GET_INFO_CUSTOMER: {
                        customer = (Customer) msg.obj;
                        headerNav(customer);
                        break;
                    }
                    case MESSAGE_GET_INFO_AREA: {
                        areaList = new ArrayList<>();
                        areaList.addAll((Collection<? extends Area>) msg.obj);
                        List<String> listNameArea = new ArrayList<>();
                        areaList.stream().forEach(area -> {
                            listNameArea.add(area.getName());
                        });
                        customSpinnerArea(listNameArea);
                        break;
                    }
                    case MESSAGE_GET_DATA_HOTEL: {
                        hotelList.clear();
                        hotelList.addAll((Collection<? extends Hotel>) msg.obj);
                        customerAdapterHotel(hotelList);
                        break;
                    }
                    case MESSAGE_SELECT_HOTEL_BY_AREA: {
                        List<Hotel> hotelList = new ArrayList<>();
                        hotelList.addAll((Collection<? extends Hotel>) msg.obj);
                        customerAdapterHotel(hotelList);
                        break;
                    }
                }
            }
        };
    }

    private void customerAdapterHotel(List<Hotel> list) {
        recyclerView = findViewById(R.id.list_hotel);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, linearLayout.getOrientation());
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.custom_divider);
        dividerItemDecoration.setDrawable(drawable);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        AdapterHotel adapterHotel = new AdapterHotel(this, list);
        recyclerView.setAdapter(adapterHotel);
    }


    private void customSpinnerArea(List<String> list) {
        list.add(0, "Chọn khu vực");
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerArea.setAdapter(arrayAdapter);
        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1: {
                        getAllHotelByArea(Constant.ID_HANOI);
                        break;
                    }
                    case 2: {
                        getAllHotelByArea(Constant.ID_TP_HCM);
                        break;
                    }
                    case 3: {
                        getAllHotelByArea(Constant.ID_QUANG_NINH);
                        break;
                    }
                    case 4: {
                        getAllHotelByArea(Constant.ID_DA_NANG);
                        break;
                    }
                    case 5: {
                        getAllHotelByArea(Constant.ID_NHA_TRANG);
                        break;
                    }
                    case 6: {
                        getAllHotelByArea(Constant.ID_HUE);
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getAllHotelByArea(int idArea) {
        Thread thread = new Thread(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {
                List<Hotel> list = new ArrayList<>();
                hotelList.stream()
                        .filter(hotel -> hotel.getIdArea() == idArea)
                        .forEach(hotel -> {
                            list.add(hotel);
                        });
                Message msg = new Message();
                msg.what = MESSAGE_SELECT_HOTEL_BY_AREA;
                msg.obj = list;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }


    private void init() {
        drawerLayout = findViewById(R.id.layout_drawer);
        nav_bar = findViewById(R.id.nav_bar);
        slide_page = findViewById(R.id.slide_page);
        tab_slide = findViewById(R.id.tab_slide);
        nav_page = findViewById(R.id.nav_page);
        spinnerArea = findViewById(R.id.spinner_area);
        Intent intent = getIntent();
        gmail = intent.getStringExtra("gmail");
        itemSlides = new ArrayList<>();
        itemSlides.add(new ItemSlide(R.drawable.item_slide_page1));
        itemSlides.add(new ItemSlide(R.drawable.item_slide_page2));
        itemSlides.add(new ItemSlide(R.drawable.item_slide_page3));
        itemSlides.add(new ItemSlide(R.drawable.item_slide_page4));
        setSupportActionBar(nav_bar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toggle = new ActionBarDrawerToggle(ActivityCustomer.this, drawerLayout, nav_bar, R.string.openDrawer, R.string.closeDrawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    public void customPagerAdapter() {
        SlidePagerAdapter pagerAdapter = new SlidePagerAdapter(getApplicationContext(), itemSlides);
        slide_page.setAdapter(pagerAdapter);
        tab_slide.setupWithViewPager(slide_page, true);
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new ActivityCustomer.RunTimeSlide(), 4000, 6000);
    }

    class RunTimeSlide extends TimerTask {
        @Override
        public void run() {
            ActivityCustomer.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (slide_page.getCurrentItem() < itemSlides.size() - 1) {
                        slide_page.setCurrentItem(slide_page.getCurrentItem() + 1);
                    } else {
                        slide_page.setCurrentItem(0);
                    }
                }
            });
        }
    }

    public void headerNav(Customer customer) {
        View headerView = nav_page.getHeaderView(0);
        avatar_customer = headerView.findViewById(R.id.avatar_customer);
        name_customer = headerView.findViewById(R.id.name_customer);
        gmail_customer = headerView.findViewById(R.id.gmail_customer);
        name_customer.setText(customer.getCustomerName());
        gmail_customer.setText(customer.getGmail());
        if (customer.getAvatar().equals("null")) {
            avatar_customer.setImageResource(R.drawable.avatar_default);
        } else {
            Picasso.get().load(customer.getAvatar()).into(avatar_customer);
        }
    }

    private void eventNav() {
        nav_page.bringToFront();
        nav_page.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.log_out: {
                        finish();
                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                        Toast.makeText(ActivityCustomer.this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    case R.id.profile: {
                        Intent intent = new Intent(ActivityCustomer.this, ActivityInfoCustomer.class);
                        intent.putExtra("customer", customer);
                        startActivity(intent);
                        break;
                    }
                    case R.id.changePassword: {
                        Intent intent = new Intent(getApplicationContext(), ActivityChangePassword.class);
                        intent.putExtra("password", customer.getPassword());
                        intent.putExtra("gmail", customer.getGmail());
                        startActivity(intent);
                        break;
                    }
                }
                return true;
            }
        });
    }

    private void getInfoCustomer(String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(ActivityCustomer.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (!response.toString().isEmpty()) {
                                    Message msg = new Message();
                                    msg.what = MESSAGE_GET_INFO_CUSTOMER;
                                    msg.obj = setInfoCustomer(response.toString());
                                    handler.sendMessage(msg);
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
                        param.put("gmail", getNameGmail(gmail));
                        return super.getParams();
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void getAllArea(String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                List<Area> list = new ArrayList<>();
                RequestQueue requestQueue = Volley.newRequestQueue(ActivityCustomer.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONObject jsonObject = new JSONObject();

                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        jsonObject = response.getJSONObject(i);
                                        list.add(new Area(
                                                jsonObject.getInt("id"),
                                                jsonObject.getString("name")
                                        ));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                Message msg = new Message();
                                msg.what = MESSAGE_GET_INFO_AREA;
                                msg.obj = list;
                                handler.sendMessage(msg);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        }
                );
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread.start();
    }

    private void getAllDataHotel(String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(ActivityCustomer.this);
                JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                JSONObject jsonObject = new JSONObject();
                                List<Hotel> hotels = new ArrayList<>();
                                for (int i = 0; i < response.length(); i++) {
                                    try {
                                        jsonObject = response.getJSONObject(i);
                                        hotels.add(new Hotel(
                                                jsonObject.getInt("id"),
                                                jsonObject.getInt("idArea"),
                                                jsonObject.getString("name"),
                                                jsonObject.getString("address"),
                                                jsonObject.getString("image")
                                        ));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Message msg = new Message();
                                    msg.what = MESSAGE_GET_DATA_HOTEL;
                                    msg.obj = hotels;
                                    handler.sendMessage(msg);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ActivityCustomer.this, error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue.add(jsonArrayRequest);
            }
        });
        thread.start();
    }

    private String getNameGmail(String gmail) {
        String[] arr = gmail.split("\\@");
        return arr[0];
    }

    private Customer setInfoCustomer(String s) {
        SimpleDateFormat format = new SimpleDateFormat(Constant.FORMAT_DATE);
        Customer customerRequest = new Customer();
        try {
            JSONObject object = new JSONObject(s);
            customerRequest.setId(Integer.parseInt(object.getString("id")));
            customerRequest.setCustomerName(object.getString("customerName"));
            customerRequest.setPassword(object.getString("password"));
            customerRequest.setGmail(object.getString("gmail"));
            customerRequest.setAddress(object.getString("address"));
            if (object.getString("gender").equals("1")) {
                customerRequest.setGender(true);
            } else {
                customerRequest.setGender(false);
            }
            customerRequest.setAvatar(object.getString("avatar"));

            if (!object.getString("DOB").equals("null")) {
                customerRequest.setDOB(format.parse(object.getString("DOB")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return customerRequest;
    }

    @Override
    protected void onResume() {
        getInfoCustomer(Constant.URL_SELECT_INFO_CUSTOMER);
        Log.d(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause: ");
    }

    public void setClickRecyclerView(Hotel hotel) {
        Intent intent = new Intent(this, ActivityInfoHotel.class);
        intent.putExtra("hotel", hotel);
        startActivity(intent);
    }
}
