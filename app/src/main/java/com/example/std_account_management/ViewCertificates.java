package com.example.std_account_management;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ViewCertificates extends AppCompatActivity {
    private FirebaseFirestore DB;
    ArrayList<Certificate> certificates = new ArrayList<>();
    CertificateRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_certificates);

        DB = FirebaseFirestore.getInstance();

        Intent getEmail = getIntent();
        String std_email = getEmail.getStringExtra("std_email_cert");

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view_cert);
        adapter = new CertificateRecyclerViewAdapter(this, certificates);
        recyclerView.setAdapter(adapter);

        // check role, lấy data từ database về, lưu vào certificates
        checkRole(adapter, std_email);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


    }

    private void checkRole(CertificateRecyclerViewAdapter adapter, String stdEmail) {
        if (stdEmail.equalsIgnoreCase("manager@gmail.com")) {
            getAllStudentsCertificates(adapter);
        }else{
            getStudentCertificates(adapter, stdEmail);
        }
    }

    private void getAllStudentsCertificates(CertificateRecyclerViewAdapter adapter) {
        DB.collection("certificate").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {
                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                    for (DocumentSnapshot d : list) {
                        Certificate c = new Certificate(d.getString("email"), d.getString("certName"), d.getString("provider"), d.getString("validTo"));
                        certificates.add(c);
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    private void getStudentCertificates(CertificateRecyclerViewAdapter adapter, String std_email) {
            DB.collection("certificate").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                        for (DocumentSnapshot d : list) {
                            if (d.getString("email").equalsIgnoreCase(std_email)) {
                                Certificate c = new Certificate(d.getString("email"), d.getString("certName"), d.getString("provider"), d.getString("validTo"));
                                certificates.add(c);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            });
    }

}