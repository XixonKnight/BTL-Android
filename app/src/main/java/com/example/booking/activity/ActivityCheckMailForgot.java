package com.example.booking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.booking.R;

public class ActivityCheckMailForgot extends AppCompatActivity {
    private EditText ed_code;
    private Button btn_check;
    private int codeAccept;
    private String nameGmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_mail_forgot);
        init();
        event();
    }

    private void event() {
        btn_check.setOnClickListener(v -> {
            int check = Integer.parseInt(ed_code.getText().toString());
            if (check == codeAccept) {
                Intent intent = new Intent(getApplicationContext(), ActivitySetupNewPassword.class);
                intent.putExtra("nameGmail", nameGmail);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(this, "Kiểm tra lại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        ed_code = findViewById(R.id.ed_code);
        btn_check = findViewById(R.id.btn_check_gmail);
        Intent intent = getIntent();
        codeAccept = intent.getIntExtra("codeAccept", -1);
        nameGmail = intent.getStringExtra("nameGmail");
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), ActivityForgotPsw.class));
        finish();
    }
}
