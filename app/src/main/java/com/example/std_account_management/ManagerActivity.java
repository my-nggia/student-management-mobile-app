package com.example.std_account_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ManagerActivity extends AppCompatActivity {
    ArrayList<Student> studentsList = new ArrayList<>();
    int default_img = R.drawable.user_img_default;
    private FirebaseFirestore DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        DB = FirebaseFirestore.getInstance();



        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);

        StudentRecyclerViewAdapter adapter = new StudentRecyclerViewAdapter(this, studentsList);
        recyclerView.setAdapter(adapter);

        // Lấy dữ liệu từ database, lưu vào studentsList
        setUpStudentsData(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    private void setUpStudentsData(StudentRecyclerViewAdapter adapter) {
        DB.collection("student").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Student s = d.toObject(Student.class);
                        studentsList.add(s);
                    }
                    adapter.notifyDataSetChanged();
                }
                else {
                    Toast.makeText(ManagerActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}