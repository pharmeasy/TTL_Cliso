package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Fragment.CampIntimation.Camp_Details_fragment;
import com.example.e5322.thyrosoft.Models.Camp_Intimatgion_List_Model;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class CampIntimation_Adapter extends RecyclerView.Adapter<CampIntimation_Adapter.ViewHolder>  {

    Activity activity;
    ArrayList<Camp_Intimatgion_List_Model> campIntimatgionListModels;
    public CampIntimation_Adapter(Activity camp_intimation, ArrayList<Camp_Intimatgion_List_Model> campIntimatgionList) {
      this.activity=camp_intimation;
      this.campIntimatgionListModels=campIntimatgionList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_recycler_camp_view, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        int position = i+1;
        viewHolder.camp_sr.setText(String.valueOf(position));
        viewHolder.camp_date.setText(campIntimatgionListModels.get(i).getCamp_date());
        viewHolder.camp_profile.setText(campIntimatgionListModels.get(i).getCamp_profile());
        viewHolder.camp_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camp_Details_fragment fragmentB=new Camp_Details_fragment();
                Bundle bundle=new Bundle();
                bundle.putString("camp_date",campIntimatgionListModels.get(i).getCamp_date());
                bundle.putString("camp_profile",campIntimatgionListModels.get(i).getCamp_profile());
                bundle.putString("camp_rate",campIntimatgionListModels.get(i).getCamp_rate());
                bundle.putString("camp_days",campIntimatgionListModels.get(i).getSample_per_day());
                bundle.putString("camp_address",campIntimatgionListModels.get(i).getCamp_address());
                bundle.putString("camp_duration",campIntimatgionListModels.get(i).getCamp_duration());
                fragmentB.setArguments(bundle);
                FragmentManager manager=activity.getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.fragment_mainLayout,fragmentB).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return campIntimatgionListModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView camp_sr,camp_date,camp_profile;
        LinearLayout camp_ll;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            camp_sr=(TextView)itemView.findViewById(R.id.camp_sr);
            camp_date=(TextView)itemView.findViewById(R.id.camp_date);
            camp_profile=(TextView)itemView.findViewById(R.id.camp_profile);
            camp_ll=(LinearLayout)itemView.findViewById(R.id.camp_ll);
        }
    }
}
