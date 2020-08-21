package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.OLCcontact_Array_list;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.e5322.thyrosoft.API.Api.imgURL;

public class Company_Adapter extends RecyclerView.Adapter<Company_Adapter.ViewHolder> {

    private ArrayList<OLCcontact_Array_list> contact_array_lists_to_pass;
    private Context context1;
    private GlobalClass globalClass;

    @NonNull
    @Override
    public Company_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_single_layout, parent, false);
        return new ViewHolder(itemView);
    }

    public Company_Adapter(Context context,ArrayList<OLCcontact_Array_list> contact_array_lists_to_pass) {
        this.contact_array_lists_to_pass = contact_array_lists_to_pass;
        this.context1 = context;
        globalClass = new GlobalClass(context);
    }

    @Override
    public void onBindViewHolder(@NonNull final Company_Adapter.ViewHolder holder, final int position) {

        GlobalClass.SetText(holder.name,contact_array_lists_to_pass.get(position).getNAME());
        GlobalClass.SetText(holder.designation,contact_array_lists_to_pass.get(position).getDESIGNATION());
        GlobalClass.SetText(holder.role,"Role: " + contact_array_lists_to_pass.get(position).getROLE());
        GlobalClass.SetText(holder.email_id,contact_array_lists_to_pass.get(position).getEMAIL_ID());

        Linkify.addLinks(holder.email_id, Linkify.EMAIL_ADDRESSES);
        holder.email_id.setMovementMethod(LinkMovementMethod.getInstance());

        if (!GlobalClass.isNull(contact_array_lists_to_pass.get(position).getCATEGORY()) && contact_array_lists_to_pass.get(position).getCATEGORY().equalsIgnoreCase("MANAGER")) {
            holder.mobile_number.setVisibility(View.GONE);
        } else {
            if (!GlobalClass.isNull(contact_array_lists_to_pass.get(position).getMOBILE())) {
                holder.mobile_number.setVisibility(View.VISIBLE);
                GlobalClass.SetText(holder.mobile_number,"Mobile number: " + contact_array_lists_to_pass.get(position).getMOBILE());
            } else {
                holder.mobile_number.setVisibility(View.GONE);
            }
        }

        String url = imgURL + contact_array_lists_to_pass.get(position).getECODE() + ".jpg";
        if (!GlobalClass.isNull(url)) {
            globalClass.DisplayImage((Activity) context1, url, holder.profile);
        } else {
            Glide.with(context1)
                    .load("")
                    .placeholder(context1.getResources().getDrawable(R.drawable.userprofile))
                    .into(holder.profile);
        }
    }

    @Override
    public int getItemCount() {
        return contact_array_lists_to_pass.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile;
        TextView name, designation, role, email_id, mobile_number;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            designation = (TextView) itemView.findViewById(R.id.designation);
            role = (TextView) itemView.findViewById(R.id.role);
            email_id = (TextView) itemView.findViewById(R.id.email_id);
            mobile_number = (TextView) itemView.findViewById(R.id.mobile_number);
            profile = (ImageView) itemView.findViewById(R.id.profile);
        }
    }

}
