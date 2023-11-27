package com.example.std_account_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
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

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
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
                    Toast.makeText(ManagerActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(ManagerActivity.this, "Add A New Student", Toast.LENGTH_SHORT).show();
                Intent addStd = new Intent(ManagerActivity.this, AddStudentActivity.class);
                startActivity(addStd);
                break;
            case R.id.import_student_list:
                Toast.makeText(ManagerActivity.this, "Import Student List", Toast.LENGTH_SHORT).show();
                readStudentFile();
                break;
            case R.id.export_student_list:
                Toast.makeText(ManagerActivity.this, "Export Student List", Toast.LENGTH_SHORT).show();
                exportStudentList();
                break;
            case R.id.import_certificate_list:
                readCertificateFile();
                Toast.makeText(ManagerActivity.this, "Import Certificate List", Toast.LENGTH_SHORT).show();
                break;
            case R.id.export_certificate_list:
                Toast.makeText(ManagerActivity.this, "Export Certificate List", Toast.LENGTH_SHORT).show();
                exportCertificateList();
                break;
        }
        return true;
    }

    // IMPORT

    // Flow: Read Student List (CSV) -> Send to Database -> Toast
    ArrayList<Student> import_student_list = new ArrayList<>();
    private void readStudentFile() {
        // Code


        // Sau khi đọc thành công -> for loop cho từng student trong mảng import_student_list
        // -> gọi hàm sendUserInfoToDB (có code sẵn bên dưới)
    }

    // Flow: Read Certificate List -> Send to Database (Document name: certificate) -> Toast
    private void readCertificateFile() {
        // Code (Đọc file)

        // Tạo Certificate.java
        // Tạo mảng import_certificate_list: ArrayList<Certificate> import_certificate_list = new ArrayList<>();
        // Đọc thành công -> for loop cho từng certificate trong mảng import_certificate_list
        // -> gọi hàm sendCertificateInfoToDB (chưa có code sẵn / giống như sendUserInfoToDB )
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
                Toast.makeText(ManagerActivity.this, "Add Student Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ManagerActivity.this, "Fail to add \n" + e, Toast.LENGTH_SHORT).show();
                // Add thất bại
            }
        });
    }

    // EXPORT

    private void exportStudentList() {
    }

    private void exportCertificateList() {
    }
}