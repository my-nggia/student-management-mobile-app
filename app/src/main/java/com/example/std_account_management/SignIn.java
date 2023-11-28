package com.example.std_account_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignIn extends AppCompatActivity {
    EditText email_ed, password_ed;
    Button signInBtn;
    private FirebaseFirestore DB;
    String role;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        DB = FirebaseFirestore.getInstance();

        email_ed = findViewById(R.id.email_sign_in);
        password_ed = findViewById(R.id.password_sign_in);
        signInBtn = findViewById(R.id.sign_in_btn);

        RadioGroup radioGroup = findViewById(R.id.btn_group_role);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId là ID của RadioButton được chọn trong RadioGroup
                RadioButton radioButton = findViewById(checkedId);

                if (radioButton != null) {
                    // Lấy giá trị từ RadioButton được chọn
                    String selectedValue = radioButton.getText().toString();
                    // Sử dụng giá trị làm gì đó, ví dụ: hiển thị Toast
                    role = selectedValue;
                    Toast.makeText(SignIn.this, "Bạn đã chọn: " + selectedValue, Toast.LENGTH_SHORT).show();
                }
            }
        });
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // khóa khoảng trắng đầu cuối chuỗi
                String email = email_ed.getText().toString().trim();
                String password = password_ed.getText().toString().trim();
                // kiểm tra đã nhập đủ hết các giá trị
                if (!isEmptyField(email, password) && isAnyRadioButtonChecked(radioGroup)) {
                    // Kiểm tra tài khoản tồn tại
                    isValidAccount(email, password);
                }
            }
        });
    }


    private void isValidAccount(String email, String password) {
        // Xác định collection dựa trên role
        String collectionName = "";
        Class<?> activityClass = null;

        if (role.equalsIgnoreCase("admin")) {
            collectionName = "admin";
            activityClass = AdminActivity.class;
        } else if (role.equalsIgnoreCase("manager")) {
            collectionName = "manager";
            activityClass = ManagerActivity.class;
        } else {
            collectionName = "student";
            activityClass = StudentActivity.class;
        }
        final Class<?> finalActivityClass = activityClass;
        final String finalCollectionName = collectionName;
        // Tạo truy vấn để lấy dữ liệu từ collection
        DB.collection(collectionName)
                .whereEqualTo("email", email)
                .whereEqualTo("pass", password)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                // Tồn tại người dùng với thông tin đăng nhập đúng
                                if(finalCollectionName.equalsIgnoreCase("student")) {
                                    for (QueryDocumentSnapshot d : task.getResult()) {
                                        String db_email = d.getString("email");
                                        String db_pass = d.getString("pass");
                                        if (db_email.equalsIgnoreCase(email) && db_pass.equalsIgnoreCase(password)) {
                                            //Toast.makeText(SignIn.this, "OK " + email + " --- " + db_email, Toast.LENGTH_SHORT).show();
                                            Intent mainActivityStudent = new Intent(getApplicationContext(), StudentActivity.class);
                                            mainActivityStudent.putExtra("user_email", db_email.toString());
                                            startActivity(mainActivityStudent);
                                            break;
                                        }
                                    }
                                }
                                else {
                                    Intent mainActivityIntent = new Intent(getApplicationContext(), finalActivityClass);
                                    startActivity(mainActivityIntent);
                                }
                            }
                            else {
                                // Không tìm thấy người dùng
                                Toast.makeText(SignIn.this, "Your account is invalid", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // Xử lý lỗi khi truy cập cơ sở dữ liệu
                            Toast.makeText(SignIn.this, "Some error when access database", Toast.LENGTH_SHORT).show();
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

    private boolean isAnyRadioButtonChecked(RadioGroup radioGroup) {
        int radioButtonCount = radioGroup.getChildCount();

        for (int i = 0; i < radioButtonCount; i++) {
            RadioButton radioButton = (RadioButton) radioGroup.getChildAt(i);

            if (radioButton.isChecked()) {
                // Có ít nhất một RadioButton được chọn
                return true;
            }
        }
        // Không có RadioButton nào được chọn
        Toast.makeText(this, "Please choose your role", Toast.LENGTH_SHORT).show();
        return false;
    }
}