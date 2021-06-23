package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;

public class BrandAdapter extends RecyclerView.Adapter<BrandAdapter.ViewHolder> {

    private ArrayList<BaseModel.BrandDtlsDTO> getMainData;
    ArrayList<String> brandName;
    private Activity activity;
    private int lastCheckedPosition = -1;
    OnClickListener onClickListener;
    String typeName;
    private String brandnametopass = "";

    public BrandAdapter(Activity activity, ArrayList<String> brandName, ArrayList<BaseModel.BrandDtlsDTO> getMainData, String typeName) {
        this.brandName = brandName;
        this.getMainData = getMainData;
        this.activity = activity;
        this.typeName = typeName;

    }


    @Override
    public BrandAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.brand_item, parent, false);
        return new BrandAdapter.ViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(@NonNull final BrandAdapter.ViewHolder holder, final int position) {


//        holder.rb_brandname.setText("" + brandName.get(position));
        setBrandName(holder.rb_brandname, brandName.get(position));

        holder.rb_brandname.setChecked(position == lastCheckedPosition);

        if (brandName.size() == 1) {
            holder.rb_brandname.setChecked(true);
            if (onClickListener != null) {
                onClickListener.onchecked(brandnametopass, holder.tv_brandrate.getText().toString());
            }
        }

        holder.rb_brandname.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (onClickListener != null) {
                        onClickListener.onchecked(brandnametopass, holder.tv_brandrate.getText().toString());
                    }
                }
            }
        });


        setRate(holder.tv_brandrate, holder.rb_brandname);
        setUrl(holder.tv_Url, holder.rb_brandname);

    }

    private void setBrandName(RadioButton rb_brandname, String position) {


        if (GlobalClass.isArraylistNotNull(getMainData)) {
            for (int i = 0; i < getMainData.size(); i++) {
                if (!GlobalClass.isNull(getMainData.get(i).getAlias())) {
                    if (position.equalsIgnoreCase(getMainData.get(i).getAlias())) {
                        if (!GlobalClass.isNull(getMainData.get(i).getFullName())) {
                            rb_brandname.setText(getMainData.get(i).getFullName());
                        } else {
                            rb_brandname.setText(position);
                        }
                        brandnametopass = position;
                        break;

                    }
                }

            }
        }
    }


    private void setUrl(TextView tv_url, RadioButton rb_brandname) {
        String url = "";
        for (int i = 0; i < getMainData.size(); i++) {
            if (getMainData.get(i).getFullName().equalsIgnoreCase(rb_brandname.getText().toString())) {
                if (!GlobalClass.isNull(getMainData.get(i).getRPTFile())) {
                    url = getMainData.get(i).getRPTFile();
                }
            }
        }

        if (!GlobalClass.isNull(url)) {
            tv_url.setVisibility(View.VISIBLE);
            tv_url.setText(Html.fromHtml("<u>" + "View Sample" + "</u>"));
        } else {
            tv_url.setVisibility(View.GONE);

        }
        final String finalUrl = url;
        tv_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(finalUrl));
                activity.startActivity(browserIntent);
            }
        });

    }

    private void setRate(TextView tv_brandrate, RadioButton rb_brandname) {
        int rate = 0;
        for (int i = 0; i < getMainData.size(); i++) {
            if (!GlobalClass.isNull(getMainData.get(i).getFullName())){
                if (getMainData.get(i).getFullName().equalsIgnoreCase(rb_brandname.getText().toString())) {
                    if (typeName.equalsIgnoreCase("ILS")) {
                        if (!GlobalClass.isNull(getMainData.get(i).getILSRate())) {
                            rate = rate + Integer.parseInt(getMainData.get(i).getILSRate());
                        }
                    } else {
                        if (!GlobalClass.isNull(getMainData.get(i).getBrandRate())) {
                            rate = rate + Integer.parseInt(getMainData.get(i).getBrandRate());
                        }
                    }
                }
            }

        }
        tv_brandrate.setText("Rs: " + rate + "/-");
    }


    @Override
    public int getItemCount() {
        return brandName.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv_brandrate, tv_Url;
        RadioButton rb_brandname;


        public ViewHolder(View itemView) {
            super(itemView);
            tv_brandrate = itemView.findViewById(R.id.tv_brandrate);
            tv_Url = itemView.findViewById(R.id.tv_Url);
            rb_brandname = itemView.findViewById(R.id.rb_brandname);

            rb_brandname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int copyOfLastCheckedPosition = lastCheckedPosition;
                    lastCheckedPosition = getAdapterPosition();
                    notifyItemChanged(copyOfLastCheckedPosition);
                    notifyItemChanged(lastCheckedPosition);

                }
            });

        }
    }

    public void setOnItemClickListener(OnClickListener clickListener) {
        this.onClickListener = clickListener;
    }

    public interface OnClickListener {
        void onchecked(String brand, String rate);
    }


}

