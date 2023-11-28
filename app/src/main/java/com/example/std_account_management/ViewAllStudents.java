package com.example.std_account_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ViewAllStudents extends BaseMenuActivity implements AdapterView.OnItemSelectedListener, RecyclerViewInterface {
    ArrayList<Student> studentsList = new ArrayList<>();
    String[] listSorting = new String[]{"Sorting by age","Sorting by name from A to Z", "Sorting by name from Z to A" };
    Spinner spinner;
    int default_img = R.drawable.user_img_default;
    private FirebaseFirestore DB;
    StudentRecyclerViewAdapter adapter;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_students);

        DB = FirebaseFirestore.getInstance();

        Toolbar toolbar = findViewById(R.id.my_app_bar);
        setSupportActionBar(toolbar);


        //Spinner sorting
        spinner = findViewById(R.id.spinner);
        // Adapter for spinner sorting
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listSorting);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterSpinner);
        spinner.setOnItemSelectedListener(ViewAllStudents.this);

        // Recycler View
        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        adapter = new StudentRecyclerViewAdapter(this, studentsList,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
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
                    Toast.makeText(ViewAllStudents.this, "No data found in Database", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewAllStudents.this, "Fail to Load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String citeria = parent.getItemAtPosition(position).toString();
        sortingListStudent(citeria);
    }

    private void sortingListStudent(String citeria) {
        if(citeria.equals("Sorting by age")) {
            Collections.sort(studentsList, new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return Integer.compare(o1.getAge(), o2.getAge());
                }
            });
            Toast.makeText(this, "Sorting by age", Toast.LENGTH_SHORT).show();
        }
        else if( citeria.equals("Sorting by name from A to Z")) {
            Collections.sort(studentsList, new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
            Toast.makeText(this, "Sorting by name from A to Z", Toast.LENGTH_SHORT).show();
        }
        else if( citeria.equals("Sorting by name from Z to A")) {
            Collections.sort(studentsList, new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return o2.getName().compareToIgnoreCase(o1.getName());
                }
            });
            Toast.makeText(this, "Sorting by name from Z to A", Toast.LENGTH_SHORT).show();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // click vào 1 item -> hiện thông tin chi tiết
    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(ViewAllStudents.this, ViewStudentAdminManager.class);
        String std_to_string = studentsList.get(position).toString();
        intent.putExtra("std_info_send_from_ViewAllStudents", std_to_string);
        startActivity(intent);
    }

    // giữ
    @Override
    public void onItemLongClick(int position) {
//        studentsList.remove(position);
//        adapter.notifyItemRemoved(position);
    }

    // Kéo sang trái để xóa
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Student del_std = studentsList.get(viewHolder.getAdapterPosition());
//            deleteStudentFromDB(del_std);
//            studentsList.remove(viewHolder.getAdapterPosition());
            Toast.makeText(ViewAllStudents.this, "Deleted" + del_std.getName(), Toast.LENGTH_SHORT).show();
//            adapter.notifyDataSetChanged();
        }
    };

    // Xóa sinh viên khỏi Database dựa trên email
//    private void deleteStudentFromDB(Student delStd) {
//        DB.collection("student").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()) {
//                    for (QueryDocumentSnapshot d : task.getResult()) {
//
//                    }
//                }
//            }
//        });
//    }
}