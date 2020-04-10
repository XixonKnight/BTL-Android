package com.example.booking.activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import com.example.booking.model.CodeRoom;
import com.example.booking.model.TypeRoom;
import com.example.booking.model.dbo.Customer;
import com.example.booking.model.dbo.Hotel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityBookRoom extends AppCompatActivity {
    private static final int MESSAGE_GET_CODE_ROOM = 1;
    private SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.FORMAT_DATE);
    private Date timeNow = new Date();
    private Button btnPlus, btnLess, btnRegister, btnLessPerson, btnPlusPerson;
    private TextView lblNameTypeRoom, lblArc, lblPrice;
    private EditText countRoom, edCountPerson, edDateRegister;
    private ImageButton btnSetDateRegister;
    private int count = 1, countPerson = 1, idRoom = 0;
    private int price;
    private TypeRoom typeRoom;
    private Calendar calendar;
    private Spinner spinnerCodeRoom;
    private Hotel hotel;
    private Handler handler;
    private List<CodeRoom> codeRoomList;
    private ProgressDialog progressDialog;
    private Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);
        init();
        initDiaLog();
        initHandler();
        setInfoTypeRoom();
        getDataCodeRoom(Constant.URL_GET_CODE_ROOM);
        event();
    }

    private void initDiaLog() {
        progressDialog = new ProgressDialog(ActivityBookRoom.this);
        progressDialog.setContentView(R.layout.progress_dilog);
        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Vui lòng chờ...");
    }


    private void initHandler() {
        handler = new Handler() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MESSAGE_GET_CODE_ROOM: {
                        codeRoomList.addAll((Collection<? extends CodeRoom>) msg.obj);
                        setSpinnerCodeRoom(codeRoomList);
                        break;
                    }
                }
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setSpinnerCodeRoom(List<CodeRoom> list) {
        List<String> codeList = new ArrayList<>();
        codeList.addAll(getNameCodeRoom(list));
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, codeList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCodeRoom.setAdapter(adapter);
        spinnerCodeRoom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String codeRoom = parent.getItemAtPosition(position).toString();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getCode().equals(codeRoom)) {
                        idRoom = list.get(i).getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<String> getNameCodeRoom(List<CodeRoom> list) {
        List<String> listNameCode = new ArrayList<>();
        list.stream().forEach(codeRoom -> {
            listNameCode.add(codeRoom.getCode());
        });
        return listNameCode;
    }

    private void getDataCodeRoom(String url) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    List<CodeRoom> list = new ArrayList<>();
                                    JSONArray jsonArray = new JSONArray(response);
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        list.add(new CodeRoom(
                                                jsonObject.getInt("id"),
                                                jsonObject.getString("codeRoom")
                                        ));
                                    }
                                    Message msg = new Message();
                                    msg.what = MESSAGE_GET_CODE_ROOM;
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
                        Map<String, String> params = new HashMap<>();
                        params.put("idHotel", String.valueOf(hotel.getIdHotel()));
                        params.put("idCategory", String.valueOf(typeRoom.getId()));
                        return params;
                    }
                };
                requestQueue.add(stringRequest);
            }
        });
        thread.start();
    }

    private void setInfoTypeRoom() {
        lblNameTypeRoom.setText(typeRoom.getLblName());
        lblPrice.setText(String.valueOf(typeRoom.getPrice()));
        price = typeRoom.getPrice();
    }

    private void init() {
        timeNow.getTime();
        codeRoomList = new ArrayList<>();
        spinnerCodeRoom = findViewById(R.id.listCodeRoom);
        calendar = Calendar.getInstance();
        edDateRegister = findViewById(R.id.edDateRegister);
        btnSetDateRegister = findViewById(R.id.btnSetDateRegister);
        edCountPerson = findViewById(R.id.countPerson);
        btnLessPerson = findViewById(R.id.btnLessPerson);
        btnPlusPerson = findViewById(R.id.btnPlusPerson);
        btnLess = findViewById(R.id.btnLess);
        btnPlus = findViewById(R.id.btnPlus);
        btnRegister = findViewById(R.id.btnRegister);
        lblArc = findViewById(R.id.lblArc);
        lblNameTypeRoom = findViewById(R.id.nameTypeRoom);
        lblPrice = findViewById(R.id.price);
        countRoom = findViewById(R.id.countRoom);
        typeRoom = (TypeRoom) getIntent().getSerializableExtra("typeRoom");
        hotel = (Hotel) getIntent().getSerializableExtra("hotel");
        customer = (Customer) getIntent().getSerializableExtra("customer");
    }

    private void event() {
        eventChangeCountRoom();
        eventChangeCountPerson();
        eventSetDateRegister();
        btnRegister.setOnClickListener(v -> {
            progressDialog.show();
            eventBookRoom();
        });
    }

    private void eventBookRoom() {
        try {
            if (edDateRegister.getText().toString().isEmpty()) {
                progressDialog.dismiss();
                Toast.makeText(this, "Mời chọn ngày đến", Toast.LENGTH_SHORT).show();
            } else {
                Date bookRoom = dateFormat.parse(edDateRegister.getText().toString());
                boolean flag = timeNow.before(bookRoom);
                if (flag) {
                    bookRoom(Constant.URL_BOOK_ROOM);
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Nhập lại ngày đến", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void bookRoom(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        if (response.equals("successfully")) {
                            finish();
                            Toast.makeText(ActivityBookRoom.this, "Đặt thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(ActivityBookRoom.this, "Đặt không thành công", Toast.LENGTH_SHORT).show();
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
                params.put("idCus", String.valueOf(customer.getId()));
                params.put("idRoom", String.valueOf(idRoom));
                params.put("amountPerson", edCountPerson.getText().toString());
                params.put("amountRoom", countRoom.getText().toString());
                params.put("dateGo", edDateRegister.getText().toString());
                params.put("dateRegister", dateFormat.format(timeNow));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void eventSetDateRegister() {
        int yearNow = calendar.get(Calendar.YEAR);
        int monthOfYear = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        btnSetDateRegister.setOnClickListener(v -> {
            DatePickerDialog date = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            calendar.set(year, month, dayOfMonth);
                            SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.FORMAT_DATE);
                            edDateRegister.setText(dateFormat.format(calendar.getTime()));
                        }
                    }, yearNow, monthOfYear, day);
            date.show();
        });

    }


    private void eventChangeCountPerson() {
        btnLessPerson.setOnClickListener(v -> {
            countPerson--;
            if (countPerson <= 1) {
                countPerson = 1;
            }
            setTextCountPerson(String.valueOf(countPerson));
        });
        btnPlusPerson.setOnClickListener(v -> {
            countPerson++;
            setTextCountPerson(String.valueOf(countPerson));
        });
    }

    private void eventChangeCountRoom() {
        btnLess.setOnClickListener(v -> {
            count--;
            if (count <= 1) {
                count = 1;
            }
            int value = price * count;
            setText(String.valueOf(count), String.valueOf(value));
        });
        btnPlus.setOnClickListener(v -> {
            count++;
            if (count >= 5) {
                count = 5;
                Toast.makeText(this, "Số lượng đặt tối đa", Toast.LENGTH_SHORT).show();
            }
            int value = price * count;
            setText(String.valueOf(count), String.valueOf(value));
        });
    }

    private void setText(String valueRoom, String price) {
        countRoom.setText(valueRoom);
        lblPrice.setText(price);
    }

    private void setTextCountPerson(String amount) {
        edCountPerson.setText(amount);
    }
}
