package com.example.booking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityLogin extends AppCompatActivity {

    private EditText ed_email, ed_psw;
    private CheckBox cb_remember;
    private TextView tv_forgot, tv_create_user;
    private Button btn_login;
    private TextInputLayout layout_email, layout_psw;
    private SharedPreferences sharedPreferences;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        setInfoAccount();
        event();
    }

    public void init() {
        ed_email = findViewById(R.id.ed_email);
        ed_psw = findViewById(R.id.ed_psw);
        cb_remember = findViewById(R.id.cb_remember);
        tv_forgot = findViewById(R.id.lbl_forgot_psw);
        tv_forgot.setPaintFlags(R.color.colorBlue);
        tv_create_user = findViewById(R.id.lbl_create_user);
        btn_login = findViewById(R.id.btn_login);
        layout_email = findViewById(R.id.layout_email);
        layout_psw = findViewById(R.id.layout_psw);
        progressDialog = new ProgressDialog(ActivityLogin.this);
        progressDialog.setContentView(R.layout.progress_dilog);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Đăng Nhập");
        progressDialog.setMessage("Vui lòng chờ...");
//        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        sharedPreferences = getSharedPreferences("Account Login", MODE_PRIVATE);
    }

    private void setInfoAccount() {
        ed_email.setText(sharedPreferences.getString("email", ""));
        ed_psw.setText(sharedPreferences.getString("password", ""));
        cb_remember.setChecked(sharedPreferences.getBoolean("remember", false));
    }

    public void event() {
        btn_login.setOnClickListener(v -> {
            eve_login();
        });
        tv_create_user.setOnClickListener(v -> {
            eve_create_user();
        });
        tv_forgot.setOnClickListener(v -> {
            even_forgot_psw();
        });
    }

    private void eve_create_user() {
        finish();
        startActivity(new Intent(getApplicationContext(), ActivityCreateUser.class));
    }

    private void eve_login() {
        setRemember();
        if (checkEmail() && checkPassword()) {
            progressDialog.show();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    requestLogin(Constant.URL_LOGIN);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Kiểm tra lại tài khoản", Toast.LENGTH_SHORT).show();
        }
    }

    private void even_forgot_psw() {
        finish();
        startActivity(new Intent(ActivityLogin.this, ActivityForgotPsw.class));
    }

    private boolean checkEmail() {
        String textEmail = layout_email.getEditText().getText().toString().trim();
        Matcher matcher = Pattern.compile(Constant.PATTERN_EMAIL).matcher(textEmail);
        if (!matcher.matches()) {
            layout_email.setError("Kiểm tra lại email");
            return false;
        } else {
            layout_email.setError(null);
            return true;
        }
    }

    private boolean checkPassword() {
        String textPassword = layout_psw.getEditText().getText().toString().trim();
        Matcher matcher = Pattern.compile(Constant.PATTERN_PASSWORD).matcher(textPassword);
        if (!matcher.matches()) {
            layout_psw.setError("Kiểm tra lại mật khẩu(gồm 8 ký tự trở lên)");
            return false;
        } else {
            layout_psw.setError(null);
            return true;
        }
    }

    private void requestLogin(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().equals("Successfully")) {
                            Intent intent = new Intent(getApplicationContext(), ActivityCustomer.class);
                            intent.putExtra("gmail", layout_email.getEditText().getText().toString());
                            progressDialog.dismiss();
                            finish();
                            startActivity(intent);
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityLogin.this, "Kiểm tra lại tài khoản", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityLogin.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gmail", layout_email.getEditText().getText().toString().trim());
                params.put("password", layout_psw.getEditText().getText().toString().trim());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void setRemember() {
        if (cb_remember.isChecked()) {
            Editor editor = sharedPreferences.edit();
            editor.putString("email", ed_email.getText().toString());
            editor.putString("password", ed_psw.getText().toString());
            editor.putBoolean("remember", true);
            editor.commit();
        } else {
            Editor editor = sharedPreferences.edit();
            editor.remove("email");
            editor.remove("password");
            editor.remove("remember");
            editor.commit();
        }
    }

}
