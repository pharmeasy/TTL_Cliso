package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.Previewhandbill_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.HandbillsResponse;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class HandbillAdapter extends RecyclerView.Adapter<HandbillAdapter.ViewHolder> {

    Activity activity;
    private ArrayList<HandbillsResponse.HandbillsByCleintBean> handbillBeanArrayList;
    String image_url;

    public HandbillAdapter(Activity activity, ArrayList<HandbillsResponse.HandbillsByCleintBean> handbillBeanArrayList) {
        this.activity = activity;
        this.handbillBeanArrayList = handbillBeanArrayList;
    }

    @NonNull
    @Override
    public HandbillAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_template, viewGroup, false);
        return new HandbillAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HandbillAdapter.ViewHolder viewHolder, final int position) {

        image_url = handbillBeanArrayList.get(position).getImageurl();

        if (!image_url.isEmpty()) {
            GlobalClass.DisplayImgWithPlaceholderFromURL(activity, image_url.replace("\\", "/"), viewHolder.imageView, R.drawable.default_img);
        }


        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image_URL = handbillBeanArrayList.get(position).getImageurl();
                if (!GlobalClass.isNull(image_URL)) {
                    Intent intent = new Intent(activity, Previewhandbill_Activity.class);
                    intent.putExtra(Constants.IMAG_URL, handbillBeanArrayList.get(position).getImageurl());
                    intent.putExtra(Constants.B_NAME, handbillBeanArrayList.get(position).getName());
                    intent.putExtra(Constants.B_MOB, handbillBeanArrayList.get(position).getMobile());
                    intent.putExtra(Constants.B_EMAIL, handbillBeanArrayList.get(position).getEmail());
                    intent.putExtra(Constants.B_ADDR, handbillBeanArrayList.get(position).getAddress());
                    intent.putExtra(Constants.B_PINCODE, handbillBeanArrayList.get(position).getPincode());
                    intent.putExtra(Constants.IMGID, handbillBeanArrayList.get(position).getImgid());
                    intent.putExtra(Constants.FROMCOME, "adapter");
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return handbillBeanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.img_template);

        }
    }
}
