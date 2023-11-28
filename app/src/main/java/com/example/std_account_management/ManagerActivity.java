package com.example.std_account_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AlertDialogLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ManagerActivity extends BaseMenuActivity {
    ArrayList<Student> studentsList = new ArrayList<>();
    String[] listSorting = new String[]{"Sorting by age","Sorting by name from A to Z", "Sorting by name from Z to A" };
    Spinner spinner;
    int default_img = R.drawable.user_img_default;
    private FirebaseFirestore DB;
    StudentRecyclerViewAdapter adapter;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager);
        DB = FirebaseFirestore.getInstance();

        // Toolbar
        toolbar = findViewById(R.id.my_app_bar);
        setSupportActionBar(toolbar);

    }



    // IMPORT

    // Flow: Read Student List (CSV) -> Send to Database -> Toast
    // readStudentFile()
    // -> Tutorial link:
    // 01. https://www.youtube.com/watch?v=i-TqNzUryn8
    // 02. https://www.youtube.com/watch?v=J6azVvt-9KE
    // private void readStudentFile() {
    //     CollectionReference studentsRef = DB.collection("students");
    //     studentsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
    //         @Override
    //         public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
    //             if (!queryDocumentSnapshots.isEmpty()) {
    //                 for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
    //                     // Convert mỗi DocumentSnapshot thành đối tượng Student
    //                     Student student = documentSnapshot.toObject(Student.class);
    //                     // Thêm sinh viên vào danh sách hoặc xử lý dữ liệu theo ý muốn
    //                     studentsList.add(student);
    //                 }
    //                 // Cập nhật RecyclerView hoặc giao diện người dùng sau khi có dữ liệu mới
    //                 adapter.notifyDataSetChanged();
    //             } else {
    //                 Toast.makeText(ManagerActivity.this, "No data found in Database", Toast.LENGTH_SHORT).show();
    //             }
    //         }
    //     }).addOnFailureListener(new OnFailureListener() {
    //         @Override
    //         public void onFailure(@NonNull Exception e) {
    //             Log.e("ReadStudentFile", "Error reading student data", e);
    //             Toast.makeText(ManagerActivity.this, "Failed to read student data", Toast.LENGTH_SHORT).show();
    //         }
    //     });
    // }

    // Flow: Read Certificate List -> Send to Database (Document name: certificate) -> Toast
//    private void readCertificateFile() {
//        CollectionReference certificatesRef = DB.collection("certificate");
//
//        certificatesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        // Convert DocumentSnapshot thành đối tượng Certificate
//                        Certificate certificate = documentSnapshot.toObject(Certificate.class);
//
//                        // Lấy thông tin từ đối tượng Certificate
//                        String certificateName = certificate.getCertificate_name(); [BUG]
//                        String stdEmail = certificate.getStd_email(); // [BUG]
//
//                        // Gọi hàm sendCertificateInfoToDB với thông tin từ Certificate
//                        sendCertificateInfoToDB(certificateName, stdEmail);
//                    }
//                } else {
//                    Toast.makeText(ManagerActivity.this, "No certificate data found in Database", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("ReadCertificateFile", "Error reading certificate data", e);
//                Toast.makeText(ManagerActivity.this, "Failed to read certificate data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

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

//    private void sendCertificateInfoToDB(String certificate_name, String std_email) {
//        Certificate certificate = new Certificate(std_email, certificate_name); // [BUG]
//
//        CollectionReference dbCertificates = DB.collection("certificate");
//
//        dbCertificates.add(certificate).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                Toast.makeText(ManagerActivity.this, "Add Certificate Successfully", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ManagerActivity.this, "Fail to add \n" + e, Toast.LENGTH_SHORT).show();
//                // Xử lý khi thêm thông tin chứng chỉ thất bại
//            }
//        });
//    }

    // EXPORT

    // Flow:
    // Lấy data từ DB về (hàm getStudentDataFromDB, getCertificateDataFromDB)
    // -> exportStudentList, exportCertificateList

    private ArrayList<Student> getStudentDataFromDB() {
        ArrayList<Student> dbStudentList = new ArrayList<>();
        // Code

        return dbStudentList;
    }

    private ArrayList<Certificate> getCertificateFromDB() {
         ArrayList<Certificate> dbCertificateList = new ArrayList<>();
         // Code

         return dbCertificateList;
    }

    // WRITE TO FILE (EXPORT)
    private void exportStudentList() {
        CollectionReference studentsRef = DB.collection("student");

        studentsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    StringBuilder csvData = new StringBuilder();
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Student student = documentSnapshot.toObject(Student.class);
                        // Tạo chuỗi CSV từ dữ liệu sinh viên
                        String studentData = student.getEmail() + "," +
                                student.getName() + "," +
                                student.getAge() + "," +
                                student.getPhone() + "," +
                                student.getPass() + "\n";

                        // Thêm dữ liệu của sinh viên vào chuỗi CSV
                        csvData.append(studentData);
                    }
                    // Gọi hàm ghi dữ liệu CSV vào tệp (hoặc thực hiện thao tác cần thiết khác)
                    writeDataToCSV(csvData.toString(), "student_list.csv");
                } else {
                    Toast.makeText(ManagerActivity.this, "No student data found in Database", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("ExportStudentList", "Error exporting student data", e);
                Toast.makeText(ManagerActivity.this, "Failed to export student data", Toast.LENGTH_SHORT).show();
            }
        });
    }

//    private void exportCertificateList() {
//        CollectionReference certificatesRef = DB.collection("certificate");
//
//        certificatesRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (!queryDocumentSnapshots.isEmpty()) {
//                    StringBuilder csvData = new StringBuilder();
//                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                        Certificate certificate = documentSnapshot.toObject(Certificate.class);
//                        // Tạo chuỗi CSV từ dữ liệu chứng chỉ
//                        String certificateData = certificate.getCertificate_name() + "," +
//                                certificate.getStd_email() + "\n"; // [BUG]
//
//                        // Thêm dữ liệu chứng chỉ vào chuỗi CSV
//                        csvData.append(certificateData);
//                    }
//                    // Gọi hàm ghi dữ liệu CSV vào tệp
//                    writeDataToCSV(csvData.toString(), "certificate_list.csv");
//                } else {
//                    Toast.makeText(ManagerActivity.this, "No certificate data found in Database", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.e("ExportCertificateList", "Error exporting certificate data", e);
//                Toast.makeText(ManagerActivity.this, "Failed to export certificate data", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    // Hàm này để ghi dữ liệu CSV vào tệp
    private void writeDataToCSV(String data, String fileName) {
        try {
            // Tạo một đối tượng FileOutputStream để ghi dữ liệu vào bộ nhớ trong (internal storage)
            FileOutputStream fileOutputStream = openFileOutput(fileName, Context.MODE_PRIVATE);

            // Sử dụng OutputStreamWriter để viết dữ liệu vào tệp CSV
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
            outputStreamWriter.write(data); // Ghi dữ liệu vào tệp CSV

            // Đóng OutputStreamWriter sau khi ghi dữ liệu xong
            outputStreamWriter.close();

            // Hiển thị thông báo khi ghi dữ liệu thành công
            Toast.makeText(ManagerActivity.this, "Data exported to " + getFilesDir() + "/" + fileName, Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý khi có lỗi xảy ra trong quá trình ghi dữ liệu
            Toast.makeText(ManagerActivity.this, "Error exporting data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
