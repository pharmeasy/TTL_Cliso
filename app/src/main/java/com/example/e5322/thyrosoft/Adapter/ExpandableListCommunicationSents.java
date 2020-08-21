package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.Communication_Activity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.Sents;
import com.example.e5322.thyrosoft.R;

import androidx.core.content.ContextCompat;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by e5426@thyrocare.com on 22/5/18.
 */

public class ExpandableListCommunicationSents extends BaseExpandableListAdapter {

    Activity activity;
    Sents[] sents;
    private TextView commHeader, name, date;
    TextView tvViewResponse;
    LinearLayout responsedatalinear, parent_question_ll, respond_ll, view_response_ll;
    public static com.android.volley.RequestQueue PostQueOtp;
    String user, passwrd, access, api_key, email_pref, mobile_pref;
    private SharedPreferences prefs;
    String TAG = ExpandableListCommunicationSents.class.getSimpleName().toString();

    public ExpandableListCommunicationSents(Communication_Activity communication_activity, Sents[] sents) {
        this.activity = communication_activity;
        this.sents = sents;
    }

    @Override
    public int getGroupCount() {
        return sents.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return sents[groupPosition].getCommunication();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return sents[groupPosition].getResponse();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.rpl_cpl_header_row, null);
        }

        tvViewResponse = (TextView) convertView.findViewById(R.id.tvViewResponse);
        commHeader = (TextView) convertView.findViewById(R.id.commHeader);
        name = (TextView) convertView.findViewById(R.id.name);
        date = (TextView) convertView.findViewById(R.id.date);
        parent_question_ll = (LinearLayout) convertView.findViewById(R.id.parent_question_ll);
        respond_ll = (LinearLayout) convertView.findViewById(R.id.respond_ll);
        view_response_ll = (LinearLayout) convertView.findViewById(R.id.view_response_ll);

        String headerTitle = sents[groupPosition].getCommunication();


        //Used shared preference
        SharedPreferences getProfileName = activity.getSharedPreferences("profilename", MODE_PRIVATE);
        String nameString = getProfileName.getString("name", "");
        String usercode = getProfileName.getString("usercode", "");
        String profile_image = getProfileName.getString("image", "");

        prefs = activity.getSharedPreferences("Userdetails", MODE_PRIVATE);
        user = prefs.getString("Username", "");
        passwrd = prefs.getString("password", "");
        access = prefs.getString("ACCESS_TYPE", "");
        api_key = prefs.getString("API_KEY", "");
        email_pref = prefs.getString("email", "");
        mobile_pref = prefs.getString("mobile_user", "");


        GlobalClass.SetText(date, this.sents[groupPosition].getCommDate());
        GlobalClass.SetText(name, nameString);
        GlobalClass.SetText(commHeader, headerTitle);
        commHeader.setVisibility(View.VISIBLE);


        if (isExpanded) {
            GlobalClass.SetText(tvViewResponse,"Hide Response");
            parent_question_ll.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.parent_question_bottom_border));
        } else {
            GlobalClass.SetText(tvViewResponse,"View Response");
            parent_question_ll.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.filled_round_rect));
        }

        if (sents[groupPosition].getResponse().equals("")) {
            respond_ll.setVisibility(View.GONE);
            view_response_ll.setVisibility(View.VISIBLE);
        } else {
            respond_ll.setVisibility(View.GONE);
            view_response_ll.setVisibility(View.VISIBLE);
        }


        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.activity
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.fromcpl_tocpl_child_row, null);
            }
            TextView resdate = (TextView) convertView.findViewById(R.id.resdate);
            TextView respondedby = (TextView) convertView.findViewById(R.id.respondedby);
            TextView response = (TextView) convertView.findViewById(R.id.response);
            TextView tat = (TextView) convertView.findViewById(R.id.tat);
            LinearLayout write_response_ll = (LinearLayout) convertView.findViewById(R.id.write_response_ll);
            LinearLayout show_resp = (LinearLayout) convertView.findViewById(R.id.show_resp);

            responsedatalinear = (LinearLayout) convertView.findViewById(R.id.responsedatalinear);
            responsedatalinear.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.anaborder));

            name.setVisibility(View.VISIBLE);
            respondedby.setVisibility(View.VISIBLE);
            GlobalClass.SetText(respondedby, sents[groupPosition].getResponseBy());


            if (GlobalClass.isNull(sents[groupPosition].getResponse())) {
                GlobalClass.SetText(response, "Not yet responded !");
                response.setTextColor(Color.parseColor("#f97d7b"));
            } else {
                GlobalClass.SetText(response, sents[groupPosition].getResponse());
                response.setTextColor(Color.parseColor("#000000"));
            }


            GlobalClass.SetText(resdate, sents[groupPosition].getResDate());
            GlobalClass.SetText(tat, "TAT: " + sents[groupPosition].getTAT());


            if (GlobalClass.checkArray(sents)) {
                if (GlobalClass.isNull(sents[groupPosition].getResponse())) {
                    write_response_ll.setVisibility(View.GONE);
                    show_resp.setVisibility(View.VISIBLE);
                } else {
                    write_response_ll.setVisibility(View.GONE);
                    show_resp.setVisibility(View.VISIBLE);
                }
            } else {
                GlobalClass.showTastyToast(activity, MessageConstants.NODATA, 2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
