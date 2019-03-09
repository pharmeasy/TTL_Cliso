package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.Activity.Communication_Activity;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.Interface_Pass_CommunicationValue;
import com.example.e5322.thyrosoft.Interface.OnItemSelectedListenerInterface;
import com.example.e5322.thyrosoft.Models.CommInbox_Model;
import com.example.e5322.thyrosoft.Models.CommToCpl_Model;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.AppPreference;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.Inboxes;
import com.example.e5322.thyrosoft.Models.PincodeMOdel.Sents;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.Context.MODE_PRIVATE;

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

        date.setText(this.inboxes[groupPosition].getCommDate());
        name.setText(this.inboxes[groupPosition].getCommBy());

        commHeader.setVisibility(View.VISIBLE);
        commHeader.setText(headerTitle);

        /*Linkify.addLinks(commHeader, Linkify.EMAIL_ADDRESSES);
        commHeader.setMovementMethod(LinkMovementMethod.getInstance());*/

        if (isExpanded) {
            tvViewResponse.setText("Hide Response");
            parent_question_ll.setBackgroundDrawable(ContextCompat.getDrawable(activity, R.drawable.parent_question_bottom_border));
        } else {
            tvViewResponse.setText("View Response");
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

            name.setText(inboxes[groupPosition].getCommBy());

            response.setText(inboxes[groupPosition].getResponse());
            resdate.setText(inboxes[groupPosition].getResDate());
            tat.setText("TAT: " + inboxes[groupPosition].getTAT());

            if (inboxes.length > 0) {
                if (inboxes[groupPosition].getResponse().equals("")) {
                    write_response_ll.setVisibility(View.VISIBLE);
                    show_resp.setVisibility(View.GONE);
                } else {
                    write_response_ll.setVisibility(View.GONE);
                    show_resp.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(activity, "No data found", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(activity,
                                ToastFile.composeMsg,
                                Toast.LENGTH_SHORT).show();
                        if (enteredString.length() > 0) {
                            commuTXT.setText(enteredString.substring(1));
                        } else {
                            commuTXT.setText("");
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

                    if (getCommunication.equals("")) {
                        Toast.makeText(activity, "Please write your response", Toast.LENGTH_SHORT).show();
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
