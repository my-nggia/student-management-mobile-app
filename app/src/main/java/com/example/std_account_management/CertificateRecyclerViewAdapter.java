package com.example.std_account_management;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CertificateRecyclerViewAdapter extends RecyclerView.Adapter<CertificateRecyclerViewAdapter.MViewHolder> {
    Context context;
    ArrayList<Certificate> certificates;

    public CertificateRecyclerViewAdapter(Context context, ArrayList<Certificate> certificates) {
        this.context = context;
        this.certificates = certificates;
    }

    @NonNull
    @Override
    public CertificateRecyclerViewAdapter.MViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.certificate_card_row, parent, false);
        return new CertificateRecyclerViewAdapter.MViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CertificateRecyclerViewAdapter.MViewHolder holder, int position) {
        holder.cer_name.setText("Certificate Name: " + certificates.get(position).getCertificate_name());
        holder.provider.setText("Provider: " + certificates.get(position).getProvider());
        holder.valid.setText("Valid to: " + certificates.get(position).getValidTo());
    }

    @Override
    public int getItemCount() {
        return certificates.size();
    }

    public class MViewHolder extends RecyclerView.ViewHolder{
        TextView cer_name, provider, valid;
        public MViewHolder(@NonNull View itemView) {
            super(itemView);
            cer_name = itemView.findViewById(R.id.cert_name_card);
            provider = itemView.findViewById(R.id.cert_provider_card);
            valid = itemView.findViewById(R.id.cert_valid_card);
        }
    }
}
