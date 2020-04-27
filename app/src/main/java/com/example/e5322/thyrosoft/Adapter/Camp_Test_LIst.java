package com.example.e5322.thyrosoft.Adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.CampIntimation;
import com.example.e5322.thyrosoft.Fragment.RateCalculatorFragment;
import com.example.e5322.thyrosoft.Interface.SendTestListfromCampList;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;
import com.sdsmdg.tastytoast.TastyToast;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class Camp_Test_LIst extends RecyclerView.Adapter<Camp_Test_LIst.ViewHolder> {
    String selected = "n";
    private SendTestListfromCampList mcallback;
    View view;
    CampIntimation mContext;
    ArrayList<Base_Model_Rate_Calculator> base_model_rate_calculators;
    ArrayList<Base_Model_Rate_Calculator> filteredList;
    private ArrayList<Base_Model_Rate_Calculator> tempselectedTests;
    private List<String> tempselectedTests1;
    Base_Model_Rate_Calculator getSelected_test;
    private ArrayList<Base_Model_Rate_Calculator> selectedTests = new ArrayList<>();
    Base_Model_Rate_Calculator.Childs selectedchildlist[];
    private ArrayList<Base_Model_Rate_Calculator> totalgetAllTests;
    private android.support.v7.app.AlertDialog.Builder alertDialogBuilder;
    ShowChildTestNamesAdapter showChildTestNamesAdapter;
    RecyclerView testdetails;
    private ImageView imgClose;
    private LinearLayoutManager linearLayoutManager;
    ArrayList<String> storetestlist;
    AlertDialog alertDialog, alert;
    public static com.android.volley.RequestQueue POstQueSendEstimation;
    public String TAG = RateCalculatorFragment.class.getSimpleName().toString();

    @NonNull
    @Override
    public Camp_Test_LIst.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        view = inflater.inflate(R.layout.rate_cal_test_single, viewGroup, false);
        Camp_Test_LIst.ViewHolder vh = new Camp_Test_LIst.ViewHolder(view);
        return vh;
    }

    public Camp_Test_LIst(CampIntimation mContext, ArrayList<Base_Model_Rate_Calculator> finalgetAllTests, ArrayList<Base_Model_Rate_Calculator> getTotalArray, ArrayList<Base_Model_Rate_Calculator> selectedTests, SendTestListfromCampList mcallback) {
        this.mContext = mContext;
        this.base_model_rate_calculators = finalgetAllTests;
        this.totalgetAllTests = getTotalArray;
        this.filteredList = finalgetAllTests;
        this.mcallback = mcallback;
        this.selectedTests = selectedTests;
        if (this.selectedTests == null) {
            this.selectedTests = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_Test_LIst.ViewHolder viewHolder, int position) {
        viewHolder.test_name_rate_txt.setText(base_model_rate_calculators.get(position).getName());

        NumberFormat numberFormat = NumberFormat.getNumberInstance(new Locale("en", "in"));
        int passThvalue = Integer.parseInt(base_model_rate_calculators.get(position).getRate().getB2c());

        String setRtaesToTests = numberFormat.format(passThvalue);

        final boolean isSelectedDueToParent = viewHolder.isSelectedDueToParent;
        final String parentTestCode = viewHolder.parentTestCode;
        final Base_Model_Rate_Calculator getSelected_test = filteredList.get(position);

        viewHolder.test_rate_cal_txt.setVisibility(View.GONE);


     /*   if (base_model_rate_calculators.get(position).getType().equalsIgnoreCase(Constants.PRODUCT_TEST)) {
            viewHolder.txt_type.setText("T");
        } else if (base_model_rate_calculators.get(position).getType().equalsIgnoreCase(Constants.PRODUCT_PROFILE)) {
            viewHolder.txt_type.setText("P");
        } else if (base_model_rate_calculators.get(position).getType().equalsIgnoreCase(Constants.PRODUCT_POP)) {
            viewHolder.txt_type.setText("PO");
        }*/
        boolean isChecked = false;
        viewHolder.isSelectedDueToParent = false;
        viewHolder.parentTestCode = "";

        if (selectedTests != null && selectedTests.size() > 0) {
            for (int i = 0; !isChecked && i < selectedTests.size(); i++) {
                Base_Model_Rate_Calculator selectedTestModel = selectedTests.get(i);
                if (selectedTestModel.getCode().equals(getSelected_test.getCode())) {
                    viewHolder.iv_checked.setVisibility(View.VISIBLE);
                    viewHolder.iv_unchecked.setVisibility(View.GONE);
                    viewHolder.iv_locked.setVisibility(View.GONE);
                    viewHolder.isSelectedDueToParent = false;
                    viewHolder.parentTestCode = "";
                    isChecked = true;
                } else if (selectedTestModel.getChilds() != null && getSelected_test.getChilds() != null && selectedTestModel.checkIfChildsContained(getSelected_test)) {
                    viewHolder.iv_checked.setVisibility(View.GONE);
                    viewHolder.iv_locked.setVisibility(View.VISIBLE);
                    viewHolder.iv_unchecked.setVisibility(View.GONE);
                    viewHolder.isSelectedDueToParent = false;
                    viewHolder.parentTestCode = selectedTestModel.getCode();
                    isChecked = true;
                } else {
                    if (selectedTestModel.getChilds() != null && selectedTestModel.getChilds().length > 0) {
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

                if(selectedTests.size()>0){
                    TastyToast.makeText(mContext, "Only one test can be selected", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
                }

                else{
                    String str = "";

                    str = str + getSelected_test.getCode() + ",";
                    String slectedpackage = "";


                    slectedpackage = getSelected_test.getName();
                    tempselectedTests = new ArrayList<>();
                    tempselectedTests1 = new ArrayList<>();


                    if (getSelected_test.getChilds() != null) {
                        for (int i = 0; i < getSelected_test.getChilds().length; i++) {
                            //tejas t -----------------------------
                            for (int j = 0; j < selectedTests.size(); j++) {


                                if (getSelected_test.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                    System.out.println("Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                    tempselectedTests1.add(selectedTests.get(j).getName());
                                    tempselectedTests.add(selectedTests.get(j));


                                } else if (selectedTests.get(j).getCode().equalsIgnoreCase("HEMOGRAM - 6 PART (DIFF)") && getSelected_test.getChilds()[j].getCode().equalsIgnoreCase("H6")) {
                                    tempselectedTests1.add(selectedTests.get(j).getName());
                                    tempselectedTests.add(selectedTests.get(j));
                                }
                            }
                        }
                    }
                    for (int j = 0; j < selectedTests.size(); j++) {
                        Base_Model_Rate_Calculator selectedTestModel123 = selectedTests.get(j);
                        if (selectedTestModel123.getChilds() != null && getSelected_test.getChilds() != null && getSelected_test.checkIfChildsContained(selectedTestModel123)) {

                            tempselectedTests1.add(selectedTests.get(j).getName());
                            tempselectedTests.add(selectedTestModel123);
                        }
                    }

                    if (tempselectedTests != null && tempselectedTests.size() > 0) {
                        String cartproduct = TextUtils.join(",", tempselectedTests1);
                        alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext);
                        alertDialogBuilder
                                .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                .setCancelable(true)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
//                                    alertDialog.dismiss();
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
                    selectedTests.add(getSelected_test);
                    mcallback.onClisktheTest(selectedTests);
                    notifyDataSetChanged();
                }
            }
        });
        viewHolder.iv_checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isSelectedDueToParent) {

                    selectedTests.remove(getSelected_test);
                    mcallback.onClisktheTest(selectedTests);

                } else {
                    alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(mContext);
                    alertDialogBuilder
                            .setMessage(Html.fromHtml("This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode))
                            .setCancelable(true)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                    alertDialog.dismiss();
                                }
                            });
                    android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                    //Toast.makeText(activity, "This test was selected because of its parent. If you wish to remove this test please remove the parent: " + parentTestCode, Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            }
        });


    }

    @Override
    public int getItemCount() {
        return base_model_rate_calculators.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView test_name_rate_txt, test_rate_cal_txt, txt_type;
        ImageView iv_checked, iv_unchecked, iv_locked;
        LinearLayout parent_ll;
        public boolean isSelectedDueToParent;
        public String parentTestCode, parentTestname;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            test_name_rate_txt = (TextView) itemView.findViewById(R.id.test_name_rate_txt);
            test_rate_cal_txt = (TextView) itemView.findViewById(R.id.test_rate_cal_txt);
            //txt_type = (TextView) itemView.findViewById(R.id.txt_type);
            iv_checked = (ImageView) itemView.findViewById(R.id.iv_checked);
            iv_unchecked = (ImageView) itemView.findViewById(R.id.iv_unchecked);
            iv_locked = (ImageView) itemView.findViewById(R.id.iv_locked);
            parent_ll = (LinearLayout) itemView.findViewById(R.id.parent_ll);
        }
    }
}
