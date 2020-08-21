package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Controller.Cancellead_Controller;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Fragment.DashboardFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CancelledAdapter extends BaseExpandableListAdapter {
    ImageView mail, print;
    DashboardFragment dashboardFragment;
    Context mContext;
    ArrayList<TrackDetModel> trackdet = new ArrayList<>();
    ListView canList;
    public static RequestQueue PostQue;
    LinearLayout cancelled_Layout, childView;
    public String RES_ID, billedB2B, credit, error, orderNo, response1;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();

    public CancelledAdapter(Context ManagingTabsActivity, DashboardFragment dashboardFragment, ArrayList<TrackDetModel> arrayList) {
        this.mContext = ManagingTabsActivity;
        this.trackdet = arrayList;
        this.dashboardFragment = dashboardFragment;
    }

    @Override
    public int getGroupCount() {
        return this.trackdet.size();
    }


    @Override
    public int getChildrenCount(int groupPosition) {

        return trackdet.get(groupPosition).getCancel_tests_with_remark().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.trackdet.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {

        return this.trackdet.get(groupPosition).getCancel_tests_with_remark().get(childPosition);


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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        if (convertView == null) {

            LayoutInflater infalInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.track_det_row, null);


            TextView name = (TextView) convertView.findViewById(R.id.name);
            canList = (ListView) convertView.findViewById(R.id.canList);

            cancelled_Layout = (LinearLayout) convertView.findViewById(R.id.cancelled_Layout);
            TextView Refby = (TextView) convertView.findViewById(R.id.Refby);
            TextView barcode = (TextView) convertView.findViewById(R.id.barcode);
            TextView tests = (TextView) convertView.findViewById(R.id.tests);
            ImageView download = (ImageView) convertView.findViewById(R.id.download);
            ;
            mail = (ImageView) convertView.findViewById(R.id.mail);
            print = (ImageView) convertView.findViewById(R.id.print);

            GlobalClass.SetText(name, trackdet.get(groupPosition).getName().toString());
            GlobalClass.SetText(Refby, "Ref by:" + trackdet.get(groupPosition).getRef_By().toString());
            GlobalClass.SetText(barcode, trackdet.get(groupPosition).getBarcode().toString());
            GlobalClass.SetText(tests, trackdet.get(groupPosition).getTests().toString());

            download.setVisibility(View.GONE);
            mail.setVisibility(View.GONE);
            print.setVisibility(View.GONE);


        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        try {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.cancell_row, null);
            }

            TextView remark = (TextView) convertView.findViewById(R.id.remark);
            TextView ReqCredit = (TextView) convertView.findViewById(R.id.ReqCredit);
            TextView FreeTest = (TextView) convertView.findViewById(R.id.FreeTest);
            childView = (LinearLayout) convertView.findViewById(R.id.childView);
            if (childPosition == trackdet.get(groupPosition).getCancel_tests_with_remark().size() - 1) {
                childView.setVisibility(View.VISIBLE);
            } else {
                childView.setVisibility(View.GONE);
            }

            TextView test = (TextView) convertView.findViewById(R.id.test);
            GlobalClass.SetText(remark, trackdet.get(groupPosition).getCancel_tests_with_remark().get(childPosition).getRemarks());

            GlobalClass.SetText(test, trackdet.get(groupPosition).getCancel_tests_with_remark().get(childPosition).getTest_code() + " : ");

            ReqCredit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostCancelledData("CREDIT", trackdet.get(groupPosition).getPatient_id());
                }
            });
            FreeTest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PostCancelledData("LEAD", trackdet.get(groupPosition).getPatient_id());

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    private void PostCancelledData(String Request, String patientID) {


        JSONObject jsonObject = new JSONObject();
        try {

            SharedPreferences prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
            jsonObject.put("api_key", prefs.getString("API_KEY", null));
            jsonObject.put(Constants.requesttype, Request);
            jsonObject.put("tsp", prefs.getString("Username", null));
            jsonObject.put(Constants.remarks, "");
            jsonObject.put(Constants.patient_id, patientID);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestQueue queue = GlobalClass.setVolleyReq(mContext);

        try {
            if (ControllersGlobalInitialiser.cancellead_controller != null) {
                ControllersGlobalInitialiser.cancellead_controller = null;
            }
            ControllersGlobalInitialiser.cancellead_controller = new Cancellead_Controller((Activity) mContext, CancelledAdapter.this);
            ControllersGlobalInitialiser.cancellead_controller.getcancelLead(jsonObject,queue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getleadResp(JSONObject response) {
        try {
            String finalJson = response.toString();
            Log.e(TAG, "onResponse: " + response);
            JSONObject parentObjectOtp = new JSONObject(finalJson);

            RES_ID = parentObjectOtp.getString("RES_ID");
            billedB2B = parentObjectOtp.getString("billedB2B");
            credit = parentObjectOtp.getString("credit");
            error = parentObjectOtp.getString("error");
            orderNo = parentObjectOtp.getString("orderNo");
            response1 = parentObjectOtp.getString("response");

            GlobalClass.showTastyToast((Activity) mContext, response1, 1);
            dashboardFragment.setNewFragment();
            GlobalClass.passDateFromLead = GlobalClass.lead_date;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

