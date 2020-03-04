package com.example.booking.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.booking.R;
import com.example.booking.apiSendEmail.SendGmail;
import com.example.booking.constant.Constant;
import com.example.booking.controller.RandomCode;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityForgotPsw extends AppCompatActivity {
    private TextInputLayout layoutGmail;
    private Button btnForgot;
    private ProgressDialog progressDialog;
    private String nameGmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_psw);
        init();
        initDialog();
        event();
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Đang kiểm tra");
        progressDialog.setMessage("Vui lòng đợi...");
    }


    private void event() {
        btnForgot.setOnClickListener(v -> {
            if (checkGmail()) {
                progressDialog.show();
                requestCheckGmail(Constant.URL_CHECK_GMAIL);
            }
        });
    }

    private boolean checkGmail() {
        String gmail = layoutGmail.getEditText().getText().toString();
        Matcher matcher = Pattern.compile(Constant.PATTERN_EMAIL).matcher(gmail);
        if (!matcher.matches()) {
            layoutGmail.setError("Kiểm tra lại gmail");
            return false;
        } else {
            layoutGmail.setError(null);
            return true;
        }
    }

    private void init() {
        layoutGmail = findViewById(R.id.layout_gmail);
        btnForgot = findViewById(R.id.btn_forgot);
    }

    private void requestCheckGmail(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().equals("Successfully")) {
                            progressDialog.dismiss();
                            int codeAccept = new RandomCode().randomCode(100000, 999999);
                            new SendGmail().send(String.valueOf(codeAccept), layoutGmail.getEditText().getText().toString());
                            Intent intent = new Intent(ActivityForgotPsw.this, ActivityCheckMailForgot.class);
                            intent.putExtra("codeAccept", codeAccept);
                            intent.putExtra("nameGmail", nameGmail);
                            startActivity(intent);
                            finish();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityForgotPsw.this, "Kiểm tra lại gmail", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityForgotPsw.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                String nameGmail = spiltString(layoutGmail.getEditText().getText().toString());
                param.put("gmail", nameGmail);
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String spiltString(String string) {
        String[] arrString = string.split("\\@");
        nameGmail = arrString[0];
        return nameGmail;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
        finish();
    }
}
