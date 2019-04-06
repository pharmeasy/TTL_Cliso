package com.example.e5322.thyrosoft.Activity;

import android.content.Intent;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e5322.thyrosoft.Models.Messages_Noticeboard;
import com.example.e5322.thyrosoft.Models.NoticeBoard_Model;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;
import com.sdsmdg.tastytoast.TastyToast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Locale;

public class BroadcastDataActivity extends AppCompatActivity {
    int position;
    String gsonString;
    NoticeBoard_Model[] broadCastArrayList;
    TextView tvHeader, tvPostedBy, tvPostedOn, tvAck;
    LinearLayout readBroadcast_ll, remark_ll;
    ImageView ivCheckboxBlank, ivCheckboxTick;
    Button btn_submit;
    EditText etRemarks;
    WebView vwData;
    String blockCharacterSet = "#$^*+-/|><";


    //change in api
    String acknowledged = "n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_broadcast_data);

        tvHeader = (TextView) findViewById(R.id.tvHeader);
        tvPostedBy = (TextView) findViewById(R.id.tvPostedBy);
        tvAck = (TextView) findViewById(R.id.tvAck);

        //remove maxlines in xml
        tvPostedOn = (TextView) findViewById(R.id.tvPostedOn);

        readBroadcast_ll = (LinearLayout) findViewById(R.id.readBroadcast_ll);
        remark_ll = (LinearLayout) findViewById(R.id.remark_ll);

        ivCheckboxBlank = (ImageView) findViewById(R.id.ivCheckboxBlank);
        ivCheckboxTick = (ImageView) findViewById(R.id.ivCheckboxTick);

        btn_submit = (Button) findViewById(R.id.btn_submit);

        etRemarks = (EditText) findViewById(R.id.etRemarks);

        vwData = (WebView) findViewById(R.id.vwData);
        vwData.setBackgroundColor(Color.TRANSPARENT);



        Intent i = getIntent();
        position = i.getIntExtra("position", 0);
        gsonString = i.getStringExtra("gsonString");

        Gson gson = new Gson();
        broadCastArrayList = gson.fromJson(gsonString, NoticeBoard_Model[].class);

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
                    Toast.makeText(BroadcastDataActivity.this,
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
                        remark_ll.setVisibility(View.GONE);
                        btn_submit.setVisibility(View.GONE);
                    }
                    else if(ivCheckboxBlank.getVisibility()==View.VISIBLE)
                        TastyToast.makeText(getApplicationContext(), "Kindly acknowledge the broadcast!", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                    else if(etRemarks.getText().toString().length()==0)
                    TastyToast.makeText(getApplicationContext(), "Enter the remarks!", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                }
            }
        });
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