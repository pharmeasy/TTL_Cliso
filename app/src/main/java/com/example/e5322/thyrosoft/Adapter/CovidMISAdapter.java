package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import com.example.e5322.thyrosoft.Activity.CovidEditActivity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.Covidmis_response;
import com.example.e5322.thyrosoft.R;
import com.rd.PageIndicatorView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class CovidMISAdapter extends RecyclerView.Adapter<CovidMISAdapter.Viewholder> {
    Context context;
    List<Covidmis_response.OutputBean> covidMISmodelList;
    Activity activity;
    List<String> doclist = new ArrayList<>();

    public CovidMISAdapter(Context context, List<Covidmis_response.OutputBean> covidMISmodelList, Activity activity) {
        this.context = context;
        this.covidMISmodelList = covidMISmodelList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public CovidMISAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_covidmis, viewGroup, false);
        return new CovidMISAdapter.Viewholder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CovidMISAdapter.Viewholder viewholder, int i) {
        final Covidmis_response.OutputBean covidMISmodel = covidMISmodelList.get(i);


        GlobalClass.SetText(viewholder.txt_name, covidMISmodel.getPatientName().trim());
        GlobalClass.SetText(viewholder.txt_mob, covidMISmodel.getMobile().trim());


        if (!GlobalClass.isNull(covidMISmodel.getAmount_Collected())) {
            viewholder.txt_amt.setVisibility(View.VISIBLE);
            GlobalClass.SetText(viewholder.txt_amt, "Rs " + covidMISmodel.getAmount_Collected());
        } else {
            viewholder.txt_amt.setVisibility(View.GONE);
        }

        if (!GlobalClass.isNull(covidMISmodel.getEntryDate().trim())) {
            GlobalClass.SetText(viewholder.txt_time, covidMISmodel.getEntryDate().trim());
        }


        if (!GlobalClass.isNull(covidMISmodel.getStatusName())) {
            GlobalClass.SetText(viewholder.txt_ccc, covidMISmodel.getStatusName());
        }

        if (!GlobalClass.isNull(covidMISmodel.getStatusName()) && covidMISmodel.getStatusName().equalsIgnoreCase("REJECTED") && !GlobalClass.isNull(covidMISmodel.getRemarks())) {
            viewholder.lin_reason.setVisibility(View.VISIBLE);
            GlobalClass.SetText(viewholder.txt_reason, covidMISmodel.getRemarks().trim());
        } else {
            viewholder.lin_reason.setVisibility(View.GONE);
        }

        try {
            if (!GlobalClass.isNull(covidMISmodel.getStatusName()) &&  covidMISmodel.getStatusName().equalsIgnoreCase("REJECTED")) {
                viewholder.btn_resubmit.setVisibility(View.VISIBLE);
            } else {
                viewholder.btn_resubmit.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        viewholder.btn_resubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CovidEditActivity.class);
                intent.putExtra("Mobile", covidMISmodel.getMobile());
                intent.putExtra("name", covidMISmodel.getPatientName());
                intent.putExtra("UniqueId", covidMISmodel.getUniqueId());
                intent.putExtra("amtcoll", covidMISmodel.getAmount_Collected());
                context.startActivity(intent);
            }
        });

        viewholder.btn_viewupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doclist.clear();
                String url = "";
                if (!GlobalClass.isNull(covidMISmodel.getPrescription())) {
                    url = covidMISmodel.getPrescription().replaceAll("\\\\", "//");
                    if (url != null && !url.isEmpty()) {
                        doclist.add(url);
                    }
                }

                String adharurl = "";
                String adharurl2 = "";
                if (!GlobalClass.isNull(covidMISmodel.getAadhar())) {
                    adharurl = covidMISmodel.getAadhar().replaceAll("\\\\", "//");
                    doclist.add(adharurl);
                }

                if (!GlobalClass.isNull(covidMISmodel.getAadhar1())) {
                    adharurl2 = covidMISmodel.getAadhar1().replaceAll("\\\\", "//");
                    doclist.add(adharurl2);
                }

                String trf_url = "";
                String trf_url2 = "";

                if (!GlobalClass.isNull(covidMISmodel.getTrf())) {
                    trf_url = covidMISmodel.getTrf().replaceAll("\\\\", "//");
                    doclist.add(trf_url);
                }

                if (!GlobalClass.isNull(covidMISmodel.getTrf1())) {
                    trf_url2 = covidMISmodel.getTrf1().replaceAll("\\\\", "//");
                    doclist.add(trf_url2);

                }

                String vial_url = "";
                if (!GlobalClass.isNull(covidMISmodel.getVialImage())) {
                    vial_url = covidMISmodel.getVialImage().replaceAll("\\\\", "//");
                    doclist.add(vial_url);
                }

                String other_url = "";
                String other_url1 = "";


                if (!GlobalClass.isNull(covidMISmodel.getOther())) {
                    other_url = covidMISmodel.getOther().replaceAll("\\\\", "//");
                    doclist.add(other_url);

                }
                if (!GlobalClass.isNull(covidMISmodel.getOther1())) {
                    other_url1 = covidMISmodel.getOther1().replaceAll("\\\\", "//");
                    doclist.add(other_url1);

                }

                setviewpager(doclist);
            }
        });

    }

    @Override
    public int getItemCount() {
        return covidMISmodelList.size();
    }

    public void filterList(ArrayList<Covidmis_response.OutputBean> filterdNames) {
        this.covidMISmodelList = filterdNames;
        notifyDataSetChanged();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView txt_name, txt_amt, txt_mob, txt_ccc, txt_time, txt_reason;
        Button btn_resubmit, btn_viewupload;
        LinearLayout lin_reason;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            btn_resubmit = itemView.findViewById(R.id.btn_resubmit);
            btn_viewupload = itemView.findViewById(R.id.btn_viewupload);
            txt_name = itemView.findViewById(R.id.patientName);
            txt_mob = itemView.findViewById(R.id.txt_mobile);
            txt_amt = itemView.findViewById(R.id.txt_amt);
            txt_ccc = itemView.findViewById(R.id.txt_ccc);
            txt_reason = itemView.findViewById(R.id.txt_reason);
            txt_time = itemView.findViewById(R.id.txt_time);
            lin_reason = itemView.findViewById(R.id.lin_reason);
        }
    }

    public void setviewpager(List<String> imagelist) {
        final Dialog dialog = new Dialog(context);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.preview_dialog);
        dialog.getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);

        ImageView ic_close = (ImageView) dialog.findViewById(R.id.img_close);
        ic_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        ViewPager viewPager = (ViewPager) dialog.findViewById(R.id.viewPager);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(context, imagelist);
        viewPager.setAdapter(viewPagerAdapter);

        final PageIndicatorView pageIndicatorView = dialog.findViewById(R.id.pageIndicatorView);
        if (GlobalClass.CheckArrayList(imagelist)&& imagelist.size() > 1) {
            pageIndicatorView.setVisibility(View.VISIBLE);
            pageIndicatorView.setCount(imagelist.size()); // specify total count of indicators
            pageIndicatorView.setSelection(0);
            pageIndicatorView.setSelectedColor(context.getResources().getColor(R.color.maroon));
        } else {
            pageIndicatorView.setVisibility(View.GONE);
        }


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {/*empty*/}

            @Override
            public void onPageSelected(int position) {
                pageIndicatorView.setSelection(position);
                pageIndicatorView.setSelectedColor(context.getResources().getColor(R.color.maroon));
            }

            @Override
            public void onPageScrollStateChanged(int state) {/*empty*/}
        });

        dialog.setCancelable(true);
        dialog.show();
    }
}
