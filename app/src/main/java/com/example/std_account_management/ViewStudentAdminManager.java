package com.example.std_account_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class ViewStudentAdminManager extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student_admin_manager);

        // Nhận chuỗi thông tin 1 sinh viên từ ViewAllStudents
        Intent intent = getIntent();
        String std = intent.getStringExtra("std_info_send_from_ViewAllStudents");
        String[] std_info = std.split(",");

        TextView name_tv, age_tv, phone_tv, email_tv, status_tv, login_tv;

        name_tv = findViewById(R.id.std_name_view);
        age_tv = findViewById(R.id.std_age_view);
        phone_tv = findViewById(R.id.std_phone_view);
        email_tv = findViewById(R.id.std_email_view);
        status_tv = findViewById(R.id.std_status_view);
        login_tv = findViewById(R.id.std_login_times_view);

        name_tv.setText("Name: " + std_info[0]);
        age_tv.setText("Age: " + std_info[1]);
        phone_tv.setText("Phone: " + std_info[2]);
        email_tv.setText("Email: " + std_info[3]);
        status_tv.setText("Status: " + std_info[4]);
        login_tv.setText("Number of logins: " +std_info[5]);
//        login_tv.setText("Number of logins: " + "<????>");

    }
}