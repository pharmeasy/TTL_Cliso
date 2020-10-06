package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.HandBillform;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.ResponseModels.TemplateResponse;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.ViewHolder> {

    Activity activity;
    private ArrayList<TemplateResponse.HandbillBean> handbillBeanArrayList;
    String image_url;

    public TemplateAdapter(Activity activity, ArrayList<TemplateResponse.HandbillBean> handbillBeanArrayList) {
        this.activity = activity;
        this.handbillBeanArrayList = handbillBeanArrayList;
    }

    @NonNull
    @Override
    public TemplateAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_template, viewGroup, false);
        return new TemplateAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TemplateAdapter.ViewHolder viewHolder, final int position) {
        image_url = handbillBeanArrayList.get(position).getUrl();

        if (!image_url.isEmpty()) {
            GlobalClass.DisplayImgWithPlaceholderFromURL(activity, image_url.replace("\\", "/"), viewHolder.imageView, R.drawable.default_img);
        }

        viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String image_URL = handbillBeanArrayList.get(position).getUrl();
                if (!GlobalClass.isNull(image_URL)) {
                    activity.startActivity(new Intent(activity, HandBillform.class).putExtra(Constants.IMAG_URL, image_URL).setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP));
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
