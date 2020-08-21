package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.Activity.ManagingTabsActivity;
import com.example.e5322.thyrosoft.Activity.SampleTypeColor;
import com.example.e5322.thyrosoft.Controller.ControllersGlobalInitialiser;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Controller.TestDetails_Controller;
import com.example.e5322.thyrosoft.Fragment.RateCalculatorFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.InterfaceRateCAlculator;
import com.example.e5322.thyrosoft.Models.RequestModels.TestDetailsRequestModel;
import com.example.e5322.thyrosoft.Models.ResponseModels.TestDetailsResponseModel;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.example.e5322.thyrosoft.ToastFile;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public class RateCAlAdapter extends RecyclerView.Adapter<RateCAlAdapter.ViewHolder> {
    public static com.android.volley.RequestQueue POstQueSendEstimation;
    public String TAG = RateCalculatorFragment.class.getSimpleName().toString();
    View view;
    ManagingTabsActivity mContext;
    ArrayList<Base_Model_Rate_Calculator> mgetSingleList;
    ArrayList<Base_Model_Rate_Calculator> filteredList;
    ShowChildTestNamesAdapter showChildTestNamesAdapter;
    RecyclerView testdetails;
    ArrayList<String> storetestlist;
    AlertDialog alertDialog;
    private ArrayList<Base_Model_Rate_Calculator> tempselectedTests;
    private List<String> tempselectedTests1;
    private ArrayList<Base_Model_Rate_Calculator> selectedTests = new ArrayList<>();
    private ArrayList<Base_Model_Rate_Calculator> AllProductArrayList;
    private androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
    private ImageView imgClose;
    private InterfaceRateCAlculator mcallback;
    private LinearLayoutManager linearLayoutManager;

    public RateCAlAdapter(ManagingTabsActivity mContext, ArrayList<Base_Model_Rate_Calculator> finalgetAllTests, ArrayList<Base_Model_Rate_Calculator> getTotalArray, ArrayList<Base_Model_Rate_Calculator> selectedTests, InterfaceRateCAlculator mcallback) {
        this.mContext = mContext;
        this.mgetSingleList = finalgetAllTests;
        this.AllProductArrayList = getTotalArray;
        this.filteredList = finalgetAllTests;
        this.mcallback = mcallback;
        this.selectedTests = selectedTests;
        if (this.selectedTests == null) {
            this.selectedTests = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public RateCAlAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        view = inflater.inflate(R.layout.rate_cal_test_single, viewGroup, false);
        RateCAlAdapter.ViewHolder vh = new RateCAlAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(@NonNull final RateCAlAdapter.ViewHolder viewHolder, final int position) {

        viewHolder.lin_color.removeAllViews();
        GlobalClass.SetText(viewHolder.test_name_rate_txt, mgetSingleList.get(position).getName());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("en", "in"));
        int passThvalue = Integer.parseInt(mgetSingleList.get(position).getRate().getB2c());
        String setRtaesToTests = numberFormat.format(passThvalue);

        GlobalClass.SetText(viewHolder.test_rate_cal_txt, "₹ " + setRtaesToTests + "/-");


        final boolean isSelectedDueToParent = viewHolder.isSelectedDueToParent;
        final String parentTestCode = viewHolder.parentTestCode;
        final Base_Model_Rate_Calculator getSelected_test = filteredList.get(position);

        /*TODO Below logic for TTL Sample type color code*/
        if (GlobalClass.CheckArrayList(mgetSingleList)) {
            SampleTypeColor sampleTypeColor = new SampleTypeColor(mContext, mgetSingleList, position);
            sampleTypeColor.ttlcolor(viewHolder.lin_color);
        }

        if (GlobalClass.checkArray(getSelected_test.getChilds())) {

            viewHolder.parent_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject jsonObject = null;
                    try {
                        TestDetailsRequestModel requestModel = new TestDetailsRequestModel();
                        requestModel.setTestName(getSelected_test.getName());
                        requestModel.setType("PROFILE");

                        Gson gson = new Gson();
                        String json = gson.toJson(requestModel);
                        jsonObject = new JSONObject(json);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    POstQueSendEstimation = GlobalClass.setVolleyReq(mContext);

                    try {
                        if (ControllersGlobalInitialiser.testDetails_controller != null) {
                            ControllersGlobalInitialiser.testDetails_controller = null;
                        }
                        ControllersGlobalInitialiser.testDetails_controller = new TestDetails_Controller((Activity)mContext,RateCAlAdapter.this);
                        ControllersGlobalInitialiser.testDetails_controller.gettestdetail_controller(jsonObject,POstQueSendEstimation);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Log.e(TAG, "onClick: jsonObject " + jsonObject);

                    if (GlobalClass.checkArray(getSelected_test.getChilds())) {
                        storetestlist = new ArrayList<>();
                        for (int i = 0; i < getSelected_test.getChilds().length; i++) {
                            storetestlist.add(getSelected_test.getChilds()[i].getCode());
                        }
                        showChildTestNamesAdapter = new ShowChildTestNamesAdapter(mContext, storetestlist);
                    }
                }
            });

        }

        boolean isChecked = false;
        viewHolder.isSelectedDueToParent = false;
        viewHolder.parentTestCode = "";


        if (GlobalClass.CheckArrayList(selectedTests)) {
            for (int i = 0; !isChecked && i < selectedTests.size(); i++) {
                Base_Model_Rate_Calculator selectedTestModel = selectedTests.get(i);

                if (!GlobalClass.isNull(selectedTestModel.getCode()) &&
                        !GlobalClass.isNull(getSelected_test.getCode()) &&
                        selectedTestModel.getCode().equalsIgnoreCase(getSelected_test.getCode())) {
                    viewHolder.iv_checked.setVisibility(View.VISIBLE);
                    viewHolder.iv_unchecked.setVisibility(View.GONE);
                    viewHolder.iv_locked.setVisibility(View.GONE);
                    viewHolder.isSelectedDueToParent = false;
                    viewHolder.parentTestCode = "";
                    isChecked = true;
                } else if (GlobalClass.checkArray(selectedTestModel.getChilds()) && selectedTestModel.checkIfChildsContained(getSelected_test)) {
                    viewHolder.iv_checked.setVisibility(View.GONE);
                    viewHolder.iv_locked.setVisibility(View.VISIBLE);
                    viewHolder.iv_unchecked.setVisibility(View.GONE);
                    viewHolder.isSelectedDueToParent = false;
                    viewHolder.parentTestCode = selectedTestModel.getCode();
                    isChecked = true;
                } else {
                    if (GlobalClass.checkArray(selectedTestModel.getChilds())) {
                        for (Base_Model_Rate_Calculator.Childs ctm :
                                selectedTestModel.getChilds()) {
                            if (ctm.getCode().equals(getSelected_test.getCode())) {

                                viewHolder.iv_checked.setVisibility(View.GONE);
                                viewHolder.iv_unchecked.setVisibility(View.GONE);
                                viewHolder.iv_locked.setVisibility(View.VISIBLE);
                                viewHolder.isSelectedDueToParent = false;
                                viewHolder.parentTestCode = selectedTestModel.getCode();
                                viewHolder.parentTestname = selectedTestModel.getName();
                                isChecked = true;
                                break;
                            } else {
                                viewHolder.iv_checked.setVisibility(View.GONE);
                                viewHolder.iv_unchecked.setVisibility(View.VISIBLE);
                                viewHolder.iv_locked.setVisibility(View.GONE);
                            }
                        }
                    } else {
                        viewHolder.iv_checked.setVisibility(View.GONE);
                        viewHolder.iv_unchecked.setVisibility(View.VISIBLE);
                        viewHolder.iv_locked.setVisibility(View.GONE);
                    }
                }
            }
        } else {
            viewHolder.iv_checked.setVisibility(View.GONE);
            viewHolder.iv_unchecked.setVisibility(View.VISIBLE);
            viewHolder.iv_locked.setVisibility(View.GONE);
        }

        viewHolder.iv_unchecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mgetSingleList.get(position).getCode().equalsIgnoreCase(Constants.CATC)) {
                    boolean isCAGCA = false;
                    for (int i = 0; i < selectedTests.size(); i++) {
                        if (selectedTests.get(i).getCode().equalsIgnoreCase(Constants.CAGCA)) {
                            isCAGCA = true;
                            break;
                        }
                    }
                    if (isCAGCA) {
                        Base_Model_Rate_Calculator ProfileToSelect = null;

                        if (GlobalClass.CheckArrayList(AllProductArrayList)){
                            for (int i = 0; i < AllProductArrayList.size(); i++) {
                                if (AllProductArrayList.get(i).getCode().equalsIgnoreCase(Constants.P690)) {
                                    ProfileToSelect = AllProductArrayList.get(i);
                                    break;
                                }
                            }
                        }

                        if (ProfileToSelect != null) {
                            CallCheckFunction(ProfileToSelect);
                        } else {
                            CallCheckFunction(mgetSingleList.get(position));
                        }
                    } else {
                        CallCheckFunction(mgetSingleList.get(position));
                    }

                } else if (!GlobalClass.isNull(mgetSingleList.get(position).getCode()) &&
                        mgetSingleList.get(position).getCode().equalsIgnoreCase(Constants.CAGCA)) {
                    boolean isCATC = false;

                    if (GlobalClass.CheckArrayList(selectedTests)){
                        for (int i = 0; i < selectedTests.size(); i++) {
                            if (selectedTests.get(i).getCode().equalsIgnoreCase(Constants.CATC)) {
                                isCATC = true;
                                break;
                            }
                        }
                    }

                    if (isCATC) {
                        Base_Model_Rate_Calculator ProfileToSelect = null;

                        if (GlobalClass.CheckArrayList(AllProductArrayList)){
                            for (int i = 0; i < AllProductArrayList.size(); i++) {
                                if (AllProductArrayList.get(i).getCode().equalsIgnoreCase(Constants.P690)) {
                                    ProfileToSelect = AllProductArrayList.get(i);
                                    break;
                                }
                            }
                        }

                        if (ProfileToSelect != null) {
                            CallCheckFunction(ProfileToSelect);
                        } else {
                            CallCheckFunction(mgetSingleList.get(position));
                        }
                    } else {
                        CallCheckFunction(mgetSingleList.get(position));
                    }
                } else {
                    CallCheckFunction(mgetSingleList.get(position));
                }
            }
        });

        viewHolder.iv_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectedDueToParent) {
                    if (GlobalClass.CheckArrayList(selectedTests)){
                        for (int i = 0; i < selectedTests.size(); i++) {
                            if (selectedTests.get(i).getName().equalsIgnoreCase(getSelected_test.getName())) {
                                selectedTests.remove(i);
                            }
                        }
                    }

                    mcallback.onCheckChangeRateCalculator(selectedTests);
                } else {
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    alertDialogBuilder
                            .setMessage(Html.fromHtml("This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode))
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();


                }
                notifyDataSetChanged();
            }
        });


    }

    private void CallCheckFunction(Base_Model_Rate_Calculator getSelected_test) {
        String str = "";
        str = str + getSelected_test.getCode() + ",";
        String slectedpackage = "";

        slectedpackage = getSelected_test.getName();
        tempselectedTests = new ArrayList<>();
        tempselectedTests1 = new ArrayList<>();

        try {
            if (GlobalClass.checkArray(getSelected_test.getChilds())) {
                for (int i = 0; i < getSelected_test.getChilds().length; i++) {
                    //tejas t -----------------------------
                    for (int j = 0; j < selectedTests.size(); j++) {
                        if (getSelected_test.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                            Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());
                            tempselectedTests1.add(selectedTests.get(j).getName());
                            tempselectedTests.add(selectedTests.get(j));
                        } else if (selectedTests.get(j).getCode().equalsIgnoreCase("HEMOGRAM - 6 PART (DIFF)") && getSelected_test.getChilds()[j].getCode().equalsIgnoreCase("H6")) {
                            tempselectedTests1.add(selectedTests.get(j).getName());
                            tempselectedTests.add(selectedTests.get(j));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        try {
            if (GlobalClass.CheckArrayList(selectedTests)) {
                for (int j = 0; j < selectedTests.size(); j++) {
                    Base_Model_Rate_Calculator selectedTestModel123 = selectedTests.get(j);
                    if (selectedTestModel123.getChilds() != null && getSelected_test.getChilds() != null && getSelected_test.checkIfChildsContained(selectedTestModel123)) {
                        tempselectedTests1.add(selectedTests.get(j).getName());
                        tempselectedTests.add(selectedTestModel123);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (GlobalClass.CheckArrayList(tempselectedTests)) {
            String cartproduct = TextUtils.join(",", tempselectedTests1);
            alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
            alertDialogBuilder
                    .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
            androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

        if (GlobalClass.CheckArrayList(tempselectedTests)) {
            for (int i = 0; i < tempselectedTests.size(); i++) {
                for (int j = 0; j < selectedTests.size(); j++) {
                    if (tempselectedTests.get(i).getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                        selectedTests.remove(j);
                    }
                }
            }
            selectedTests.add(getSelected_test);
            mcallback.onCheckChangeRateCalculator(selectedTests);
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return mgetSingleList.size();
    }

    public void getTestdetailResponse(JSONObject response) {
        try {
            Log.e(TAG, "onResponse: " + response);
            TestDetailsResponseModel responseModel = new Gson().fromJson(String.valueOf(response), TestDetailsResponseModel.class);
            if (responseModel != null) {
                if (!GlobalClass.isNull(responseModel.getResponse()) && responseModel.getResponse().equalsIgnoreCase("Success")) {
                    if (GlobalClass.CheckArrayList(responseModel.getTestName())) {
                        showChildTestNamesAdapter = new ShowChildTestNamesAdapter(mContext, responseModel.getTestName());
                        LayoutInflater li = LayoutInflater.from(mContext);
                        View promptsView = li.inflate(R.layout.activity_show_child_list, null);
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
                        alertDialogBuilder.setView(promptsView);
                        testdetails = (RecyclerView) promptsView.findViewById(R.id.testdetails);
                        imgClose = (ImageView) promptsView.findViewById(R.id.imgClose);
                        linearLayoutManager = new LinearLayoutManager(mContext);
                        testdetails.setLayoutManager(linearLayoutManager);
                        testdetails.setAdapter(showChildTestNamesAdapter);
                        alertDialog = alertDialogBuilder.create();
                        if (!((Activity) mContext).isFinishing()) {
                            alertDialog.show();

                        }
                        imgClose.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                alertDialog.dismiss();
                            }
                        });
                    }
                }
            } else {
                GlobalClass.showTastyToast(mContext, ToastFile.something_went_wrong,2);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public boolean isSelectedDueToParent;
        public String parentTestCode, parentTestname;
        TextView test_name_rate_txt, test_rate_cal_txt;
        ImageView iv_checked, iv_unchecked, iv_locked;
        LinearLayout parent_ll, lin_color;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            test_name_rate_txt = (TextView) itemView.findViewById(R.id.test_name_rate_txt);
            test_rate_cal_txt = (TextView) itemView.findViewById(R.id.test_rate_cal_txt);
            iv_checked = (ImageView) itemView.findViewById(R.id.iv_checked);
            iv_unchecked = (ImageView) itemView.findViewById(R.id.iv_unchecked);
            iv_locked = (ImageView) itemView.findViewById(R.id.iv_locked);
            parent_ll = (LinearLayout) itemView.findViewById(R.id.parent_ll);
            lin_color = itemView.findViewById(R.id.lin_color);
        }
    }


}
