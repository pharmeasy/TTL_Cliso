package com.example.e5322.thyrosoft.Adapter;

/**
 * Created by Priyanka on 09-06-2018.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import com.example.e5322.thyrosoft.Controller.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.e5322.thyrosoft.API.Api;
import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.InterfaceRateCAlculator;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Product_Rate_CalculatorModel;
import com.example.e5322.thyrosoft.ToastFile;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by E5322 on 07-06-2018.
 */

public class ExapandableAdpterForB2CRate_Calculator extends BaseExpandableListAdapter {

    AlertDialog alertDialog, alert;
    ProgressDialog barProgressDialog;
    private Activity activity;
    private ArrayList<Product_Rate_CalculatorModel> filteredList;
    private ArrayList<Product_Rate_CalculatorModel> testRateMasterModels;
    private ArrayList<Base_Model_Rate_Calculator> selectedTests = new ArrayList<>();
    private InterfaceRateCAlculator mcallback;
    private android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
    private List<String> tempselectedTests1;
    private ArrayList<Base_Model_Rate_Calculator> tempselectedTests;
    RecyclerView testdetails;
    public static com.android.volley.RequestQueue POstQueSendEstimation;
    ShowChildTestNamesAdapter showChildTestNamesAdapter;
    private ImageView imgClose;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<String> storetestlist;
    public String TAG = ExapandableAdpterForB2CRate_Calculator.class.getSimpleName().toString();

    public ExapandableAdpterForB2CRate_Calculator(Activity activity, ArrayList<Product_Rate_CalculatorModel> testRateMasterModels1, ArrayList<Base_Model_Rate_Calculator> selectedTests, InterfaceRateCAlculator mcallback) {
        this.activity = activity;
        this.filteredList = testRateMasterModels1;
        this.testRateMasterModels = testRateMasterModels1;
        this.mcallback = mcallback;
        this.selectedTests = selectedTests;
        if (this.selectedTests == null) {
            this.selectedTests = new ArrayList<>();
        }
    }

    @Override
    public int getGroupCount() {
        return filteredList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return filteredList.get(groupPosition).getTestRateMasterModels().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return filteredList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return filteredList.get(groupPosition).getTestRateMasterModels().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewParentHolder holder = null;
        if (convertView == null) {
            LayoutInflater inf = activity.getLayoutInflater();
            convertView = inf.inflate(R.layout.product_header, parent, false);
            holder = new ViewParentHolder();
            holder.txtHeader = (TextView) convertView.findViewById(R.id.txt_header);
            convertView.setTag(holder);
        } else {
            holder = (ViewParentHolder) convertView.getTag();
        }
        holder.txtHeader.setText(filteredList.get(groupPosition).getTestType());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder = null;
        convertView = null;
        if (convertView == null) {
            LayoutInflater infalInflater = activity.getLayoutInflater();
            convertView = infalInflater.inflate(R.layout.product_child, parent, false);
            holder = new ViewChildHolder();

            holder.txt_test = (TextView) convertView.findViewById(R.id.txt_test);
            holder.txt_dis_amt = (TextView) convertView.findViewById(R.id.txt_dis_amt);
            holder.img_test_type = (ImageView) convertView.findViewById(R.id.img_test_type);//image pop or test or peofile
            holder.imgCheck = (ImageView) convertView.findViewById(R.id.img_check);
            holder.imgChecked = (ImageView) convertView.findViewById(R.id.img_checked);
            holder.isSelectedDueToParent = false;
            holder.parentTestCode = "";
            holder.imgTestFasting = (ImageView) convertView.findViewById(R.id.test_fasting);
            convertView.setTag(holder);
        } else {
            holder = (ViewChildHolder) convertView.getTag();
        }

        final boolean isSelectedDueToParent = holder.isSelectedDueToParent;
        final String parentTestCode = holder.parentTestCode;

        final Base_Model_Rate_Calculator testRateMasterModel = filteredList.get(groupPosition).getTestRateMasterModels().get(childPosition);
        holder.txt_test.setText(testRateMasterModel.getName());

        holder.txt_dis_amt.setText("₹ " + testRateMasterModel.getRate().getB2c() + "/-");

        if (testRateMasterModel.getType() != null && testRateMasterModel.getType().equalsIgnoreCase(Constants.PRODUCT_POP)) {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.p));
        } else if (testRateMasterModel.getType() != null && testRateMasterModel.getType().equalsIgnoreCase(Constants.PRODUCT_PROFILE)) {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.p));
        } else if (testRateMasterModel.getType() != null && testRateMasterModel.getType().equalsIgnoreCase(Constants.PRODUCT_TEST)) {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.t));
        }
        if (testRateMasterModel.getFasting().equalsIgnoreCase("FASTING")) {
            holder.imgTestFasting.setVisibility(View.GONE);
            holder.imgTestFasting.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_fasting));
        } else if (testRateMasterModel.getFasting().equalsIgnoreCase("NON FASTING")) {
            holder.imgTestFasting.setVisibility(View.GONE);
            holder.imgTestFasting.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_non_fasting));
        } else {
            holder.imgTestFasting.setVisibility(View.INVISIBLE);
        }

        if (testRateMasterModel.getChilds().length != 0) {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    JSONObject jsonObjectValidateOtp = new JSONObject();
                    try {
                        jsonObjectValidateOtp.put("TestName", testRateMasterModel.getName());
                        jsonObjectValidateOtp.put("Type", "PROFILE");

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    barProgressDialog = new ProgressDialog(activity);
                    barProgressDialog.setTitle("Kindly wait ...");
                    barProgressDialog.setMessage(ToastFile.processing_request);
                    barProgressDialog.setProgressStyle(barProgressDialog.STYLE_SPINNER);
                    barProgressDialog.setProgress(0);
                    barProgressDialog.setMax(20);
                    barProgressDialog.show();
                    barProgressDialog.setCanceledOnTouchOutside(false);
                    barProgressDialog.setCancelable(false);
                    POstQueSendEstimation = GlobalClass.setVolleyReq(activity);

                    JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(com.android.volley.Request.Method.POST, Api.testDetails, jsonObjectValidateOtp, new com.android.volley.Response.Listener<JSONObject>() {


                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Log.e(TAG, "onResponse: " + response);
                                String finalJson = response.toString();
                                ArrayList<String> responseFromAPI = new ArrayList<>();

                                String responsedata = response.getString("response");
                                JSONArray jsonArray = response.getJSONArray("TestName");
                                if (jsonArray.length() != 0 || jsonArray != null) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        responseFromAPI.add(jsonArray.get(i).toString());
                                    }
                                    if (responsedata.equals("Success")) {

                                        showChildTestNamesAdapter = new ShowChildTestNamesAdapter(activity, responseFromAPI);
                                        LayoutInflater li = LayoutInflater.from(activity);
                                        View promptsView = li.inflate(R.layout.activity_show_child_list, null);
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                                        alertDialogBuilder.setView(promptsView);
                                        testdetails = (RecyclerView) promptsView.findViewById(R.id.testdetails);
                                        imgClose = (ImageView) promptsView.findViewById(R.id.imgClose);
                                        linearLayoutManager = new LinearLayoutManager(activity);
                                        testdetails.setLayoutManager(linearLayoutManager);
                                        testdetails.setAdapter(showChildTestNamesAdapter);
                                        if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                            barProgressDialog.dismiss();
                                        }
                                        alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();

                                        imgClose.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                alertDialog.dismiss();
                                            }
                                        });

                                    }

                                } else {
                                    TastyToast.makeText(activity, "", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                                    if (barProgressDialog != null && barProgressDialog.isShowing()) {
                                        barProgressDialog.dismiss();
                                    }
                                }
                            } catch (JSONException e) {
                                Toast.makeText(activity, "" + e, Toast.LENGTH_SHORT).show();
                                e.printStackTrace();
                            }
                        }
                    }, new com.android.volley.Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error != null) {
                            } else {

                                System.out.println(error);
                            }
                        }
                    });
                    POstQueSendEstimation.add(jsonObjectRequest1);
                    Log.e(TAG, "onClick: URl" + jsonObjectRequest1);


