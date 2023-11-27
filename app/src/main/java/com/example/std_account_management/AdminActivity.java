package com.example.std_account_management;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminActivity extends AppCompatActivity {
    ArrayList<Student> studentsList = new ArrayList<>();
    int default_img = R.drawable.user_img_default;
    private FirebaseFirestore DB;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        DB = FirebaseFirestore.getInstance();

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_admin);
        setSupportActionBar(toolbar);


        // Recycler View
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
                    Toast.makeText(AdminActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // Menu for toolbar

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                Toast.makeText(AdminActivity.this, "Add New Stduent", Toast.LENGTH_SHORT).show();
                Intent addStd = new Intent(AdminActivity.this, AddStudentActivity.class);
                startActivity(addStd);
                break;
        }
        return true;
    }




}