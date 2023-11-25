package com.example.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignIn extends AppCompatActivity {
    EditText email_ed, password_ed;
    Button signInBtn, signUpBtn;
    private FirebaseFirestore DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in);

        DB = FirebaseFirestore.getInstance();

        email_ed = findViewById(R.id.email_sign_in);
        password_ed = findViewById(R.id.password_sign_in);
        signInBtn = findViewById(R.id.sign_in_btn);
        signUpBtn = findViewById(R.id.sign_up);

        // sự kiện Sign In
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khóa khoảng trắng đầu cuối chuỗi
                String email = email_ed.getText().toString().trim();
                String password = password_ed.getText().toString().trim();

                // kiểm tra đã nhập đủ hết các giá trị
                if (!isEmptyField(email, password)) {
                    // Kiểm tra tài khoản tồn tại
                    isValidAccount(email, password);
                }
            }
        });

        // Nhấn sign up khi chưa có tài khoản
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUp = new Intent(SignIn.this, SignUp.class);
                startActivity(signUp);
            }
        });

    }

    // kiểm tra tài khoản có tồn tại hay không
    private void isValidAccount(String email, String password) {
        DB.collection("student").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot d : task.getResult()) {
                        String db_email = d.getString("email");
                        String db_pass = d.getString("pass");
                        if (db_email.equalsIgnoreCase(email) && db_pass.equalsIgnoreCase(password)) {
                            Intent mainActivityStudent = new Intent(getApplicationContext(), MainActivityStudent.class);
                            mainActivityStudent.putExtra("user_email", db_email.toString());
                            startActivity(mainActivityStudent);
                            break;
                        }

                    }
                }
            }
        });
    }
    private boolean isEmptyField(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            email_ed.setError("Please enter your email");
            return true;
        } else if (TextUtils.isEmpty(password)) {
            password_ed.setError("Please enter your name");
            return true;
        } else
            return false;
    }
}