package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.Communication_Activity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.Interface_Pass_CommunicationValue;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.Inboxes;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import androidx.core.content.ContextCompat;

/**
 * Created by e5426@thyrocare.com on 22/5/18.
 */

public class ExpandableListCommunication extends BaseExpandableListAdapter {

    Activity activity;
    Inboxes[] inboxes;
    private TextView commHeader, name, date;
    TextView tvViewResponse;
    LinearLayout responsedatalinear, parent_question_ll, respond_ll, view_response_ll;
    private String getCommunication;
    String TAG = ExpandableListCommunication.class.getSimpleName().toString();
    private Interface_Pass_CommunicationValue interface_pass_communicationValue;

    public ExpandableListCommunication(Communication_Activity communication_activity, Inboxes[] inboxes, Interface_Pass_CommunicationValue interface_pass_communicationValue) {
        this.activity = communication_activity;
        this.inboxes = inboxes;
        this.interface_pass_communicationValue = interface_pass_communicationValue;
    }

    @Override
    public int getGroupCount() {
        return inboxes.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return inboxes[groupPosition].getQuestion();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return inboxes[groupPosition].getResponse();
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

        String headerTitle = inboxes[groupPosition].getQuestion();

        GlobalClass.SetText(date,this.inboxes[groupPosition].getCommDate());
        GlobalClass.SetText(name,this.inboxes[groupPosition].getCommBy());

        commHeader.setVisibility(View.VISIBLE);
        GlobalClass.SetText(commHeader,headerTitle);


        if (isExpanded) {
            GlobalClass.SetText(tvViewResponse,"Hide Response");
            parent_question_ll.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.parent_question_bottom_border));
        } else {
            GlobalClass.SetText(tvViewResponse,"View Response");
            parent_question_ll.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.filled_round_rect));
        }

        if (inboxes[groupPosition].getResponse().equals("")) {
            respond_ll.setVisibility(View.VISIBLE);
            view_response_ll.setVisibility(View.GONE);
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
            final EditText commuTXT = (EditText) convertView.findViewById(R.id.commuTXT);
            Button sendcomm = (Button) convertView.findViewById(R.id.sendcomm);
            LinearLayout write_response_ll = (LinearLayout) convertView.findViewById(R.id.write_response_ll);
            LinearLayout show_resp = (LinearLayout) convertView.findViewById(R.id.show_resp);


            responsedatalinear = (LinearLayout) convertView.findViewById(R.id.responsedatalinear);
            responsedatalinear.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.anaborder));

            name.setVisibility(View.VISIBLE);
            respondedby.setVisibility(View.GONE);

            GlobalClass.SetText(name,inboxes[groupPosition].getCommBy());
            GlobalClass.SetText(response,inboxes[groupPosition].getResponse());
            GlobalClass.SetText(resdate,inboxes[groupPosition].getResDate());
            GlobalClass.SetText(tat,"TAT: " + inboxes[groupPosition].getTAT());


            if (GlobalClass.checkArray(inboxes)) {
                if (!GlobalClass.isNull(inboxes[groupPosition].getResponse()) && inboxes[groupPosition].getResponse().equalsIgnoreCase("")) {
                    write_response_ll.setVisibility(View.VISIBLE);
                    show_resp.setVisibility(View.GONE);
                } else {
                    write_response_ll.setVisibility(View.GONE);
                    show_resp.setVisibility(View.VISIBLE);
                }
            } else {
                GlobalClass.showTastyToast(activity, MessageConstants.NODATA, 2);
            }

            commuTXT.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    String enteredString = s.toString();
                    if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                            enteredString.startsWith("#") || enteredString.startsWith("$") ||
                            enteredString.startsWith("%") || enteredString.startsWith("^") ||
                            enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                        GlobalClass.showTastyToast(activity,
                                ToastFile.composeMsg,
                                2);
                        if (enteredString.length() > 0) {
                            GlobalClass.SetText(commuTXT,enteredString.substring(1));
                        } else {
                            GlobalClass.SetText(commuTXT,"");
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


            sendcomm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getCommunication = commuTXT.getText().toString();

                    if (GlobalClass.isNull(getCommunication)) {
                        GlobalClass.showTastyToast(activity, MessageConstants.WRT_Response, 2);
                    } else {
                        interface_pass_communicationValue.passCommIdAndMSg(activity, inboxes[groupPosition].getCommId(), getCommunication);
                    }
                }
            });

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
