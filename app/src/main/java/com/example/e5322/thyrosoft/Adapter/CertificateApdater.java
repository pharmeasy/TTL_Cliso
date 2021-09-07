package com.example.e5322.thyrosoft.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.Activity.Certificate_activity;
import com.example.e5322.thyrosoft.Activity.ViewCertificate;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.CertificateResponseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class CertificateApdater extends RecyclerView.Adapter<CertificateApdater.ViewHolder> {

    private ArrayList<CertificateResponseModel.CertificatedtlDTO> certificate_array;
    private Certificate_activity certificate_activity;


    public CertificateApdater(Certificate_activity certificate_activity, ArrayList<CertificateResponseModel.CertificatedtlDTO> certificate_array) {
        this.certificate_array = certificate_array;
        this.certificate_activity = certificate_activity;

    }


    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.certificate_item, parent, false);
        return new ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

       /* if (!GlobalClass.isNull(contact_array_lists_to_pass.get(position).getUrl())) {
            holder.web_certificate.loadUrl(contact_array_lists_to_pass.get(position).getUrl().replaceAll("\\\\", "//").replace(" ", "%20"));
            System.out.println("URL------"+contact_array_lists_to_pass.get(position).getUrl().replaceAll("\\\\", "//").replace(" ", "%20"));
        }*/

        if (!GlobalClass.isNull(certificate_array.get(position).getTitle())) {
            holder.tv_type.setText(certificate_array.get(position).getTitle());
            holder.tv_title.setText(certificate_array.get(position).getName());
        }
        holder.ll_viewcertificate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(certificate_activity, ViewCertificate.class);
                intent.putExtra("certificate", certificate_array.get(position).getUrl().replaceAll("\\\\", "//").replace(" ", "%20"));
                certificate_activity.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return certificate_array.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title, tv_type;
        LinearLayout ll_viewcertificate;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_title = itemView.findViewById(R.id.tv_title);
            ll_viewcertificate = itemView.findViewById(R.id.ll_viewcertificate);

        }
    }

}
