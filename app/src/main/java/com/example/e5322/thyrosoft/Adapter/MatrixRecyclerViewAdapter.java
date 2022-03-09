package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.EscalationMatrixResponseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class MatrixRecyclerViewAdapter extends RecyclerView.Adapter<MatrixRecyclerViewAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<EscalationMatrixResponseModel.MatrixDetails> matrixDetails;

    public MatrixRecyclerViewAdapter(Activity activity, ArrayList<EscalationMatrixResponseModel.MatrixDetails> modelClassList) {
        this.activity = activity;
        this.matrixDetails = modelClassList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_escalation_matrix, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final EscalationMatrixResponseModel.MatrixDetails data = matrixDetails.get(position);
        GlobalClass.SetText(holder.tv_escalation_type, data.getType());
        GlobalClass.SetText(holder.tv_incharge_email, data.getIncharge());
        GlobalClass.SetText(holder.tv_level1, data.getL1());
        GlobalClass.SetText(holder.tv_level2, data.getL2());
        GlobalClass.SetText(holder.tv_level3, data.getL3());
    }

    @Override
    public int getItemCount() {
        return matrixDetails.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_escalation_type, tv_incharge_email, tv_level1, tv_level2, tv_level3;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_escalation_type = itemView.findViewById(R.id.tv_escalation_type);
            tv_incharge_email = itemView.findViewById(R.id.tv_incharge_email);
            tv_level1 = itemView.findViewById(R.id.tv_level1);
            tv_level2 = itemView.findViewById(R.id.tv_level2);
            tv_level3 = itemView.findViewById(R.id.tv_level3);
        }
    }
}
