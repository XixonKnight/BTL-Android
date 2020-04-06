package com.example.booking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.booking.R;

public class ActivityBookRoom extends AppCompatActivity {
    private Button btnPlus, btnLess, btnRegister;
    private TextView lblNameTypeRoom, lblArc, lblPrice;
    private EditText countRoom;
    private int count = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);
        init();
        event();
    }

    private void init() {
        btnLess = findViewById(R.id.btnLess);
        btnPlus = findViewById(R.id.btnPlus);
        btnRegister = findViewById(R.id.btnRegister);
        lblArc = findViewById(R.id.lblArc);
        lblNameTypeRoom = findViewById(R.id.nameTypeRoom);
        lblPrice = findViewById(R.id.price);
        countRoom = findViewById(R.id.countRoom);
    }

    private void event() {
//        eventChangeCountRoom();
    }

//    private void eventChangeCountRoom() {
//            btnLess.setOnClickListener(v -> {
//                count--;
//                if (count <= 1){
//                    count = 1;
//                }
//                countRoom.setText(String.valueOf(count));
//            });
//        btnPlus.setOnClickListener(v -> {
//            count++;
//            countRoom.setText(String.valueOf(count));
//        });
//    }
}
