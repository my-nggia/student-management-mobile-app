package com.example.account;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;


public class MainActivityStudent extends AppCompatActivity {
    TextView std_name, std_age, std_phone, std_email, username_std;
    ImageView user_img, user_img_large;
    FirebaseFirestore DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_student);

        DB = FirebaseFirestore.getInstance();

        std_name = findViewById(R.id.std_name);
        std_age = findViewById(R.id.std_age);
        std_phone = findViewById(R.id.std_phone);
        std_email = findViewById(R.id.std_email);
        username_std = findViewById(R.id.username_std);
        user_img = findViewById(R.id.user_img);
        user_img_large = findViewById(R.id.user_img_large);

        // Nhận email từ sign in
        Intent emailFromSignIn = getIntent();
        String user_email = emailFromSignIn.getStringExtra("user_email");

        // Query dựa trên thuộc tính (field) "email" và hiện trên màn hình
        getStudentInfo(user_email);

        // Đổi ảnh profile
//        user_img.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeProfilePic();
//            }
//        });
//
//        // Đổi ảnh profile
//        user_img_large.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeProfilePic();
//            }
//        });

    }

    private void changeProfilePic() {
        //
    }

    private void getStudentInfo(String email) {
       DB.collection("student").get()
               .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                   @Override
                   public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                       List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                       if (!queryDocumentSnapshots.isEmpty()) {
                           for (DocumentSnapshot d : list) {
                              Toast.makeText(MainActivityStudent.this, "<-><-><->"+d.get("email").toString(), Toast.LENGTH_SHORT).show();

                               // query document có field "email" = email của người dùng
//                               if (email.equalsIgnoreCase(d.get("email").toString())) {
//                                   Toast.makeText(MainActivityStudent.this, "<><><>"+d.get("email").toString(), Toast.LENGTH_SHORT).show();
//                                   Student std = d.toObject(Student.class);
//                                   // set Text view
//                                   displayOnScreen(std);
//                               }
//                               Toast.makeText(MainActivityStudent.this, "<-><-><->"+d.get("email").toString(), Toast.LENGTH_SHORT).show();
//                               break;
                           }
                       } else {
                            // if the snapshot is empty we are displaying a toast message.
                            Toast.makeText(MainActivityStudent.this, "No data found", Toast.LENGTH_SHORT).show();
                        }
                   }
               }).addOnFailureListener(new OnFailureListener() {
                   @Override
                   public void onFailure(@NonNull Exception e) {
                       Toast.makeText(MainActivityStudent.this, "Failed to get data", Toast.LENGTH_SHORT).show();
                   }
               });
    }

    private void displayOnScreen(Student std) {
        username_std.setText(std.getName());
        std_name.setText("Name: " + std.getName());
        std_email.setText("Email: " + std.getEmail());
        std_phone.setText("Phone: " + std.getPhone());
        std_age.setText("Age: " + std.getAge());
    }


}