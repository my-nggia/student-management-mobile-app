package com.example.std_account_management;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class DialogFragment extends androidx.fragment.app.DialogFragment {

    public interface OnInputListener {
        void sendInput(String input);
    }
    public OnInputListener onInputListener;
    private EditText name_ed, phone_ed, age_ed;
    private Button cancel_btn, update_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_dialog_fragment, container, false);

        cancel_btn = view.findViewById(R.id.update_cancel_btn);
        update_btn = view.findViewById(R.id.update_btn);

        age_ed = view.findViewById(R.id.age_update);
        name_ed = view.findViewById(R.id.name_update);
        phone_ed = view.findViewById(R.id.phone_update);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = name_ed.getText().toString() + ',' + age_ed.getText().toString() + ',' + phone_ed.getText().toString();
                onInputListener.sendInput(input);
                getDialog().dismiss();
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            onInputListener = (OnInputListener)getActivity();
        } catch (ClassCastException e) {
//            Toast.makeText(context, "!! DialogFragment", Toast.LENGTH_SHORT).show();
        }
    }

}