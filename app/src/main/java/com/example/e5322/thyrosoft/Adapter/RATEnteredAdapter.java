package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.Activity.frags.AnitbodyEnteredFrag;
import com.example.e5322.thyrosoft.Fragment.RATEnteredFrag;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.DateUtils;
import com.example.e5322.thyrosoft.Models.RATEnteredResponseModel;
import com.example.e5322.thyrosoft.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class RATEnteredAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    Passdata passdata;
    ArrayList<RATEnteredResponseModel.PatientDETAILSBean> patientDETAILSBeans;
    Activity activity;
    String mcurrentTIME;
    private int timeflag = 0;
    private int timespan;


    public RATEnteredAdapter(RATEnteredFrag ratEnteredFrag, ArrayList<RATEnteredResponseModel.PatientDETAILSBean> patientDETAILSBeans, String pcurrentTIME, int timespan) {
        this.context = ratEnteredFrag.getContext();
        activity = ratEnteredFrag.getActivity();
        this.patientDETAILSBeans = patientDETAILSBeans;
        this.mcurrentTIME = pcurrentTIME;
        this.timespan = timespan;

    }


    public RATEnteredAdapter(AnitbodyEnteredFrag anitbodyEnteredFrag, ArrayList<RATEnteredResponseModel.PatientDETAILSBean> patientDETAILSBeans, String pcurrentTIME,int timespan) {
        this.context = anitbodyEnteredFrag.getContext();
        activity = anitbodyEnteredFrag.getActivity();
        this.patientDETAILSBeans = patientDETAILSBeans;
        this.mcurrentTIME = pcurrentTIME;
        this.timespan = timespan;

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name, tv_mobile, tv_barcode, tv_view, tv_siitime, tv_resulttime, txt_txt, tv_result;
        Button btn_upload;
        CountDownTimer timer;
        LinearLayout lin_result;


        public MyViewHolder(View view) {
            super(view);
            txt_txt = view.findViewById(R.id.txt_txt);
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
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.enteredrat_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int i) {
        final MyViewHolder userViewHolder = (MyViewHolder) viewHolder;


        userViewHolder.tv_name.setText(patientDETAILSBeans.get(i).getName());
        userViewHolder.tv_mobile.setText(patientDETAILSBeans.get(i).getMobile());
        userViewHolder.tv_barcode.setText(patientDETAILSBeans.get(i).getBarcode().toUpperCase());


        try {
            if (patientDETAILSBeans.get(i).getStatus() == 0) {
                userViewHolder.btn_upload.setText("Upload Cassette Image");
                userViewHolder.tv_view.setVisibility(View.GONE);
                userViewHolder.tv_result.setVisibility(View.GONE);
                userViewHolder.lin_result.setVisibility(View.GONE);
            } else if (patientDETAILSBeans.get(i).getStatus() == 1) {
                userViewHolder.tv_view.setVisibility(View.VISIBLE);
                userViewHolder.tv_view.setText(Html.fromHtml("<u>" + "Upload 1" + "</u>"));
                userViewHolder.tv_view.setTextColor(Color.BLUE);
                userViewHolder.tv_result.setVisibility(View.GONE);
                userViewHolder.lin_result.setVisibility(View.GONE);
//                userViewHolder.btn_upload.setText("Please wait to upload result image");
                SetTimetoBtn(userViewHolder.btn_upload, patientDETAILSBeans.get(i));
            } else if (patientDETAILSBeans.get(i).getStatus() == 2) {
                userViewHolder.tv_view.setVisibility(View.VISIBLE);
                userViewHolder.tv_view.setText(Html.fromHtml("<u>" + "Upload 2" + "</u>"));
                userViewHolder.tv_view.setTextColor(Color.BLUE);
                userViewHolder.tv_barcode.setVisibility(View.VISIBLE);
                userViewHolder.tv_barcode.setText(Html.fromHtml("<u>" + "Upload 1" + "</u>"));
                userViewHolder.tv_barcode.setTextColor(Color.BLUE);




                userViewHolder.btn_upload.setVisibility(View.GONE);
                userViewHolder.tv_siitime.setVisibility(View.VISIBLE);
                userViewHolder.tv_resulttime.setVisibility(View.VISIBLE);
                userViewHolder.lin_result.setVisibility(View.VISIBLE);
                if (!GlobalClass.isNull(patientDETAILSBeans.get(i).getResult())) {
                    if (patientDETAILSBeans.get(i).getResult().equalsIgnoreCase("POSITIVE")) {
                        userViewHolder.tv_result.setTextColor(context.getResources().getColor(R.color.darkgreen));
                    } else {
                        userViewHolder.tv_result.setTextColor(Color.RED);
                    }
                    userViewHolder.tv_result.setText(patientDETAILSBeans.get(i).getResult().trim());
                }

                if (!GlobalClass.isNull(patientDETAILSBeans.get(i).getUploadTIME())) {
                    userViewHolder.tv_siitime.setText("Sample Insertion Time(SIT): " + patientDETAILSBeans.get(i).getUploadTIME());
                } else {
                    userViewHolder.tv_siitime.setText("Sample Insertion Time(SIT): " + "");
                }
                if (!GlobalClass.isNull(patientDETAILSBeans.get(i).getResultTIME())) {
                    userViewHolder.tv_resulttime.setText("Actual Result Time: " + patientDETAILSBeans.get(i).getResultTIME());
                } else {
                    userViewHolder.tv_resulttime.setText("Actual Result Time: " + "");
                }
            } else if (patientDETAILSBeans.get(i).getStatus() == 3) {
                userViewHolder.btn_upload.setVisibility(View.GONE);
                userViewHolder.tv_view.setVisibility(View.VISIBLE);
                userViewHolder.tv_siitime.setVisibility(View.VISIBLE);
                userViewHolder.lin_result.setVisibility(View.GONE);
                if (!GlobalClass.isNull(patientDETAILSBeans.get(i).getUploadTIME())) {
                    userViewHolder.tv_siitime.setText("Sample Insertion Time(SIT): " + patientDETAILSBeans.get(i).getUploadTIME());
                } else {
                    userViewHolder.tv_siitime.setText("Sample Insertion Time(SIT): " + "");
                }
                userViewHolder.tv_view.setText(Html.fromHtml("<u>" + "Upload 1" + "</u>"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userViewHolder.timer != null) {
            userViewHolder.timer.cancel();
        }

        userViewHolder.tv_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!GlobalClass.isNull(patientDETAILSBeans.get(i).getSiiURL())) {
                    GlobalClass.showImageDialog(activity, null, patientDETAILSBeans.get(i).getSiiURL().replaceAll("\\\\", "//"), 2);
                }

            }
        });

      /*  int dg = 0;
        if (i == 0) {
            dg = 2 * 60 * 1000;
        } else if (i == 1) {
            dg = 1 * 60 * 1000;
        } else {
            dg = 1 * 60 * 100;
        }
        userViewHolder.timer = new CountDownTimer(dg, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                userViewHolder.txt_txt.setText("" + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                userViewHolder.txt_txt.setText("Finished - " + i);
            }
        }.start();*/

        userViewHolder.btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userViewHolder.btn_upload.getText().toString().equalsIgnoreCase("Please wait to upload result image")) {

                } else {
                    passdata.pass(patientDETAILSBeans.get(i));
                }

            }
        });

        userViewHolder.tv_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (patientDETAILSBeans.get(i).getStatus() == 1) {
                        if (!GlobalClass.isNull(patientDETAILSBeans.get(i).getSiiURL())) {
                            GlobalClass.showImageDialog(activity, null, patientDETAILSBeans.get(i).getSiiURL().replaceAll("\\\\", "//"), 2);

                        } else {
                            Toast.makeText(context, "No file Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    } else if (patientDETAILSBeans.get(i).getStatus() == 2) {
                        if (!GlobalClass.isNull(patientDETAILSBeans.get(i).getResultURL())) {
                            GlobalClass.showImageDialog(activity, null, patientDETAILSBeans.get(i).getResultURL().replaceAll("\\\\", "//"), 2);
                        } else {
                            Toast.makeText(context, "No file Uploaded", Toast.LENGTH_SHORT).show();

                        }
                    } else if (patientDETAILSBeans.get(i).getStatus() == 3) {

                        if (!GlobalClass.isNull(patientDETAILSBeans.get(i).getSiiURL())) {
                            GlobalClass.showImageDialog(activity, null, patientDETAILSBeans.get(i).getSiiURL().replaceAll("\\\\", "//"), 2);

                        } else {
                            Toast.makeText(context, "No file Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void SetTimetoBtn(Button btn_upload, RATEnteredResponseModel.PatientDETAILSBean patientDETAILSBean) {
        if (validateTimeAfter(patientDETAILSBean.getUploadTIME())) {
            if (timeflag == 1) {
                btn_upload.setText("Upload result image");
                btn_upload.setBackground(context.getResources().getDrawable(R.drawable.btn_bg_green));
                btn_upload.setEnabled(true);
            } else if (timeflag == 2) {
                btn_upload.setText("Upload result image till " + getTimetoShowafter120(patientDETAILSBean.getUploadTIME()));
                btn_upload.setBackground(context.getResources().getDrawable(R.drawable.btn_bg));
                btn_upload.setEnabled(true);
            }

        } else {
            btn_upload.setEnabled(false);
            btn_upload.setText("Upload result between " + getTimetoShow(patientDETAILSBean.getUploadTIME()) + "-" + getTimetoShowafter(patientDETAILSBean.getUploadTIME()));
        }
    }

    private String getTimetoShow(String uploadTIME) {
        String st = "";
        try {
            Date urg = DateUtils.convertToDateWithFormat(uploadTIME, "dd-MM-yyyy HH:mm");
            Date urg_cu = DateUtils.convertToDateWithFormat(mcurrentTIME, "dd-MM-yyyy HH:mm");

            Calendar c = Calendar.getInstance();
            try {
                c.setTime(urg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            c.add(Calendar.MINUTE, 15);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            st = sdf1.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return st;
    }

    private String getTimetoShowafter120(String uploadTIME) {
        String st = "";
        try {
            Date urg = DateUtils.convertToDateWithFormat(uploadTIME, "dd-MM-yyyy HH:mm");
            Date urg_cu = DateUtils.convertToDateWithFormat(mcurrentTIME, "dd-MM-yyyy HH:mm");

            Calendar c = Calendar.getInstance();
            try {
                c.setTime(urg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            c.add(Calendar.MINUTE, timespan-30);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            st = sdf1.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return st;
    }

    private String getTimetoShowafter(String uploadTIME) {
        String st = "";
        try {
            Date urg = DateUtils.convertToDateWithFormat(uploadTIME, "dd-MM-yyyy HH:mm");
            Date urg_cu = DateUtils.convertToDateWithFormat(mcurrentTIME, "dd-MM-yyyy HH:mm");

            Calendar c = Calendar.getInstance();
            try {
                c.setTime(urg);
            } catch (Exception e) {
                e.printStackTrace();
            }
            c.add(Calendar.MINUTE, 30);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            st = sdf1.format(c.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return st;
    }

    private boolean validateTimeAfter(String uploadTIME) {
        try {
            Date urg = DateUtils.convertToDateWithFormat(uploadTIME, "dd-MM-yyyy HH:mm");
            Date urg_cu = DateUtils.convertToDateWithFormat(mcurrentTIME, "dd-MM-yyyy HH:mm");
            long difference = urg_cu.getTime() - urg.getTime();
            if (difference >= 15 * 60 * 1000 && difference <= 30 * 60 * 1000) {
                timeflag = 1;
                return true;
            } else if (difference >= 30 * 60 * 1000 && difference <= timespan * 60 * 1000) {
                timeflag = 2;
                return true;

            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
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
        void pass(RATEnteredResponseModel.PatientDETAILSBean postmaterial);
    }


}
