package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BloodSugarEntries;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class BS_MISAdapter extends RecyclerView.Adapter<BS_MISAdapter.ViewHolder> {

    Activity activity;
    private ArrayList<BloodSugarEntries> bloodSugarEntries;

    public BS_MISAdapter(Activity activity, ArrayList<BloodSugarEntries> bloodSugarEntries) {
        this.activity = activity;
        this.bloodSugarEntries = bloodSugarEntries;
    }

    @NonNull
    @Override
    public BS_MISAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_blood_sugar_item, viewGroup, false);
        return new BS_MISAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BS_MISAdapter.ViewHolder viewHolder, final int position) {
        viewHolder.patientName.setText("" + bloodSugarEntries.get(position).getName().toUpperCase() + "(" + bloodSugarEntries.get(position).getAge() + "/" + bloodSugarEntries.get(position).getGender().toUpperCase() + ")");
        String mob_no = bloodSugarEntries.get(position).getMobile();
        if (mob_no != null && !mob_no.isEmpty()) {
            viewHolder.tv_mob.setVisibility(View.VISIBLE);
            viewHolder.tv_mob.setText(mob_no);
        } else viewHolder.tv_mob.setVisibility(View.GONE);
        viewHolder.tv_testNameValue.setText(bloodSugarEntries.get(position).getTest() + " - " + bloodSugarEntries.get(position).getTestValue());

        String systolicValue = bloodSugarEntries.get(position).getSBP();
        String diastolicValue = bloodSugarEntries.get(position).getDBP();

        if (GlobalClass.isNull(systolicValue) || GlobalClass.isNull(diastolicValue)) {
            viewHolder.ll_bpValues.setVisibility(View.GONE);
        } else {
            viewHolder.ll_bpValues.setVisibility(View.VISIBLE);
            viewHolder.tv_systolicBP.setText(systolicValue);
            viewHolder.tv_diastolicBP.setText(diastolicValue);
        }

        String collAmount = bloodSugarEntries.get(position).getAmountCollected();
        if (!GlobalClass.isNull(collAmount))
            viewHolder.tv_collAmount.setText(activity.getString(R.string.rupeeSymbol) + " " + collAmount);
        else
            viewHolder.tv_collAmount.setText("");

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = bloodSugarEntries.get(position).getFileUrl().replaceAll("\\\\", "//");
//                url = url.replaceAll("http:", "https:");
                if (url != null && !url.isEmpty())
                    GlobalClass.showImageDialog(activity, null, url, 2);
                else
                    Global.showCustomToast(activity, "Image not found");
            }
        });
    }

    @Override
    public int getItemCount() {
        return bloodSugarEntries.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView patientName, tv_mob, tv_testNameValue, tv_collAmount, tv_systolicBP, tv_diastolicBP;
        ImageView imageView;
        LinearLayout ll_bpValues;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            patientName = (TextView) itemView.findViewById(R.id.patientName);
            tv_mob = (TextView) itemView.findViewById(R.id.tv_mob);
            tv_testNameValue = (TextView) itemView.findViewById(R.id.tv_testNameValue);
            tv_collAmount = (TextView) itemView.findViewById(R.id.tv_remarks);
            tv_systolicBP = (TextView) itemView.findViewById(R.id.tv_systolicBP);
            tv_diastolicBP = (TextView) itemView.findViewById(R.id.tv_diastolicBP);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            ll_bpValues = (LinearLayout) itemView.findViewById(R.id.ll_bpValues);
        }
    }
}
