package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.BroadcastDetailsActivity;
import com.example.e5322.thyrosoft.Models.ResponseModels.GetBroadcastsResponseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class BroadcastAdapter extends RecyclerView.Adapter<BroadcastAdapter.MyViewholder> {

    private final Activity mActivity;
    private final ArrayList<GetBroadcastsResponseModel.MessagesBean> messagesArrayList;

    public BroadcastAdapter(Activity activity, ArrayList<GetBroadcastsResponseModel.MessagesBean> messages) {
        mActivity = activity;
        messagesArrayList = messages;
    }

    public class MyViewholder extends RecyclerView.ViewHolder {
        LinearLayout parent_ll;
        TextView tvHeader, tvPostedOn, tvPostedBy;

        public MyViewholder(@NonNull View itemView) {
            super(itemView);
            parent_ll = itemView.findViewById(R.id.parent_ll);
            tvPostedOn = itemView.findViewById(R.id.tvPostedOn);
            tvPostedBy = itemView.findViewById(R.id.tvPostedBy);
            tvHeader = itemView.findViewById(R.id.tvHeader);
        }
    }

    @NonNull
    @Override
    public BroadcastAdapter.MyViewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View v = inflater.inflate(R.layout.broadcat_single_row, viewGroup, false);
        BroadcastAdapter.MyViewholder vh = new BroadcastAdapter.MyViewholder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull BroadcastAdapter.MyViewholder holder, final int position) {
        holder.tvHeader.setText(messagesArrayList.get(position).getTitle());
        holder.tvPostedBy.setText(messagesArrayList.get(position).getEnterBy());
        holder.tvPostedOn.setText(messagesArrayList.get(position).getNoticeDate());
        holder.parent_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, BroadcastDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("BroadcastMessage", messagesArrayList.get(position));
                intent.putExtras(bundle);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messagesArrayList.size();
    }
}