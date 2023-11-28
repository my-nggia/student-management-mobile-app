package com.example.std_account_management;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class BaseMenuActivity extends AppCompatActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.back_home_screen:
//                Toast.makeText(getApplicationContext(), "Home", Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_add:
                Toast.makeText(getApplicationContext(), "Add A New Student", Toast.LENGTH_SHORT).show();
                Intent addStd = new Intent(getApplicationContext(), AddStudentActivity.class);
                startActivity(addStd);
                break;

            case R.id.menu_view_students:
                Toast.makeText(getApplicationContext(), "View All Students", Toast.LENGTH_SHORT).show();
                Intent viewAllStds = new Intent(getApplicationContext(), ViewAllStudents.class);
                startActivity(viewAllStds);
                break;

            case R.id.import_student_list:
                Toast.makeText(getApplicationContext(), "Import Student List", Toast.LENGTH_SHORT).show();
//                readStudentFile();
                break;
            case R.id.export_student_list:
                Toast.makeText(getApplicationContext(), "Export Student List", Toast.LENGTH_SHORT).show();
//                exportStudentList();
                break;
            case R.id.import_certificate_list:
//                readCertificateFile();
                Toast.makeText(getApplicationContext(), "Import Certificate List", Toast.LENGTH_SHORT).show();
                break;
            case R.id.export_certificate_list:
                Toast.makeText(getApplicationContext(), "Export Certificate List", Toast.LENGTH_SHORT).show();
//                exportCertificateList();
                break;
        }
        return true;
    }
}
