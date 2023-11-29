package com.example.std_account_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class StudentActivity extends AppCompatActivity {
    TextView std_name, std_age, std_phone, std_email, username_std, std_log_times, sign_out_std;
    ImageView user_img;
    FirebaseFirestore DB;
    Button view_cer_std;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        DB = FirebaseFirestore.getInstance();

        std_name = findViewById(R.id.std_name);
        std_age = findViewById(R.id.std_age);
        std_phone = findViewById(R.id.std_phone);
        std_email = findViewById(R.id.std_email);
        username_std = findViewById(R.id.username_std);
        user_img = findViewById(R.id.user_img);
        std_log_times = findViewById(R.id.std_login_times);
        sign_out_std = findViewById(R.id.sign_out_std);
        view_cer_std = findViewById(R.id.view_cert_std);

        // Nhận email từ sign in activity
        Intent emailFromSignIn = getIntent();
        String user_email = emailFromSignIn.getStringExtra("user_email");

        // Query dựa trên thuộc tính (field) "email"
        getStudentInfo(user_email);

        sign_out_std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(StudentActivity.this, SignIn.class);
                startActivity(i);
            }
        });

        view_cer_std.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(StudentActivity.this, ViewCertificates.class);
                it.putExtra("std_email_cert", user_email);
                startActivity(it);
            }
        });



    }

    private void getStudentInfo(String email) {
        DB.collection("student").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (DocumentSnapshot d : list) {
                                // query document có field "email" = email của người dùng
                                if (email.equalsIgnoreCase(d.get("email").toString())) {
                                    Student std = d.toObject(Student.class);
                                    // set Text view
                                    displayOnScreen(std);
                                    break;
                                }
                            }
                        } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(StudentActivity.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(StudentActivity.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayOnScreen(Student std) {
        username_std.setText(std.getName());
        std_name.setText("Name: " + std.getName());
        std_email.setText("Email: " + std.getEmail());
        std_phone.setText("Phone: " + std.getPhone());
        std_age.setText("Age: " + std.getAge());
        std_log_times.setText("Number of logins: " + String.valueOf(std.getLoginTimes()));
    }
}