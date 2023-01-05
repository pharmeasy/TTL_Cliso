package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PackageResponseModel;
import com.example.e5322.thyrosoft.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PackageAdapter extends RecyclerView.Adapter<PackageAdapter.ViewHolder> {
    Activity activity;
    ArrayList<PackageResponseModel.ResultDTO.WidgetDTO> widgetDTOS;

    public PackageAdapter(Activity activity, ArrayList<PackageResponseModel.ResultDTO.WidgetDTO> widgetDTOS) {
        this.activity = activity;
        this.widgetDTOS = widgetDTOS;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.lay_package_details, viewGroup, false);
        return new PackageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        PackageResponseModel.ResultDTO.WidgetDTO widgetDTO = widgetDTOS.get(i);
        String str_tip_heading = widgetDTO.getTip_heading();
        String str_tip_description = widgetDTO.getTip_description();
        String MarginTxt = String.valueOf(widgetDTO.getMargin());

        GlobalClass.SetText(viewHolder.txt_name, String.valueOf(Html.fromHtml("<b>" + widgetDTO.getName() + "</b>")));
        GlobalClass.SetText(viewHolder.txt_rate, "B2C: " + widgetDTO.getB2c_price() + " | B2B: " + widgetDTO.getB2b_price());
        GlobalClass.SetText(viewHolder.btn_viewdeatils, widgetDTO.getButton_text());
        if (widgetDTO.getImage_url() != null && widgetDTO.getImage_url().length() > 0) {
            Picasso.get().load(widgetDTO.getImage_url()).into(viewHolder.img_banner);
        }

        if (GlobalClass.checkEqualIgnoreCase(Global.getAccessType(activity), "ADMIN")) {
            GlobalClass.SetText(viewHolder.txt_rate, "B2C: " + widgetDTO.getB2c_price() + " | B2B: " + widgetDTO.getB2b_price());
        } else {
            GlobalClass.SetText(viewHolder.txt_rate, "B2C: " + widgetDTO.getB2c_price());
        }


        if (widgetDTO.getButton_url() != null && !widgetDTO.getButton_url().isEmpty() && widgetDTO.getButton_url().length() > 0) {
            viewHolder.btn_viewdeatils.setVisibility(View.VISIBLE);
        }
        viewHolder.btn_viewdeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (widgetDTO.getButton_url() != null && !widgetDTO.getButton_url().isEmpty() && widgetDTO.getButton_url().length() > 0) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(widgetDTO.getButton_url()));
                    activity.startActivity(browserIntent);

                }
            }
        });

        if (widgetDTO.getTAT() != null) {
            viewHolder.txt_tat.setVisibility(View.VISIBLE);
            GlobalClass.SetText(viewHolder.txt_tat, "TAT: " + widgetDTO.getTAT());
        }else {
            viewHolder.txt_tat.setVisibility(View.GONE);
        }

        if (GlobalClass.checkEqualIgnoreCase(Global.getAccessType(activity), "ADMIN")) {
            if (!MarginTxt.isEmpty()) {
                viewHolder.txt_margin.setVisibility(View.VISIBLE);
                GlobalClass.SetText(viewHolder.txt_margin, "MARGIN: " + MarginTxt + "/-");
            } else {
                viewHolder.txt_margin.setVisibility(View.GONE);
            }
        } else {
            viewHolder.txt_margin.setVisibility(View.GONE);
        }


        if (widgetDTO.getTip_heading() != null && widgetDTO.getTip_description() != null) {
            viewHolder.ll_tip.setVisibility(View.VISIBLE);
            viewHolder.txt_tip.setText(Html.fromHtml("<b>" + str_tip_heading + ": " + "</b>" + str_tip_description));
        } else {
            viewHolder.ll_tip.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return widgetDTOS.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_name, txt_rate, txt_tat, txt_margin, txt_tip;
        Button btn_viewdeatils;
        LinearLayout ll_tip;
        ImageView img_banner;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.txt_name);
            txt_rate = itemView.findViewById(R.id.txt_rate);
            txt_margin = itemView.findViewById(R.id.txt_margin);
            btn_viewdeatils = itemView.findViewById(R.id.btn_viewdeatils);
            img_banner = itemView.findViewById(R.id.img_banner);
            ll_tip = itemView.findViewById(R.id.ll_tip);
            txt_tip = itemView.findViewById(R.id.txt_tip);
            txt_tat = itemView.findViewById(R.id.txt_tat);
        }
    }
}
