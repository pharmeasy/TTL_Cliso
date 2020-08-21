package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.PostCHN_Controller;
import com.example.e5322.thyrosoft.Fragment.CHNfragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Models.TrackDetModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.ToastFile;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by e5426@thyrocare.com on 13/6/18.
 */

public class CHN_Adapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<TrackDetModel> List; // header titles
    java.util.List<String> items;
    public static RequestQueue PostQue;
    JSONArray PostArray = new JSONArray();
    JSONObject obj = new JSONObject();
    private String error;
    private String TAG = ManagingTabsActivity.class.getSimpleName().toString();
    private String RES_ID;
    private String response1;
    private String getSLNumber;
    CHNfragment mCHNfragment;
    String convertedDate;
    boolean serFlagVisible = true;

    private String blockCharacterSet = "#$^*+-/|><";

    String blockCharacterSetdata = "~#^|$%&*!+:`";
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
    private InputFilter filter1 = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSetdata.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };
    private String getName;
    private String getSL_No;

    public CHN_Adapter(Context context, ArrayList<TrackDetModel> listData, java.util.List<String> tests, CHNfragment fragment, String convertedDate) {
        this.mContext = context;
        this.List = listData;
        this.items = tests;
        this.mCHNfragment = fragment;
        this.convertedDate = convertedDate;

    }

    @Override
    public int getGroupCount() {
        return List.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return List.get(groupPosition).getChn_test_list().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return List.size();

    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return 0;

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
            convertView = infalInflater.inflate(R.layout.track_dtl_row_chn, null);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView Refby = (TextView) convertView.findViewById(R.id.Refby);
            TextView barcode = (TextView) convertView.findViewById(R.id.barcode);
            TextView tests = (TextView) convertView.findViewById(R.id.tests);
            TextView test_name = (TextView) convertView.findViewById(R.id.test_name);
            ImageView download = (ImageView) convertView.findViewById(R.id.download);
            ImageView mail = (ImageView) convertView.findViewById(R.id.mail);
            ImageView print = (ImageView) convertView.findViewById(R.id.print);
            final Button enter_chn_btn = (Button) convertView.findViewById(R.id.enter_chn_btn);
            Button submit_chn = (Button) convertView.findViewById(R.id.submit_chn);
            final EditText enter_remark = (EditText) convertView.findViewById(R.id.enter_remark);
            final LinearLayout chn_remark_layout = (LinearLayout) convertView.findViewById(R.id.chn_remark_layout);

            enter_remark.setFilters(new InputFilter[]{filter});
            enter_remark.setFilters(new InputFilter[]{filter1});
            enter_remark.setFilters(new InputFilter[]{EMOJI_FILTER});
            enter_remark.addTextChangedListener(new TextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    String enteredString = s.toString();
                    if (enteredString.startsWith(" ") || enteredString.startsWith("!") || enteredString.startsWith("@") ||
                            enteredString.startsWith("#") || enteredString.startsWith("$") ||
                            enteredString.startsWith("%") || enteredString.startsWith("^") ||
                            enteredString.startsWith("&") || enteredString.startsWith("*") || enteredString.startsWith(".")) {
                        GlobalClass.showTastyToast((Activity)mContext,
                                ToastFile.remark,
                                2);
                        if (enteredString.length() > 0) {
                            GlobalClass.SetEditText(enter_remark,enteredString.substring(1));
                        } else {
                            GlobalClass.SetEditText(enter_remark,"");
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


            GlobalClass.SetText(name, List.get(groupPosition).getName());
            GlobalClass.SetText(Refby, "Ref by:" + List.get(groupPosition).getRef_By());
            GlobalClass.SetText(barcode, List.get(groupPosition).getBarcode());
            GlobalClass.SetText(tests, List.get(groupPosition).getTests());
            GlobalClass.SetText(test_name, List.get(groupPosition).getChn_test());


            download.setVisibility(View.GONE);
            mail.setVisibility(View.GONE);
            print.setVisibility(View.GONE);

            enter_chn_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (serFlagVisible) {

                        getName = List.get(groupPosition).getName().toString();
                        if (getName.equals(List.get(groupPosition).getName().toString())) {
                            getSL_No = List.get(groupPosition).getLabcode().toString();
                        }
                        chn_remark_layout.setVisibility(View.VISIBLE);
                        serFlagVisible = false;
                        GlobalClass.SetButtonText(enter_chn_btn,"Hide Remark");
                    } else if (!serFlagVisible) {
                        chn_remark_layout.setVisibility(View.GONE);
                        serFlagVisible = true;
                        GlobalClass.SetButtonText(enter_chn_btn,"Enter CHN");
                    }

                }
            });

            submit_chn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String getVAlue = enter_remark.getText().toString();
                    if (getVAlue.equals("")) {
                        GlobalClass.showTastyToast((Activity)mContext, ToastFile.remark, 2);
                    } else if (getVAlue.length() < 3) {
                        GlobalClass.showTastyToast((Activity)mContext, ToastFile.remark, 2);
                    } else {
                        Log.v("TAG", "" + List);
                        PostArray = new JSONArray();

                        if (GlobalClass.CheckArrayList(List) && GlobalClass.CheckArrayList(List.get(groupPosition).getChn_test_list())){
                            for (int i = 0; i < List.get(groupPosition).getChn_test_list().size(); i++) {
                                try {
                                    obj = new JSONObject();
                                    obj.put("test_code", List.get(groupPosition).getChn_test_list().get(i).getTest());
                                    obj.put("remark", getVAlue);
                                    PostArray.put(obj);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }

                        PostCHN();

                    }
                }
            });
        }
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        //final String childText = (String) getChild(groupPosition, childPosition);
        try {
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) this.mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.chn_post_row, null);

                TextView Tests = (TextView) convertView.findViewById(R.id.Tests);
                EditText remark = (EditText) convertView.findViewById(R.id.remarkwrite);
                Button button = (Button) convertView.findViewById(R.id.submitBtn);



                GlobalClass.SetText(Tests, List.get(groupPosition).getChn_test_list().get(childPosition).getTest().toString());

                remark.setTag(List.get(groupPosition).getChn_test_list().get(childPosition));
                remark.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.v("TAG", "Nitya >> " + List.get(groupPosition).getChn_test_list().get(childPosition).getTest());
                        List.get(groupPosition).getChn_test_list().get(childPosition).setRemark("" + s.toString());
                    }
                });


                if (childPosition == List.get(groupPosition).getChn_test_list().size() - 1) {
                    button.setVisibility(View.VISIBLE);
                } else if (childPosition == 0 && List.get(groupPosition).getChn_test_list().size() != 1) {
                    button.setVisibility(View.GONE);
                }


            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }

    private void PostCHN() {

        SharedPreferences prefs = mContext.getSharedPreferences("Userdetails", MODE_PRIVATE);
        String user = prefs.getString("Username", null);
        String passwrd = prefs.getString("password", null);
        String access = prefs.getString("ACCESS_TYPE", null);
        String api_key = prefs.getString("API_KEY", null);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String parseDate = sdf.format(d);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("api_key", api_key);
            jsonObject.put("tsp", user);
            jsonObject.put("sdate", convertedDate);
            jsonObject.put("sl_no", getSL_No);
            jsonObject.put("chn_remark", PostArray);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestQueue queue = GlobalClass.setVolleyReq(mContext);

        try {
            if (ControllersGlobalInitialiser.postCHN_controller != null) {
                ControllersGlobalInitialiser.postCHN_controller = null;
            }
            ControllersGlobalInitialiser.postCHN_controller = new PostCHN_Controller((Activity)mContext, CHN_Adapter.this);
            ControllersGlobalInitialiser.postCHN_controller.postchn_controller(jsonObject,queue);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    public void getpostchnResp(JSONObject response) {
        try {

            String finalJson = response.toString();
            Log.e(TAG, "onResponse: RESPONSE" + response);
            JSONObject parentObjectOtp = new JSONObject(finalJson);

            error = parentObjectOtp.getString("error");
            RES_ID = parentObjectOtp.getString("RES_ID");
            response1 = parentObjectOtp.getString("response");

            if (!GlobalClass.isNull(response1) && response1.equals(MessageConstants.CHN_UPDATE_SUCCUSS)) {
                GlobalClass.showTastyToast((Activity)mContext,"" + response1,1);
                mCHNfragment.setNewFragment();
                GlobalClass.passDate = GlobalClass.CHN_Date;

            } else {
                GlobalClass.showTastyToast((Activity)mContext,"" + response1,2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
