package com.example.booking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.example.booking.constant.Constant;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActivityChangePassword extends AppCompatActivity {
    private TextInputLayout layout_psw_old, layout_psw_new, layout_psw_confirm;
    private Button btn_change;
    private String nameGmail, password;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
        initDialog();
        event();
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Đang đổi mật khẩu");
        progressDialog.setMessage("Vui lòng đợi...");
    }

    private void event() {
        btn_change.setOnClickListener(v -> {
            changePasswordNew();
        });

    }

    private void changePasswordNew() {
        if (checkPasswordOdl()) {
            if (checkPasswordConfirm()) {
                progressDialog.show();
                sendRequest(Constant.URL_UPDATE_PASSWORD);
            }
        }
    }

    private void sendRequest(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().equals("Successfully")) {
                            progressDialog.dismiss();
                            finish();
                            Toast.makeText(ActivityChangePassword.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityChangePassword.this, "Lỗi đổi mật khẩu", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityChangePassword.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gmail", nameGmail);
                params.put("password", layout_psw_confirm.getEditText().getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private boolean checkPasswordNew() {
        String ed_psw_new = layout_psw_new.getEditText().getText().toString();
        Matcher matcher = Pattern.compile(Constant.PATTERN_PASSWORD).matcher(ed_psw_new);
        if (matcher.matches()) {
            layout_psw_new.setError(null);
            return true;
        } else {
            layout_psw_new.setError("Kiểm tra lại mật khẩu(hơn 8 ký tự");
            return false;
        }
    }

    private boolean checkPasswordConfirm() {
        String ed_psw_confirm = layout_psw_confirm.getEditText().getText().toString();
        String ed_psw_new = layout_psw_new.getEditText().getText().toString();
        if (checkPasswordNew()) {
            if (ed_psw_confirm.equals(ed_psw_new)) {
                layout_psw_confirm.setError(null);
                return true;
            } else {
                layout_psw_confirm.setError("Mật khẩu không trùng nhau");
                return false;
            }
        }
        return false;
    }

    private boolean checkPasswordOdl() {
        String ed_password = layout_psw_old.getEditText().getText().toString();
        if (password.equals(ed_password)) {
            layout_psw_old.setError(null);
            return true;
        } else {
            layout_psw_old.setError("Sai mật khẩu");
            return false;
        }
    }

    private void init() {
        layout_psw_old = findViewById(R.id.layout_psw_old);
        layout_psw_new = findViewById(R.id.layout_psw_new);
        layout_psw_confirm = findViewById(R.id.layout_psw_confirm);
        btn_change = findViewById(R.id.btn_change);
        Intent intent = getIntent();
        nameGmail = intent.getStringExtra("gmail");
        password = intent.getStringExtra("password");
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
