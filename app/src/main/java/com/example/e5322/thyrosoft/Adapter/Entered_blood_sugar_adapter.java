package com.example.e5322.thyrosoft.Adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.R;

public class Entered_blood_sugar_adapter extends RecyclerView.Adapter<Entered_blood_sugar_adapter.ViewHolder> {
    @NonNull
    @Override
    public Entered_blood_sugar_adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_ll_view_blood_sugar_, viewGroup, false);
        return new Entered_blood_sugar_adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Entered_blood_sugar_adapter.ViewHolder viewHolder, int position) {
        viewHolder.patientName.setText(""+getPatientname(position));
    }

    private String getPatientname(int position) {
        String str= "";
        if(position == 0){
            str="Pooja Shelake (25/F)";
        }else  if(position == 1){
            str="Nitin Bhandari (26/M)";
        }else  if(position == 2){
            str="Nityanand Khilari (27/M)";
        }else  if(position == 3){
            str="Tejas Telawane (28/M)";
        }else  if(position == 4){
            str="Vikas Mohite (30/M)";
        }
        return str;
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView patientName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.patientName = (TextView) itemView.findViewById(R.id.patientName);
        }
    }
}
