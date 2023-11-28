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

public class ViewAllStudents extends BaseMenuActivity implements AdapterView.OnItemSelectedListener, RecyclerViewInterface, DialogFragment.OnInputListener {
    ArrayList<Student> studentsList = new ArrayList<>();
    String[] listSorting = new String[]{"Sorting by age","Sorting by name from A to Z", "Sorting by name from Z to A" };
    Spinner spinner;
    int default_img = R.drawable.user_img_default;
    private FirebaseFirestore DB;
    StudentRecyclerViewAdapter adapter;
    private Toolbar toolbar;
    private static final String TAG = "";
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
        }
        else if( citeria.equals("Sorting by name from A to Z")) {
            Collections.sort(studentsList, new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return o1.getName().compareToIgnoreCase(o2.getName());
                }
            });
        }
        else if( citeria.equals("Sorting by name from Z to A")) {
            Collections.sort(studentsList, new Comparator<Student>() {
                @Override
                public int compare(Student o1, Student o2) {
                    return o2.getName().compareToIgnoreCase(o1.getName());
                }
            });
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

    // Kéo sang trái để xóa
    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            Student del_std = studentsList.get(viewHolder.getAdapterPosition());
            deleteStudentFromDB(del_std);
            studentsList.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();
        }
    };

    // Nhấn giữ để update thông tin sinh viên
    int updated_user_pos_array_list;
    @Override
    public void onItemLongClick(int position) {
        updated_user_pos_array_list = position;
        DialogFragment dialog = new DialogFragment();
        dialog.show(getSupportFragmentManager(), "Dialog");
    }

    @Override
    public void sendInput(String input) {
        Toast.makeText(ViewAllStudents.this, input, Toast.LENGTH_SHORT).show();
        String[] updatedInfo = input.split(",");
        // name, age, phone
        sendUpdatedInfoToDB(updatedInfo[0], updatedInfo[1], updatedInfo[2]);
    }

    private void sendUpdatedInfoToDB(String s, String s1, String s2) {
        Student st = studentsList.get(updated_user_pos_array_list);
        // email,  name,  age,  phone,  status,  password
        Student updatedStd = new Student(st.getEmail().toString(), s, Integer.parseInt(s1), s2, st.getStatus().toString(), st.getPass().toString());
        DB.collection("student").whereEqualTo("email", st.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String idDelete = document.getId();
                        DB.collection("student").document(idDelete).set(updatedStd).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ViewAllStudents.this, "Updated Successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewAllStudents.this, "Fail to update in DB", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    }

    // Xóa sinh viên khỏi Database dựa trên email
    private void deleteStudentFromDB(Student delStd) {
        DB.collection("student").whereEqualTo("email", delStd.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String idDelete = document.getId();
                        DB.collection("student").document(idDelete).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ViewAllStudents.this, "Deleted in DB", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ViewAllStudents.this, "Fail to delete in DB", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
    } // [END] Delete a student from database



}
