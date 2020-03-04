package com.example.booking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

public class ActivityCheckGmail extends AppCompatActivity {

    EditText ed_code;
    Button btn_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_gmail);
        init();
        event();
    }

    private void init() {
        ed_code = findViewById(R.id.ed_code);
        btn_check = findViewById(R.id.btn_check_gmail);
    }

    public void event() {
        Intent intent = getIntent();
        String customerName = intent.getStringExtra("customername");
        String gmail = intent.getStringExtra("gmail");
        String password = intent.getStringExtra("password");
        boolean gender = intent.getBooleanExtra("gender", false);
        int code_check = intent.getIntExtra("code_check", -1);
        btn_check.setOnClickListener(v -> {
            int code = Integer.parseInt(ed_code.getText().toString());
            if (code == code_check) {
                sendRequest(Constant.URL_CREATE_USER, customerName, gmail, password, gender);
                Toast.makeText(getApplicationContext(), "Tạo thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getApplicationContext(), "Kiểm tra lại mã code", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void sendRequest(String url, String customerName, String gmail, String password, boolean gender) {

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().equals("Successfully")) {
                            startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Thêm bị lỗi", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customername", customerName);
                params.put("gmail", gmail);
                params.put("password", password);
                params.put("gender",String.valueOf(gender));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ActivityCreateUser.class));
        finish();
    }
}
