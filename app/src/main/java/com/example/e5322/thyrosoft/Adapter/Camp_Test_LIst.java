package com.example.e5322.thyrosoft.Adapter;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.e5322.thyrosoft.Activity.CampIntimation;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.Fragment.RateCalculatorFragment;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.SendTestListfromCampList;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Camp_Test_LIst extends RecyclerView.Adapter<Camp_Test_LIst.ViewHolder> {
    private SendTestListfromCampList mcallback;
    View view;
    CampIntimation mContext;
    ArrayList<Base_Model_Rate_Calculator> base_model_rate_calculators;
    ArrayList<Base_Model_Rate_Calculator> filteredList;
    private ArrayList<Base_Model_Rate_Calculator> tempselectedTests;
    private List<String> tempselectedTests1;
    private ArrayList<Base_Model_Rate_Calculator> selectedTests = new ArrayList<>();
    private androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
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
        this.filteredList = finalgetAllTests;
        this.mcallback = mcallback;
        this.selectedTests = selectedTests;
        if (this.selectedTests == null) {
            this.selectedTests = new ArrayList<>();
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_Test_LIst.ViewHolder viewHolder, int position) {
        GlobalClass.SetText(viewHolder.test_name_rate_txt,base_model_rate_calculators.get(position).getName());

        final boolean isSelectedDueToParent = viewHolder.isSelectedDueToParent;
        final String parentTestCode = viewHolder.parentTestCode;
        final Base_Model_Rate_Calculator getSelected_test = filteredList.get(position);

        viewHolder.test_rate_cal_txt.setVisibility(View.GONE);

        boolean isChecked = false;
        viewHolder.isSelectedDueToParent = false;
        viewHolder.parentTestCode = "";

        if (GlobalClass.CheckArrayList(selectedTests)) {
            for (int i = 0; !isChecked && i < selectedTests.size(); i++) {
                Base_Model_Rate_Calculator selectedTestModel = selectedTests.get(i);
                if (!GlobalClass.isNull(selectedTestModel.getCode()) && !GlobalClass.isNull(getSelected_test.getCode()) &&
                        selectedTestModel.getCode().equals(getSelected_test.getCode())) {
                    viewHolder.iv_checked.setVisibility(View.VISIBLE);
                    viewHolder.iv_unchecked.setVisibility(View.GONE);
                    viewHolder.iv_locked.setVisibility(View.GONE);
                    viewHolder.isSelectedDueToParent = false;
                    viewHolder.parentTestCode = "";
                    isChecked = true;
                } else if (GlobalClass.checkArray(selectedTestModel.getChilds()) && GlobalClass.checkArray(getSelected_test.getChilds()) && selectedTestModel.checkIfChildsContained(getSelected_test)) {
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
                            if (GlobalClass.isNull(ctm.getCode()) && !GlobalClass.isNull(getSelected_test.getCode()) && ctm.getCode().equalsIgnoreCase(getSelected_test.getCode())) {

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

                if(GlobalClass.CheckArrayList(selectedTests)){
                    GlobalClass.showTastyToast((Activity)mContext, MessageConstants.only_one_test,2);

                }

                else{

                    String slectedpackage = "";
                    slectedpackage = getSelected_test.getName();
                    tempselectedTests = new ArrayList<>();
                    tempselectedTests1 = new ArrayList<>();


                    if (GlobalClass.checkArray(getSelected_test.getChilds())) {
                        for (int i = 0; i < getSelected_test.getChilds().length; i++) {
                            if (GlobalClass.CheckArrayList(selectedTests)) {
                                for (int j = 0; j < selectedTests.size(); j++) {
                                    if (!GlobalClass.isNull(getSelected_test.getChilds()[i].getCode()) &&
                                            !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                            getSelected_test.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                        Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                        tempselectedTests1.add(selectedTests.get(j).getName());
                                        tempselectedTests.add(selectedTests.get(j));


                                    } else if (!GlobalClass.isNull(selectedTests.get(j).getCode()) && selectedTests.get(j).getCode().equalsIgnoreCase("HEMOGRAM - 6 PART (DIFF)") && getSelected_test.getChilds()[j].getCode().equalsIgnoreCase("H6")) {
                                        tempselectedTests1.add(selectedTests.get(j).getName());
                                        tempselectedTests.add(selectedTests.get(j));
                                    }
                                }
                            }
                        }
                    }

                    if (GlobalClass.CheckArrayList(selectedTests)){
                        for (int j = 0; j < selectedTests.size(); j++) {
                            Base_Model_Rate_Calculator selectedTestModel123 = selectedTests.get(j);
                            if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(getSelected_test.getChilds()) && getSelected_test.checkIfChildsContained(selectedTestModel123)) {
                                tempselectedTests1.add(selectedTests.get(j).getName());
                                tempselectedTests.add(selectedTestModel123);
                            }
                        }
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

                    if (GlobalClass.CheckArrayList(tempselectedTests)){
                        for (int i = 0; i < tempselectedTests.size(); i++) {
                            if (GlobalClass.CheckArrayList(selectedTests)){
                                for (int j = 0; j < selectedTests.size(); j++) {
                                    if (!GlobalClass.isNull(tempselectedTests.get(i).getCode()) &&
                                            !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                            tempselectedTests.get(i).getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                        selectedTests.remove(j);
                                    }
                                }
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
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(mContext);
                    alertDialogBuilder
                            .setMessage(Html.fromHtml(MessageConstants.PARENT_CHILD_REALTION_MSG + parentTestCode))
                            .setCancelable(true)
                            .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
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
            iv_checked = (ImageView) itemView.findViewById(R.id.iv_checked);
            iv_unchecked = (ImageView) itemView.findViewById(R.id.iv_unchecked);
            iv_locked = (ImageView) itemView.findViewById(R.id.iv_locked);
            parent_ll = (LinearLayout) itemView.findViewById(R.id.parent_ll);
        }
    }
}
