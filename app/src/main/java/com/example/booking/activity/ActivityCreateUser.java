package com.example.booking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.booking.R;
import com.example.booking.apiSendEmail.GMailSender;
import com.example.booking.apiSendEmail.SendGmail;
import com.example.booking.constant.Constant;
import com.example.booking.controller.RandomCode;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityCreateUser extends AppCompatActivity {
    private static final int MAX = 999999;
    private static final int MIN = 100000;
    private TextInputLayout layout_customer, layout_gmail, layout_password;
    private RadioButton rd_male, rd_female;
    private Button btn_register;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        init();
        initDialog();
        event();
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Vui lòng đợi...");
    }

    private void init() {
        layout_customer = findViewById(R.id.layout_customer);
        layout_gmail = findViewById(R.id.layout_gmail);
        layout_password = findViewById(R.id.layout_psw);
        btn_register = findViewById(R.id.btn_register);
        rd_male = findViewById(R.id.male);
        rd_female = findViewById(R.id.female);
    }

    private void event() {
        btn_register.setOnClickListener(v -> {
            event_btn_register();
        });
    }


    private void event_btn_register() {

        if (check_cus() && check_email() && checkPassword()) {
            progressDialog.show();
            checkRequestGmail();
        } else {
            Toast.makeText(getApplicationContext(), "Kiểm tra lại thông tin", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkPassword() {
        String text_password = layout_password.getEditText().getText().toString().trim();
        Matcher matcher_password = Pattern.compile(Constant.PATTERN_PASSWORD).matcher(text_password);
        if (!matcher_password.matches()) {
            layout_password.setError("Kiểm tra lại mât khẩu(gồm 8 ký tự trở lên)");
            return false;
        } else {
            layout_password.setError(null);
            return true;
        }
    }

    private boolean check_cus() {
        String text_customer = layout_customer.getEditText().getText().toString().trim();
        if (text_customer.isEmpty()) {
            layout_customer.setError("Kiểm tra lại tên");
            return false;
        } else {
            layout_customer.setError(null);
            return true;
        }
    }

    private boolean check_email() {
        String text_email = layout_gmail.getEditText().getText().toString().trim();
        Matcher matcher_email = Pattern.compile(Constant.PATTERN_EMAIL).matcher(text_email);
        if (!matcher_email.matches()) {
            layout_gmail.setError("Kiểm tra lại email");
            return false;
        } else {
            layout_gmail.setError(null);
            return true;
        }
    }

    private void checkRequestGmail() {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constant.URL_CHECK_CREATE_GMAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().equals("Successfully")){
                            progressDialog.dismiss();
                            layout_gmail.setError(null);
                            int codeAccept = new RandomCode().randomCode(MIN, MAX);
                            new SendGmail().send(String.valueOf(codeAccept), layout_gmail.getEditText().getText().toString());
                            Intent intent = new Intent(getApplicationContext(), ActivityCheckGmail.class);
                            intent.putExtra("customername", layout_customer.getEditText().getText().toString().trim());
                            intent.putExtra("gmail", layout_gmail.getEditText().getText().toString().trim());
                            intent.putExtra("password", layout_password.getEditText().getText().toString().trim());
                            if (rd_male.isChecked()) {
                                intent.putExtra("gender", true);
                            } else if (rd_female.isChecked()) {
                                intent.putExtra("gender", false);
                            }
                            intent.putExtra("code_check", codeAccept);
                            startActivity(intent);
                            finish();
                        }else {
                            progressDialog.dismiss();
                            layout_gmail.setError("Email đã được sử dụng");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ActivityCreateUser.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> param = new HashMap<>();
                param.put("gmail", spiltString(layout_gmail.getEditText().getText().toString()));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }

    private String spiltString(String string) {
        String[] arrString = string.split("\\@");
        return arrString[0];
    }

    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
    }
}
