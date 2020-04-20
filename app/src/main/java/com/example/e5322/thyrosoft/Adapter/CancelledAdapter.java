package com.example.e5322.thyrosoft.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Fragment.DashboardFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class CancelledAdapter extends BaseExpandableListAdapter {
    ImageView mail, print;
    DashboardFragment dashboardFragment;
    Context mContext;
    ArrayList<TrackDetModel> trackdet = new ArrayList<>();
    int selectedPosition = 0;
    int[] SelectedArray;
    ListView canList;
    public static RequestQueue PostQue;
    SharedPreferences sharedpreferences;
    LinearLayout cancelled_Layout, childView;
    int Flag = 0;
    private ProgressDialog barProgressDialog;
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

            name.setText(trackdet.get(groupPosition).getName().toString());
            Refby.setText("Ref by:" + trackdet.get(groupPosition).getRef_By().toString());
            barcode.setText(trackdet.get(groupPosition).getBarcode().toString());
            tests.setText(trackdet.get(groupPosition).getTests().toString());
            download.setVisibility(View.GONE);
            mail.setVisibility(View.GONE);
            print.setVisibility(View.GONE);


        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //final String childText = (String) getChild(groupPosition, childPosition);
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
//            else if (childPosition == 0 && trackdet.get(groupPosition).getCancel_tests_with_remark().size() != 1) {
//                childView.setVisibility(View.GONE);
//            }
            TextView test = (TextView) convertView.findViewById(R.id.test);
            remark.setText(trackdet.get(groupPosition).getCancel_tests_with_remark().get(childPosition).getRemarks());

            test.setText(trackdet.get(groupPosition).getCancel_tests_with_remark().get(childPosition).getTest_code() + " : ");


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
        barProgressDialog = new ProgressDialog(mContext, R.style.ProgressBarColor);
        barProgressDialog.setTitle("Kindly wait ...");
        barProgressDialog.setMessage(ToastFile.processing_request);
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
        barProgressDialog.setProgress(0);
        barProgressDialog.setMax(20);
        barProgressDialog.show();
        barProgressDialog.setCanceledOnTouchOutside(false);
        barProgressDialog.setCancelable(false);
        PostQue = GlobalClass.setVolleyReq(mContext);

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
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                com.android.volley.Request.Method.POST, Api.postcancellead, jsonObject,
                new com.android.volley.Response.Listener<JSONObject>() {
                    public String RES_ID, billedB2B, credit, error, orderNo, response1;

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            String finalJson = response.toString();
                            Log.e(TAG, "onResponse: " + response);
                            JSONObject parentObjectOtp = new JSONObject(finalJson);

                            if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                barProgressDialog.dismiss();
                            }

                            RES_ID = parentObjectOtp.getString("RES_ID");
                            billedB2B = parentObjectOtp.getString("billedB2B");
                            credit = parentObjectOtp.getString("credit");
                            error = parentObjectOtp.getString("error");
                            orderNo = parentObjectOtp.getString("orderNo");
                            response1 = parentObjectOtp.getString("response");

                            TastyToast.makeText(mContext, response1, TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            dashboardFragment.setNewFragment();
                            GlobalClass.passDateFromLead = GlobalClass.lead_date;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    System.out.println("error ala parat " + error);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                150000,
                3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjectRequest);
        Log.e(TAG, "PostCancelledData: URL" + jsonObjectRequest);
        Log.e(TAG, "PostCancelledData: json" + jsonObject);
    }
}

