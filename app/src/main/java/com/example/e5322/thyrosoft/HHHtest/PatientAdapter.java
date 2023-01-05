package com.example.e5322.thyrosoft.HHHtest;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Paint;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.e5322.thyrosoft.API.Global;
import com.example.e5322.thyrosoft.Adapter.ViewPagerAdapter;
import com.example.e5322.thyrosoft.Adapter.ViewPagerImageAdapter;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.HHHtest.Model.PatientResponseModel;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DateUtils;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.rd.PageIndicatorView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class PatientAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    Context context;
    ArrayList<PatientResponseModel.PatientDetailsBean> patientDETAILSBeans;
    Activity activity;
    Passdata passdata;
    private String SIIurl = "";

    public PatientAdapter(HHHEnteredFrag hhhEnteredFrag, ArrayList<PatientResponseModel.PatientDetailsBean> patientDETAILSBeans) {
        this.context = hhhEnteredFrag.getContext();
        activity = hhhEnteredFrag.getActivity();
        this.patientDETAILSBeans = patientDETAILSBeans;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_mobile, tv_barcode, tv_view, tv_siitime, tv_resulttime, tv_result;
        Button btn_upload;
        LinearLayout lin_result;

        public MyViewHolder(View view) {
            super(view);
            tv_name = view.findViewById(R.id.tv_name);

            tv_mobile = view.findViewById(R.id.tv_mobile);
            tv_barcode = view.findViewById(R.id.tv_barcode);
            tv_view = view.findViewById(R.id.tv_view);
            btn_upload = view.findViewById(R.id.btn_upload);
            tv_siitime = view.findViewById(R.id.tv_siitime);
            tv_resulttime = view.findViewById(R.id.tv_resulttime);
            tv_result = view.findViewById(R.id.tv_result);
            lin_result = view.findViewById(R.id.lin_result);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.hhh_item, viewGroup, false);
        return new PatientAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {

        final PatientAdapter.MyViewHolder userViewHolder = (PatientAdapter.MyViewHolder) viewHolder;

        Global.setTextview(userViewHolder.tv_name, patientDETAILSBeans.get(position).getName());
        Global.setTextview(userViewHolder.tv_mobile, patientDETAILSBeans.get(position).getMobile());
        Global.setTextview(userViewHolder.tv_barcode, patientDETAILSBeans.get(position).getBarcode().toUpperCase());


        if (patientDETAILSBeans.get(position).getStatus().equalsIgnoreCase("2") || patientDETAILSBeans.get(position).getStatus().equalsIgnoreCase("1")) {
            userViewHolder.btn_upload.setVisibility(View.GONE);
        }

        userViewHolder.btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passdata.pass(patientDETAILSBeans.get(position), userViewHolder.btn_upload.getText().toString());
            }
        });

        for (int j = 0; j < patientDETAILSBeans.get(position).getUploaded().size(); j++) {
            if (GlobalClass.isNull(patientDETAILSBeans.get(position).getUploaded().get(j).getImageURL())) {
                userViewHolder.btn_upload.setText("" + patientDETAILSBeans.get(position).getUploaded().get(j).getImageNAME());
                break;
            }

        }

        try {
            for (int j = 0; j < patientDETAILSBeans.get(position).getUploaded().size(); j++) {

                if (!GlobalClass.isNull(patientDETAILSBeans.get(position).getUploaded().get(j).getImageURL())) {
                    userViewHolder.tv_view.setText(Html.fromHtml("<u>" + "View Upload" + "</u>"));
                    if (patientDETAILSBeans.get(position).getStatus().equalsIgnoreCase("2") || patientDETAILSBeans.get(position).getStatus().equalsIgnoreCase("1")) {
                        userViewHolder.lin_result.setVisibility(View.VISIBLE);
                        userViewHolder.tv_result.setText("" + DateUtils.Req_Date_Req(patientDETAILSBeans.get(position).getUploaded().get(j).getUploadedtime(), "MM/dd/yyyy hh:mm:ss a", "dd-MM-yyyy HH:mm"));
                    }
                    break;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        userViewHolder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SIIurl = "";
                    ArrayList<String> imagelist = new ArrayList<>();
                    for (int j = 0; j < patientDETAILSBeans.get(position).getUploaded().size(); j++) {
                        if (!GlobalClass.isNull(patientDETAILSBeans.get(position).getUploaded().get(j).getImageURL())) {
                            imagelist.add(patientDETAILSBeans.get(position).getUploaded().get(j).getImageURL().replaceAll("\\\\", "//"));
                        }


                    }

                    if (imagelist != null && imagelist.size() > 0) {
                        setviewpager(imagelist);
                    } else {
                        Toast.makeText(context, "No file Uploaded", Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    public void setviewpager(ArrayList<String> imagelist) {
        try {
            final Dialog maindialog = new Dialog(activity);
            maindialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            maindialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            maindialog.setContentView(R.layout.preview_dialog);
            maindialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

            final ViewPager viewPager = (ViewPager) maindialog.findViewById(R.id.viewPager);
            final ViewPagerImageAdapter viewPagerAdapter = new ViewPagerImageAdapter(activity, imagelist);
            viewPager.setAdapter(viewPagerAdapter);
            viewPagerAdapter.notifyDataSetChanged();

            final PageIndicatorView pageIndicatorView = maindialog.findViewById(R.id.pageIndicatorView);
            if (imagelist != null && imagelist.size() > 1) {
                pageIndicatorView.setVisibility(View.VISIBLE);
                pageIndicatorView.setCount(imagelist.size());
                pageIndicatorView.setSelection(0);
                pageIndicatorView.setSelectedColor(activity.getResources().getColor(R.color.maroon));
            } else {
                pageIndicatorView.setVisibility(View.GONE);
            }

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

                @Override
                public void onPageSelected(int position) {

                    pageIndicatorView.setSelection(position);
                    pageIndicatorView.setSelectedColor(activity.getResources().getColor(R.color.maroon));
                    pageIndicatorView.setUnselectedColor(activity.getResources().getColor(R.color.grey));
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


            ImageView ic_close = (ImageView) maindialog.findViewById(R.id.img_close);
            ic_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    maindialog.dismiss();
                }
            });

            maindialog.setCancelable(true);
            maindialog.show();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return patientDETAILSBeans.size();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void Click(Passdata passdata) {
        this.passdata = passdata;
    }

    public interface Passdata {
        void pass(PatientResponseModel.PatientDetailsBean postmaterial, String value);
    }


}

