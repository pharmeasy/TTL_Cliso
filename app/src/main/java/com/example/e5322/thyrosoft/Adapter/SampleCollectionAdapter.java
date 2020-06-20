package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.SourceILSModel.LABS;

import java.util.ArrayList;

class SampleCollectionAdapter extends RecyclerView.Adapter<SampleCollectionAdapter.ViewHolder> {
    Context mContext;
    ArrayList<LABS> LabDetailsArraList;
    private OnItemClickListener onItemClickListener;

    public SampleCollectionAdapter(ManagingTabsActivity mContext, ArrayList<LABS> LabDetailsArraList) {
        this.mContext = mContext;
        this.LabDetailsArraList = LabDetailsArraList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void filteredArraylist(ArrayList<LABS> LabDetailsArraList) {

        this.LabDetailsArraList = LabDetailsArraList;
    }


    @NonNull
    @Override
    public SampleCollectionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.spinner_item_ll, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final SampleCollectionAdapter.ViewHolder holder, final int position) {
        holder.text1.setText(LabDetailsArraList.get(position).getLabName() + " - " + LabDetailsArraList.get(position).getClientid());
        if (LabDetailsArraList.get(position).getStatus().equals("Y")) {
            holder.text1.setTextColor(mContext.getResources().getColor(R.color.green));
        } else {
            holder.text1.setTextColor(mContext.getResources().getColor(R.color.black));
        }
        holder.text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ItemName = holder.text1.getText().toString();
                if (onItemClickListener != null) {
//                    onItemClickListener.onPassSgcName(ItemName);
                    onItemClickListener.onPassSgcID(LabDetailsArraList.get(position));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return LabDetailsArraList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView text1;

        public ViewHolder(View itemView) {
            super(itemView);
            text1 = (TextView) itemView.findViewById(R.id.textspnner);
        }
    }


    public interface OnItemClickListener {
//        void onPassSgcName(String name);
        void onPassSgcID(LABS pos);
    }

}
