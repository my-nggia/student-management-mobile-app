package com.example.std_account_management;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.checkerframework.checker.units.qual.A;

import java.util.Calendar;

public class AddStudentActivity extends AppCompatActivity {

    EditText email_validate, phoneNum_validate, name_validate, password_validate;
    TextView displayDate;
    Button addBtn, cancelBtn;
    private String name, phoneNumber, status;
    private int age;
    private FirebaseFirestore DB;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_activity);

        DB = FirebaseFirestore.getInstance();

        displayDate = findViewById(R.id.date_tv);
        addBtn = findViewById(R.id.add_btn);
        cancelBtn = findViewById(R.id.cancel_add_btn);

        email_validate = findViewById(R.id.email_sign_up);
        phoneNum_validate = findViewById(R.id.phone_sign_up);
        name_validate = findViewById(R.id.name_sign_up);
        password_validate = findViewById(R.id.password_sign_up);

        // chọn ngày tháng năm sinh
        datePicker();

        displayDate = findViewById(R.id.date_tv);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email_validate.getText().toString();
                String userPhoneNum = phoneNum_validate.getText().toString();
                String userName = name_validate.getText().toString();
                String userPass = password_validate.getText().toString();
                String userBirthday = displayDate.getText().toString();

                // kiểm tra thông tin được nhập đủ
                if(!isEmptyField(userEmail, userPass, userName, userPhoneNum, userBirthday)) {
                    // kiểm tra:
                    // 1. email đã đúng chưa
                    // 2. số điện thoại
                    if (emailValidator(userEmail) && phoneNumberValidator(userPhoneNum)) {
                        sendUserInfoToDB(userEmail, userPass, userName, userPhoneNum, userBirthday); // Gửi dữ liệu lên database
                    }
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // trở lại AdminActivity.java / ManagerActivity.java
                Toast.makeText(AddStudentActivity.this, "Back lại Admin/ Manager", Toast.LENGTH_SHORT).show();
                try {
                    Intent manager_activity = new Intent(AddStudentActivity.this, ManagerActivity.class);
                    startActivity(manager_activity);
                } catch (Exception e) {
                    Toast.makeText(AddStudentActivity.this, "!!!! " + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmptyField(String email, String uPass, String uName, String phone, String birthday) {
        if (TextUtils.isEmpty(email)) {
            email_validate.setError("Please enter your email");
            return true;
        } else if (TextUtils.isEmpty(uName)) {
            name_validate.setError("Please enter your name");
            return true;
        } else if (TextUtils.isEmpty(uPass)) {
            password_validate.setError("Please enter your password");
            return true;
        } else if (TextUtils.isEmpty(phone)) {
            phoneNum_validate.setError("Please enter your phone number");
            return true;
        } else if (TextUtils.isEmpty(birthday)) {
            displayDate.setError("Please enter your birthday");
            return true;
        } else
            return false;
    }

    private int calculateAge(String year) {
        Calendar cal = Calendar.getInstance();
        int yearNow = cal.get(Calendar.YEAR);
        return yearNow - Integer.parseInt(year);
    }

    private void sendUserInfoToDB(String email, String pass, String name, String phone, String birthday) {
        String[] a = birthday.split("/", -2);
        int age = calculateAge(a[2]); // tuổi

        CollectionReference dbStudents = DB.collection("student");
        Student std = new Student(email, name, age, phone, "normal", pass);

        dbStudents.add(std).addOnSuccessListener(new OnSuccessListener<DocumentReference>(){
            @Override
            public void onSuccess(DocumentReference documentReference) {
                Toast.makeText(AddStudentActivity.this, "Add Student Successfully", Toast.LENGTH_SHORT).show();
                // Add thành công, chuyển sang về ManagerActivity
                try {
                    Intent manager_activity = new Intent(AddStudentActivity.this, ManagerActivity.class);
                    startActivity(manager_activity);
                } catch (Exception e) {
                    Toast.makeText(AddStudentActivity.this, "!!!! " + e.toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddStudentActivity.this, "Fail to add \n" + e, Toast.LENGTH_SHORT).show();
                // Add thất bại
            }
        });
    }


    // kiểm tra email
    private boolean emailValidator(String email) {
        if (!email.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return true;
        }
        else{
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // kiểm tra số điện thoại
    private boolean phoneNumberValidator(String phoneNumber) {
        if (phoneNumber.matches("0[0-9]{9}")) {
            return true;
        }
        else {
            Toast.makeText(this, "Số điện thoại không hợp lệ", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    // chọn ngày/tháng/
    public void datePicker() {
        displayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(AddStudentActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date =  day + "/" + month + "/" + year;
                displayDate.setText(date);
            }
        };
    }

}
