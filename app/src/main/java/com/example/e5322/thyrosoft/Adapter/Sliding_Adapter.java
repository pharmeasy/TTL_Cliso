package com.example.e5322.thyrosoft.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.Activity.BroadcastDataActivity;
import com.example.e5322.thyrosoft.BroadcastViewPagerActivity;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;

public class Sliding_Adapter extends PagerAdapter {
    NoticeBoard_Model[] broadCastArrayList;

    private LayoutInflater inflater;
    private Context context;
    TextView tvHeader, tvPostedBy, tvPostedOn, tvAck;
    LinearLayout readBroadcast_ll, remark_ll;
    ImageView ivCheckboxBlank, ivCheckboxTick;
    Button btn_submit;
    EditText etRemarks;
    WebView vwData;
    WebSettings webSettings;


    //change in api
    String acknowledged = "n";

    public Sliding_Adapter(Context context,  NoticeBoard_Model[] broadCastArrayList) {
        this.context = context;
        this.broadCastArrayList = broadCastArrayList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return broadCastArrayList[0].getMessages().length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View imageLayout = inflater.inflate(R.layout.broadcast_data, view, false);

        assert imageLayout != null;

        tvHeader = (TextView) imageLayout.findViewById(R.id.tvHeader);
        tvPostedBy = (TextView) imageLayout.findViewById(R.id.tvPostedBy);
        tvAck = (TextView) imageLayout.findViewById(R.id.tvAck);

        //remove maxlines in xml
        tvPostedOn = (TextView) imageLayout.findViewById(R.id.tvPostedOn);

        readBroadcast_ll = (LinearLayout) imageLayout.findViewById(R.id.readBroadcast_ll);
        remark_ll = (LinearLayout) imageLayout.findViewById(R.id.remark_ll);

        ivCheckboxBlank = (ImageView) imageLayout.findViewById(R.id.ivCheckboxBlank);
        ivCheckboxTick = (ImageView) imageLayout.findViewById(R.id.ivCheckboxTick);

        btn_submit = (Button) imageLayout.findViewById(R.id.btn_submit);

        etRemarks = (EditText) imageLayout.findViewById(R.id.etRemarks);

        vwData = (WebView) imageLayout.findViewById(R.id.vwData);
        vwData.setBackgroundColor(Color.TRANSPARENT);

        if (acknowledged.equals("y")) {
            tvAck.setVisibility(View.VISIBLE);
            remark_ll.setVisibility(View.GONE);
            btn_submit.setVisibility(View.GONE);
        } else {
            tvAck.setVisibility(View.GONE);
            remark_ll.setVisibility(View.VISIBLE);
            btn_submit.setVisibility(View.VISIBLE);        }

        etRemarks.setFilters(new InputFilter[]{EMOJI_FILTER});
        etRemarks.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String enteredString = s.toString();
                if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                        enteredString.startsWith("#") || enteredString.startsWith("$") ||
                        enteredString.startsWith("%") || enteredString.startsWith("^") ||
                        enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                    Toast.makeText(context,
                            ToastFile.ent_feedback,
                            Toast.LENGTH_SHORT).show();
                    if (enteredString.length() > 0) {
                        etRemarks.setText(enteredString.substring(1));
                    } else {
                        etRemarks.setText("");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        tvHeader.setText(Html.fromHtml(broadCastArrayList[0].getMessages()[position].getNoticeMessage()));
        tvPostedBy.setText("Posted by " + broadCastArrayList[0].getMessages()[position].getEnterBy());
        tvPostedOn.setText("Posted on " + broadCastArrayList[0].getMessages()[position].getNoticeDate());

        webSettings=vwData.getSettings();
        webSettings.setJavaScriptEnabled(true);
        String htmlText = " %s ";

        String justifyTag = broadCastArrayList[0].getMessages()[position].getNoticeMessage();
        //String dataString = String.format(Locale.US, justifyTag, "");
        vwData.loadData(String.format(htmlText,justifyTag),"text/html","utf-8");

        readBroadcast_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ivCheckboxBlank.getVisibility()==View.VISIBLE) {
                    ivCheckboxBlank.setVisibility(View.GONE);
                    ivCheckboxTick.setVisibility(View.VISIBLE);
                }
                else {
                    ivCheckboxBlank.setVisibility(View.VISIBLE);
                    ivCheckboxTick.setVisibility(View.GONE);
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (acknowledged.equals("y")) {
                    tvAck.setVisibility(View.VISIBLE);
                    remark_ll.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.GONE);
                } else {
                    if(etRemarks.getText().toString().length()!=0 && ivCheckboxTick.getVisibility()==View.VISIBLE){
                        acknowledged="y";
                        tvAck.setVisibility(View.VISIBLE);
                        readBroadcast_ll.setVisibility(View.GONE);
                        remark_ll.setVisibility(View.GONE);
                        btn_submit.setVisibility(View.GONE);
                    }
                    else if(ivCheckboxBlank.getVisibility()==View.VISIBLE)
                        TastyToast.makeText(context, "Kindly acknowledge the broadcast!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    else if(etRemarks.getText().toString().length()==0)
                        TastyToast.makeText(context, "Enter the remarks!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }
        });
        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
    public static InputFilter EMOJI_FILTER = new InputFilter() {
        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int index = start; index < end; index++) {

                int type = Character.getType(source.charAt(index));

                if (type == Character.SURROGATE) {
                    return "";
                }
            }
            return null;
        }
    };


}