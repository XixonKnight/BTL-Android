package com.example.booking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booking.R;
import com.example.booking.constant.Constant;
import com.example.booking.model.TypeRoom;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ActivityBookRoom extends AppCompatActivity {
    private Button btnPlus, btnLess, btnRegister, btnLessPerson, btnPlusPerson ;
    private TextView lblNameTypeRoom, lblArc, lblPrice;
    private EditText countRoom, edCountPerson, edDateRegister;
    private ImageButton btnSetDateRegister;
    private int count = 1, countPerson = 1;
    private int price;
    private TypeRoom typeRoom;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_room);
        init();
        setInfoTypeRoom();
        event();
    }

    private void setInfoTypeRoom() {
        lblNameTypeRoom.setText(typeRoom.getLblName());
        lblPrice.setText(String.valueOf(typeRoom.getPrice()));
        price = typeRoom.getPrice();
    }

    private void init() {
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
    }

    private void event() {
        eventChangeCountRoom();
        eventChangeCountPerson();
        eventSetDateRegister();
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
