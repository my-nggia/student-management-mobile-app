package com.example.std_account_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentRecyclerViewAdapter extends RecyclerView.Adapter<StudentRecyclerViewAdapter.MyViewHolder> {
    private final RecyclerViewInterface recyclerViewInterface;
    Context context;
    ArrayList<Student> students;
    public StudentRecyclerViewAdapter(Context context, ArrayList<Student> students, RecyclerViewInterface recyclerViewInterface) {
        this.context = context;
        this.students = students;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public StudentRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType ) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.student_card_row, parent, false);
        return new StudentRecyclerViewAdapter.MyViewHolder(view, recyclerViewInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentRecyclerViewAdapter.MyViewHolder holder, int position) {
        // gán giá trị cho view đã tạo ở student_card_row.xml
        // dựa trên vị trí của recycler view
        holder.tvName.setText(students.get(position).getName());
        holder.tvEmail.setText("Email: " + students.get(position).getEmail());
        holder.tvPhone.setText("Phone: " + students.get(position).getPhone());
        holder.tvStatus.setText(students.get(position).getStatus());
        holder.tvAge.setText(String.valueOf("Age: " + students.get(position).getAge()));
        // ***change this***
        holder.imgView.setImageResource(R.drawable.user_img_default);
    }

    @Override
    public int getItemCount() {
        return students.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imgView;
        TextView tvName, tvAge, tvEmail, tvPhone, tvStatus;
        public MyViewHolder(@NonNull View itemView, RecyclerViewInterface recyclerViewInterface) {
            super(itemView);
            imgView = itemView.findViewById(R.id.std_img_card);
            tvName = itemView.findViewById(R.id.std_name_card);
            tvAge = itemView.findViewById(R.id.std_age_card);
            tvEmail = itemView.findViewById(R.id.std_email_card);
            tvPhone = itemView.findViewById(R.id.std_phone_card);
            tvStatus= itemView.findViewById(R.id.std_status_card);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (recyclerViewInterface != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            recyclerViewInterface.onItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}
