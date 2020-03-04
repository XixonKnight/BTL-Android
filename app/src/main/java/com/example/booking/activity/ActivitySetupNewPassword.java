package com.example.booking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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

public class ActivitySetupNewPassword extends AppCompatActivity {
    private TextInputLayout layout_new_psw, layout_confirm;
    private Button btn_save;
    private String nameGmail;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_new_password);
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
        btn_save.setOnClickListener(v -> {
            if (checkPassword()) {
                progressDialog.show();
                sendUpdatePassword(Constant.URL_UPDATE_PASSWORD);
            }
        });
    }

    private void sendUpdatePassword(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().equals("Successfully")) {
                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                            Toast.makeText(ActivitySetupNewPassword.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ActivitySetupNewPassword.this, "Lỗi xác nhận", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivitySetupNewPassword.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("gmail", nameGmail);
                params.put("password", layout_new_psw.getEditText().getText().toString());
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private boolean checkPassword() {
        if (checkConfirm()) {
            layout_confirm.setError(null);
            return true;
        } else {
            layout_confirm.setError("Mật khẩu không trùng nhau");
            return false;
        }
    }

    private boolean checkNewPassword() {
        String psw = layout_new_psw.getEditText().getText().toString();
        Matcher matcher = Pattern.compile(Constant.PATTERN_PASSWORD).matcher(psw);
        if (matcher.matches()) {
            layout_new_psw.setError(null);
            return true;
        } else {
            layout_new_psw.setError("Kiểm tra lại mật khẩu(hơn 8 ký tự)");
            return false;
        }
    }

    private boolean checkConfirm() {
        if (checkNewPassword()) {
            String psw = layout_new_psw.getEditText().getText().toString();
            String confirm = layout_confirm.getEditText().getText().toString();
            if (psw.equals(confirm)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void init() {
        btn_save = findViewById(R.id.btn_save);
        layout_new_psw = findViewById(R.id.layout_new_psw);
        layout_confirm = findViewById(R.id.layout_confirm);
        Intent intent = getIntent();
        nameGmail = intent.getStringExtra("nameGmail");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ActivityCheckMailForgot.class));
        finish();
    }
}