/////////////////////////////////////////////////////////////////////////////////////

                    if (testRateMasterModel.getChilds().length != 0) {
                        storetestlist = new ArrayList<>();
                        for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                            storetestlist.add(testRateMasterModel.getChilds()[i].getCode());
                        }
                        showChildTestNamesAdapter = new ShowChildTestNamesAdapter(activity, storetestlist);
                    }
///////////////////////////////////////////////////////////////////////////////////////////////////
                }
            });

        }

        boolean isChecked = false;
        holder.isSelectedDueToParent = false;
        holder.parentTestCode = "";
        holder.imgChecked.setVisibility(View.GONE);
        holder.imgCheck.setVisibility(View.VISIBLE);

        if (selectedTests != null && selectedTests.size() > 0) {
            for (int i = 0; !isChecked && i < selectedTests.size(); i++) {
                Base_Model_Rate_Calculator selectedTestModel = selectedTests.get(i);
                if (selectedTestModel.getCode().equals(testRateMasterModel.getCode())) {
                    holder.imgChecked.setVisibility(View.VISIBLE);
                    holder.imgCheck.setVisibility(View.GONE);
                    holder.isSelectedDueToParent = false;
                    holder.parentTestCode = "";
                    isChecked = true;
                } else if (selectedTestModel.getChilds() != null && testRateMasterModel.getChilds() != null && selectedTestModel.checkIfChildsContained(testRateMasterModel)) {
                    holder.imgChecked.setVisibility(View.INVISIBLE);
                    holder.imgCheck.setVisibility(View.GONE);
                    holder.isSelectedDueToParent = false;
                    holder.parentTestCode = selectedTestModel.getCode();
                    isChecked = true;
                } else {
                    if (selectedTestModel.getChilds() != null && selectedTestModel.getChilds().length > 0) {
                        for (Base_Model_Rate_Calculator.Childs ctm :
                                selectedTestModel.getChilds()) {
                            if (ctm.getCode().equals(testRateMasterModel.getCode())) {

                                holder.imgChecked.setVisibility(View.INVISIBLE);
                                holder.imgCheck.setVisibility(View.GONE);
                                holder.isSelectedDueToParent = false;
                                holder.parentTestCode = selectedTestModel.getCode();
                                holder.parentTestname = selectedTestModel.getName();
                                isChecked = true;


//                                holder.imgChecked.setVisibility(View.GONE);
//                                holder.imgCheck.setVisibility(View.GONE);
//                                holder.isSelectedDueToParent = true;
//                                holder.parentTestCode = selectedTestModel.getCode();
//                                isChecked = true;


                                break;
                            } else {
                                holder.imgChecked.setVisibility(View.INVISIBLE);
                                holder.imgCheck.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        holder.imgChecked.setVisibility(View.INVISIBLE);
                        holder.imgCheck.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            holder.imgChecked.setVisibility(View.INVISIBLE);
            holder.imgCheck.setVisibility(View.VISIBLE);
        }

        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "";

                str = str + testRateMasterModel.getCode() + ",";


                String slectedpackage = "";


                slectedpackage = testRateMasterModel.getName();

                tempselectedTests = new ArrayList<>();
                tempselectedTests1 = new ArrayList<>();

                if (testRateMasterModel.getChilds() != null) {
                    for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                        //tejas t -----------------------------
                        for (int j = 0; j < selectedTests.size(); j++) {


                            if (testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                System.out.println("Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                tempselectedTests1.add(selectedTests.get(j).getName());
                                tempselectedTests.add(selectedTests.get(j));
                            } else if (selectedTests.get(j).getCode().equalsIgnoreCase("HEMOGRAM - 6 PART (DIFF)") && testRateMasterModel.getChilds()[j].getCode().equalsIgnoreCase("H6")) {
                                tempselectedTests1.add(selectedTests.get(j).getName());
                                tempselectedTests.add(selectedTests.get(j));
                            }
                        }
                    }
                }
                for (int j = 0; j < selectedTests.size(); j++) {
                    Base_Model_Rate_Calculator selectedTestModel123 = selectedTests.get(j);
                    if (selectedTestModel123.getChilds() != null && testRateMasterModel.getChilds() != null && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {

                        tempselectedTests1.add(selectedTests.get(j).getName());
                        tempselectedTests.add(selectedTestModel123);
                    }
                }

                if (tempselectedTests != null && tempselectedTests.size() > 0) {
                    String cartproduct = TextUtils.join(",", tempselectedTests1);
                    alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
                    alertDialogBuilder
                            .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    alertDialog.dismiss();
                                }
                            });
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
                for (int i = 0; i < tempselectedTests.size(); i++) {
                    for (int j = 0; j < selectedTests.size(); j++) {
                        if (tempselectedTests.get(i).getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                            selectedTests.remove(j);
                        }
                    }
                }
                //tejas t -----------------------------
                selectedTests.add(testRateMasterModel);
                mcallback.onCheckChangeRateCalculator(selectedTests);
            }
        });

        holder.imgChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectedDueToParent) {
                    selectedTests.remove(testRateMasterModel);
                    mcallback.onCheckChangeRateCalculator(selectedTests);
                } else {
                    alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(activity);
                    alertDialogBuilder
                            .setMessage(Html.fromHtml("This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode))
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    alertDialog.dismiss();
                                }
                            });
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    //Toast.makeText(activity, "This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public void onCheckChangeRateCalculator(ArrayList<Base_Model_Rate_Calculator> selectedTests) {

    }

    private class ViewParentHolder {
        TextView txtHeader;
    }

    private class ViewChildHolder {
        ImageView img_test_type;
        ImageView imgCheck, imgChecked;
        TextView txt_test, txt_dis_amt;
        boolean isSelectedDueToParent;
        String parentTestCode, parentTestname;
        ImageView imgTestFasting;


    }

    public void filterData(String query) {

        query = query.toLowerCase();
//        Logger.verbose("FilteredListSizeBeforeFilter: " + String.valueOf(filteredList.size()));

//        if (query.isEmpty()) {
//            filteredList=null;
//            filteredList = new ArrayList<>();
//            filteredList.addAll(testRateMasterModels);
////            Logger.verbose("FilteredListSizeAfterFilerQueryEmpty: " + String.valueOf(filteredList.size()));
//
//        } else {
        filteredList = null;
        filteredList = new ArrayList<>();
        for (Product_Rate_CalculatorModel testTypeModel : testRateMasterModels) {

            ArrayList<Base_Model_Rate_Calculator> oldList = testTypeModel.getTestRateMasterModels();
            ArrayList<Base_Model_Rate_Calculator> newList = new ArrayList<>();
            for (Base_Model_Rate_Calculator testModel : oldList) {
                if (testModel.getName().toLowerCase().contains(query)) {
                    newList.add(testModel);
                }
            }
            if (newList.size() > 0) {
                Product_Rate_CalculatorModel nContinent = new Product_Rate_CalculatorModel(testTypeModel.getTestType(), newList);
                filteredList.add(nContinent);
            }
        }
//        }

//        Logger.verbose("FilteredListSizeAfterFilter: " + String.valueOf(filteredList.size()));
        notifyDataSetChanged();

    }
}

