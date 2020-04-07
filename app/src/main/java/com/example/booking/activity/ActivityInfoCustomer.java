package com.example.booking.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
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
import com.example.booking.constant.Constant;
import com.example.booking.model.dbo.Customer;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityInfoCustomer extends AppCompatActivity {

    private static final int MESSAGE_CUSTOMER = 1;
    private static final int REQUEST_CODE_IMAGE = 2;

    private TextInputLayout layout_name_customer, layout_address, layout_dob;
    private TextInputEditText ed_dob, ed_customer, ed_address;
    private CircleImageView avatar_customer;
    private Button btn_save;
    private RadioGroup gender;
    private RadioButton rd_male, rd_female;
    private Customer customer;
    private Handler handler;
    private Bitmap bitmap;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_customer);
        init();
        initHandler();
        infoCustomer();
        initDialog();
        event();
    }

    private void initDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Đang cập nhật");
        progressDialog.setMessage("Vui lòng đợi...");
    }

    private void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MESSAGE_CUSTOMER: {
                        Customer cus = new Customer();
                        cus = (Customer) msg.obj;
                        setInfoCustomer(cus);
                        break;
                    }
                }
            }
        };
    }

    private void event() {
        avatar_customer.setOnClickListener(v -> {
            eventImage();
        });
        ed_dob.setOnClickListener(v -> {
            eventDate();
        });
        btn_save.setOnClickListener(v -> {
            progressDialog.show();
            eventUpdateInfoCustomer(Constant.URL_UPDATE_INFO_CUSTOMER);
        });
    }

    private void eventUpdateInfoCustomer(String url) {
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.toString().equals("Successfully")) {
                            progressDialog.dismiss();
                            finish();
                            Toast.makeText(ActivityInfoCustomer.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(ActivityInfoCustomer.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(ActivityInfoCustomer.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id", String.valueOf(customer.getId()));
                params.put("customername", ed_customer.getText().toString());
                params.put("address", ed_address.getText().toString());
                params.put("dob", ed_dob.getText().toString());
                if (rd_male.isChecked()) {
                    params.put("gender", String.valueOf(true));
                } else {
                    params.put("gender", String.valueOf(false));
                }
                params.put("image_path", imageToString(bitmap));
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }


    private void eventDate() {
        Calendar calendar = Calendar.getInstance();
        int yearNow = calendar.get(Calendar.YEAR);
        int monthNow = calendar.get(Calendar.MONTH);
        int dayOM = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog pickerDialog = new DatePickerDialog(this,
                (view, year, month, dayOfMonth) -> {
                    calendar.set(year, month, dayOfMonth);
                    SimpleDateFormat format = new SimpleDateFormat(Constant.FORMAT_DATE);
                    ed_dob.setText(format.format(calendar.getTime()));
                }, yearNow, monthNow, dayOM);
        pickerDialog.show();
    }

    private void eventImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select image from gallery"), REQUEST_CODE_IMAGE);
    }

    private void setInfoCustomer(Customer customer) {
        SimpleDateFormat format = new SimpleDateFormat(Constant.FORMAT_DATE);
        if (customer.getAvatar().equals("null")||customer.getAvatar().isEmpty()) {
            avatar_customer.setImageResource(R.drawable.avatar_default);
        } else {
            Picasso.get().load(customer.getAvatar()).into(avatar_customer);
        }
        ed_customer.setText(customer.getCustomerName());
        if (!customer.getAddress().equals("null")) {
            ed_address.setText(customer.getAddress());
        }
        if (customer.getDOB() != null) {
            ed_dob.setText(format.format(customer.getDOB()));
        }
        if (customer.isGender()) {
            rd_male.setChecked(true);
        } else {
            rd_female.setChecked(true);
        }

    }

    private void init() {
        layout_name_customer = findViewById(R.id.layout_name_customer);
        layout_address = findViewById(R.id.layout_address);
        layout_dob = findViewById(R.id.layout_dob);
        ed_dob = findViewById(R.id.ed_dob);
        avatar_customer = findViewById(R.id.avatar_customer);
        btn_save = findViewById(R.id.btn_save);
        rd_male = findViewById(R.id.male);
        rd_female = findViewById(R.id.female);
        ed_customer = findViewById(R.id.ed_customer);
        ed_address = findViewById(R.id.ed_address);
        gender = findViewById(R.id.gender);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    public void infoCustomer() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Intent intent = getIntent();
                customer = (Customer) intent.getSerializableExtra("customer");
                Message msg = new Message();
                msg.what = MESSAGE_CUSTOMER;
                msg.obj = customer;
                handler.sendMessage(msg);
            }
        });
        thread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                avatar_customer.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String imageToString(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        byte[] arrByte = out.toByteArray();
        return Base64.encodeToString(arrByte, Base64.DEFAULT);
    }
}
