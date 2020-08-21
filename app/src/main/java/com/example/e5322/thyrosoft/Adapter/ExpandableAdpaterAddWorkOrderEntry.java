package com.example.e5322.thyrosoft.Adapter;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.CommonItils.MessageConstants;
import com.example.e5322.thyrosoft.Controller.Log;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.Interface.EditTestExpandListAdapterCheckboxDelegate;
import com.example.e5322.thyrosoft.Interface.EditTestExpandListAdapterCheckboxDelegateAddWOE;
import com.example.e5322.thyrosoft.MainModelForAllTests.Product_Rate_MasterModel;
import com.example.e5322.thyrosoft.Models.BaseModel;
import com.example.e5322.thyrosoft.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by E5322 on 07-06-2018.
 */

public class ExpandableAdpaterAddWorkOrderEntry extends BaseExpandableListAdapter {


    private Activity activity;
    ArrayList<String> listOfString;
    private ArrayList<Product_Rate_MasterModel> filteredList;
    private ArrayList<Product_Rate_MasterModel> testRateMasterModels;
    private ArrayList<BaseModel> selectedTests = new ArrayList<>();
    private ArrayList<BaseModel> selectedTestFlag = new ArrayList<>();
    private ArrayList<BaseModel> profileselectedTests = new ArrayList<>();
    private ArrayList<BaseModel> defaultselectedModel = new ArrayList<>();
    private EditTestExpandListAdapterCheckboxDelegate mcallback;
    private EditTestExpandListAdapterCheckboxDelegateAddWOE mcallbackforAdd;
    private androidx.appcompat.app.AlertDialog.Builder alertDialogBuilder;
    private List<String> tempselectedTests1;
    private ArrayList<BaseModel> tempselectedTests;
    private String getTestName;
    String testNames;
    private boolean flagToChangetheTest = false;
    private boolean setFlagFortests = false;

    public ExpandableAdpaterAddWorkOrderEntry(Activity activity, ArrayList<Product_Rate_MasterModel> testRateMasterModels1, String tests, ArrayList<BaseModel> selectedTests, EditTestExpandListAdapterCheckboxDelegate mcallback, EditTestExpandListAdapterCheckboxDelegateAddWOE mcallbackforAdd) {
        this.activity = activity;
        if (this.filteredList != null) {
            filteredList = null;
        }
        this.filteredList = testRateMasterModels1;
        this.testRateMasterModels = testRateMasterModels1;
        this.mcallback = mcallback;
        this.mcallbackforAdd = mcallbackforAdd;
        this.selectedTests = selectedTests;
        this.profileselectedTests = profileselectedTests;
        this.testNames = tests;
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
        return null;
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
        if (GlobalClass.CheckArrayList(filteredList)) {
            GlobalClass.SetText(holder.txtHeader, filteredList.get(groupPosition).getTestType());
        } else {
            GlobalClass.SetText(holder.txtHeader, "");
        }


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewChildHolder holder = null;
        if (convertView == null) {
            LayoutInflater infalInflater = activity.getLayoutInflater();
            convertView = infalInflater.inflate(R.layout.product_child, parent, false);
            holder = new ViewChildHolder();

            SharedPreferences getAddtionalTest = activity.getSharedPreferences("AddTestType", Context.MODE_PRIVATE);
            getTestName = getAddtionalTest.getString("TESTS", "");

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
        final String parentTestCode = holder.parentTestname;


        final BaseModel testRateMasterModel = filteredList.get(groupPosition).getTestRateMasterModels().get(childPosition);

        GlobalClass.SetText(holder.txt_test, testRateMasterModel.getName());
        GlobalClass.SetText(holder.txt_dis_amt, "₹ " + testRateMasterModel.getBillrate() + "/-");
        holder.txt_dis_amt.setVisibility(View.GONE);

        if (!GlobalClass.isNull(testRateMasterModel.getType()) && testRateMasterModel.getType().equalsIgnoreCase(Constants.PRODUCT_POP)) {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.p));
        } else if (!GlobalClass.isNull(testRateMasterModel.getType()) && testRateMasterModel.getType().equalsIgnoreCase(Constants.PRODUCT_PROFILE)) {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.p));
        } else if (!GlobalClass.isNull(testRateMasterModel.getType()) && testRateMasterModel.getType().equalsIgnoreCase(Constants.PRODUCT_TEST)) {
            holder.img_test_type.setImageDrawable(activity.getResources().getDrawable(R.drawable.t));
        }
        if (!GlobalClass.isNull(testRateMasterModel.getFasting()) && testRateMasterModel.getFasting().equalsIgnoreCase("FASTING")) {
            holder.imgTestFasting.setVisibility(View.GONE);
            holder.imgTestFasting.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_fasting));
        } else if (!GlobalClass.isNull(testRateMasterModel.getFasting()) && testRateMasterModel.getFasting().equalsIgnoreCase("NON FASTING")) {
            holder.imgTestFasting.setVisibility(View.GONE);
            holder.imgTestFasting.setImageDrawable(activity.getResources().getDrawable(R.drawable.visit_non_fasting));
        } else {
            holder.imgTestFasting.setVisibility(View.INVISIBLE);
        }

        boolean isChecked = false;
        holder.isSelectedDueToParent = false;
        holder.parentTestCode = "";
        holder.imgChecked.setVisibility(View.GONE);
        holder.imgCheck.setVisibility(View.VISIBLE);


        if (!GlobalClass.isNull(testNames)) {
            String[] elements = testNames.split(",");
            List<String> fixedLenghtList = Arrays.asList(elements);
            listOfString = new ArrayList<String>(fixedLenghtList);

        }

        if (GlobalClass.CheckArrayList(selectedTests)) {
            for (int i = 0; !isChecked && i < selectedTests.size(); i++) {
                BaseModel selectedTestModel = selectedTests.get(i);
                if (!GlobalClass.isNull(selectedTestModel.getCode()) &&
                        !GlobalClass.isNull(testRateMasterModel.getCode()) &&
                        selectedTestModel.getCode().equalsIgnoreCase(testRateMasterModel.getCode())) {
                    holder.imgChecked.setVisibility(View.VISIBLE);
                    holder.imgCheck.setVisibility(View.GONE);
                    holder.isSelectedDueToParent = false;
                    holder.parentTestCode = "";
                    isChecked = true;
                } else if (GlobalClass.checkArray(selectedTestModel.getChilds())
                        && GlobalClass.checkArray(testRateMasterModel.getChilds()) &&
                        selectedTestModel.checkIfChildsContained(testRateMasterModel)) {
                    holder.imgChecked.setVisibility(View.GONE);
                    holder.imgCheck.setVisibility(View.GONE);
                    holder.isSelectedDueToParent = true;
                    holder.parentTestCode = selectedTestModel.getCode();
                    holder.parentTestname = selectedTestModel.getName();
                    isChecked = true;
                } else {
                    if (GlobalClass.checkArray(selectedTestModel.getChilds())) {
                        for (BaseModel.Childs ctm :
                                selectedTestModel.getChilds()) {
                            if (!GlobalClass.isNull(ctm.getCode()) &&
                                    !GlobalClass.isNull(testRateMasterModel.getCode()) &&
                                    ctm.getCode().equalsIgnoreCase(testRateMasterModel.getCode())) {

                                holder.imgChecked.setVisibility(View.GONE);
                                holder.imgCheck.setVisibility(View.GONE);
                                holder.isSelectedDueToParent = true;
                                holder.parentTestCode = selectedTestModel.getCode();
                                holder.parentTestname = selectedTestModel.getName();
                                isChecked = true;
                                break;
                            } else {
                                holder.imgChecked.setVisibility(View.GONE);
                                holder.imgCheck.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        holder.imgChecked.setVisibility(View.GONE);
                        holder.imgCheck.setVisibility(View.VISIBLE);
                    }
                }
            }
        } else {
            holder.imgChecked.setVisibility(View.GONE);
            holder.imgCheck.setVisibility(View.VISIBLE);
        }


        if (GlobalClass.CheckArrayList(listOfString)) {
            for (int i = 0; i < listOfString.size(); i++) {
                if (!GlobalClass.isNull(testRateMasterModel.getCode()) &&
                        !GlobalClass.isNull(listOfString.get(i)) &&
                        testRateMasterModel.getCode().equalsIgnoreCase(listOfString.get(i))) {
                    holder.imgCheck.setVisibility(View.GONE);
                    holder.imgChecked.setVisibility(View.INVISIBLE);
                }
            }
        }


        holder.imgCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String slectedpackage = "";
                slectedpackage = testRateMasterModel.getName();

                tempselectedTests = new ArrayList<>();
                tempselectedTests1 = new ArrayList<>();

                if (!GlobalClass.isNull(testRateMasterModel.getType()) &&
                        testRateMasterModel.getType().equalsIgnoreCase("PROFILE")) {

                    if (GlobalClass.CheckArrayList(selectedTests)) {
                        for (int h = 0; h < selectedTests.size(); h++) {
                            if (!GlobalClass.isNull(selectedTests.get(h).getType()) &&
                                    selectedTests.get(h).getType().equalsIgnoreCase("TEST")) {

                                if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                                    for (int l = 0; l < testRateMasterModel.getChilds().length; l++) {
                                        if (!GlobalClass.isNull(testRateMasterModel.getChilds()[l].getCode()) &&
                                                !GlobalClass.isNull(selectedTests.get(h).getCode()) &&
                                                testRateMasterModel.getChilds()[l].getCode().equalsIgnoreCase(selectedTests.get(h).getCode())) {
                                            selectedTests.remove(selectedTests.get(h));
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (setFlagFortests) {
                        if (flagToChangetheTest) {
                            selectedTestFlag = new ArrayList<>();
                            if (GlobalClass.CheckArrayList(selectedTests)) {
                                for (int i = 0; i < selectedTests.size(); i++) {
                                    if (!GlobalClass.isNull(selectedTests.get(i).getType()) && selectedTests.get(i).getType().equalsIgnoreCase("TEST")) {
                                        selectedTestFlag.add(selectedTests.get(i));
                                    }
                                }
                            }

                            if (GlobalClass.CheckArrayList(selectedTests)) {
                                selectedTests.clear();
                                if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                                    for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {

                                        for (int j = 0; j < selectedTests.size(); j++) {
                                            if (!GlobalClass.isNull(testRateMasterModel.getChilds()[i].getCode()) &&
                                                    !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                                    testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                                Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                                tempselectedTests1.add(selectedTests.get(j).getName());
                                                tempselectedTests.add(selectedTests.get(j));
                                            }
                                        }
                                    }
                                }

                                if (GlobalClass.CheckArrayList(selectedTests)) {
                                    for (int j = 0; j < selectedTests.size(); j++) {
                                        BaseModel selectedTestModel123 = selectedTests.get(j);
                                        if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(testRateMasterModel.getChilds()) && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                                            tempselectedTests1.add(selectedTests.get(j).getName());
                                            tempselectedTests.add(selectedTestModel123);
                                        }
                                    }
                                }


                                if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                    String cartproduct = TextUtils.join(",", tempselectedTests1);
                                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                                    alertDialogBuilder
                                            .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                            .setCancelable(true)
                                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }

                                if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                    for (int i = 0; i < tempselectedTests.size(); i++) {
                                        if (GlobalClass.CheckArrayList(selectedTests)) {
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

                                selectedTests.add(testRateMasterModel);

                                if (GlobalClass.CheckArrayList(selectedTestFlag)) {
                                    for (int i = 0; i < selectedTestFlag.size(); i++) {
                                        selectedTests.add(selectedTestFlag.get(i));
                                    }
                                    mcallback.onCheckChange(selectedTests);
                                    flagToChangetheTest = true;
                                }


                            }
                        } else {
                            if (GlobalClass.CheckArrayList(selectedTests)) {

                                selectedTestFlag = new ArrayList<>();
                                for (int i = 0; i < selectedTests.size(); i++) {
                                    if (!GlobalClass.isNull(selectedTests.get(i).getType()) && selectedTests.get(i).getType().equalsIgnoreCase("TEST")) {
                                        selectedTestFlag.add(selectedTests.get(i));
                                    }
                                }
                                selectedTests.clear();
                                if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                                    for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                                        if (GlobalClass.CheckArrayList(selectedTests)) {
                                            for (int j = 0; j < selectedTests.size(); j++) {
                                                if (!GlobalClass.isNull(testRateMasterModel.getChilds()[i].getCode()) &&
                                                        !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                                        testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                                    Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                                    tempselectedTests1.add(selectedTests.get(j).getName());
                                                    tempselectedTests.add(selectedTests.get(j));
                                                }
                                            }
                                        }

                                    }
                                }

                                if (GlobalClass.CheckArrayList(selectedTests)) {
                                    for (int j = 0; j < selectedTests.size(); j++) {
                                        BaseModel selectedTestModel123 = selectedTests.get(j);
                                        if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(testRateMasterModel.getChilds()) && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                                            tempselectedTests1.add(selectedTests.get(j).getName());
                                            tempselectedTests.add(selectedTestModel123);
                                        }
                                    }
                                }


                                if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                    String cartproduct = TextUtils.join(",", tempselectedTests1);
                                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                                    alertDialogBuilder
                                            .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                            .setCancelable(true)
                                            .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }

                                if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                    for (int i = 0; i < tempselectedTests.size(); i++) {
                                        if (GlobalClass.CheckArrayList(selectedTests)) {
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

                                selectedTests.add(testRateMasterModel);

                                if (GlobalClass.CheckArrayList(selectedTestFlag)) {
                                    for (int i = 0; i < selectedTestFlag.size(); i++) {
                                        selectedTests.add(selectedTestFlag.get(i));
                                    }
                                }

                                mcallback.onCheckChange(selectedTests);
                                flagToChangetheTest = true;

                            } else {
                                if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                                    for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                                        for (int j = 0; j < selectedTests.size(); j++) {

                                            if (!GlobalClass.isNull(testRateMasterModel.getChilds()[i].getCode()) &&
                                                    !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                                    testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                                Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                                tempselectedTests1.add(selectedTests.get(j).getName());
                                                tempselectedTests.add(selectedTests.get(j));
                                            }
                                        }
                                    }
                                }
                                if (GlobalClass.CheckArrayList(selectedTests)) {
                                    for (int j = 0; j < selectedTests.size(); j++) {
                                        BaseModel selectedTestModel123 = selectedTests.get(j);
                                        if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(testRateMasterModel.getChilds()) && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                                            tempselectedTests1.add(selectedTests.get(j).getName());
                                            tempselectedTests.add(selectedTestModel123);
                                        }
                                    }
                                }


                                if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                    String cartproduct = TextUtils.join(",", tempselectedTests1);
                                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                                    alertDialogBuilder
                                            .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                            .setCancelable(true)
                                            .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                }

                                if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                    for (int i = 0; i < tempselectedTests.size(); i++) {
                                        if (GlobalClass.CheckArrayList(selectedTests)){
                                            for (int j = 0; j < selectedTests.size(); j++) {
                                                if (!GlobalClass.isNull(tempselectedTests.get(i).getCode())&&
                                                        !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                                         tempselectedTests.get(i).getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                                    selectedTests.remove(j);
                                                }
                                            }
                                        }

                                    }
                                }


                                flagToChangetheTest = true;
                                selectedTests.add(testRateMasterModel);
                                mcallback.onCheckChange(selectedTests);
                            }
                        }
                    } else {
                        if (GlobalClass.CheckArrayList(selectedTests)) {
                            selectedTests.clear();
                            flagToChangetheTest = true;
                            if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                                for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {

                                    if (GlobalClass.CheckArrayList(selectedTests)){
                                        for (int j = 0; j < selectedTests.size(); j++) {

                                            if (!GlobalClass.isNull(testRateMasterModel.getChilds()[i].getCode()) &&
                                                    !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                                    testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                                Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                                tempselectedTests1.add(selectedTests.get(j).getName());
                                                tempselectedTests.add(selectedTests.get(j));
                                            }
                                        }
                                    }


                                }
                            }

                            if (GlobalClass.CheckArrayList(selectedTests)) {
                                for (int j = 0; j < selectedTests.size(); j++) {
                                    BaseModel selectedTestModel123 = selectedTests.get(j);
                                    if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(testRateMasterModel.getChilds()) && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                                        tempselectedTests1.add(selectedTests.get(j).getName());
                                        tempselectedTests.add(selectedTestModel123);
                                    }
                                }
                            }


                            if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                String cartproduct = TextUtils.join(",", tempselectedTests1);
                                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                                alertDialogBuilder
                                        .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                        .setCancelable(true)
                                        .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }

                            if (GlobalClass.CheckArrayList(tempselectedTests)) {
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

                            selectedTests.add(testRateMasterModel);
                            mcallback.onCheckChange(selectedTests);
                            flagToChangetheTest = true;

                        } else {
                            if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                                for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                                   if (GlobalClass.CheckArrayList(selectedTests)){
                                       for (int j = 0; j < selectedTests.size(); j++) {
                                           if (!GlobalClass.isNull(testRateMasterModel.getChilds()[i].getCode()) &&
                                                   !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                                   testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                               Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                               tempselectedTests1.add(selectedTests.get(j).getName());
                                               tempselectedTests.add(selectedTests.get(j));
                                           }
                                       }
                                   }

                                }
                            }

                            if (GlobalClass.CheckArrayList(selectedTests)) {
                                for (int j = 0; j < selectedTests.size(); j++) {
                                    BaseModel selectedTestModel123 = selectedTests.get(j);
                                    if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(testRateMasterModel.getChilds()) && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {
                                        tempselectedTests1.add(selectedTests.get(j).getName());
                                        tempselectedTests.add(selectedTestModel123);
                                    }
                                }
                            }


                            if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                String cartproduct = TextUtils.join(",", tempselectedTests1);
                                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                                alertDialogBuilder
                                        .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                        .setCancelable(true)
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                            if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                for (int i = 0; i < tempselectedTests.size(); i++) {
                                    if (GlobalClass.CheckArrayList(selectedTests)){
                                        for (int j = 0; j < selectedTests.size(); j++) {
                                            if (tempselectedTests.get(i).getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                                selectedTests.remove(j);
                                            }
                                        }
                                    }

                                }
                            }


                            flagToChangetheTest = true;
                            //tejas t -----------------------------
                            selectedTests.add(testRateMasterModel);
                            mcallback.onCheckChange(selectedTests);
                        }
                    }
                } else if (!GlobalClass.isNull(testRateMasterModel.getType()) && testRateMasterModel.getType().equalsIgnoreCase("TEST")) {
                    //<---------------TODO allow user to add maximum 5 tests from 'TEST' and minimum 1 from 'PROFILE'

                    if (GlobalClass.CheckArrayList(selectedTests)) {
                        for (int i = 0; i < selectedTests.size(); i++) {
                            if (selectedTests.get(i).getType().equalsIgnoreCase("PROFILE")) {
                                flagToChangetheTest = true;
                            }
                        }
                    }
                    //<-------------------TODO if tests is selected from 'PROFILE' then user can add tests from 'TEST'
                    if (flagToChangetheTest) {
                        if (GlobalClass.CheckArrayList(selectedTests) && selectedTests.size() > 5) {
//                            Alter box
                            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                    .setContentText(MessageConstants.select_max_5_test)
                                    .setConfirmText(MessageConstants.OK)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                                for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                                   if (GlobalClass.CheckArrayList(selectedTests)){
                                       for (int j = 0; j < selectedTests.size(); j++) {
                                           if (!GlobalClass.isNull(testRateMasterModel.getChilds()[i].getCode()) &&
                                                   !GlobalClass.isNull(selectedTests.get(j).getCode()) &&
                                                   testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                               Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                               tempselectedTests1.add(selectedTests.get(j).getName());
                                               tempselectedTests.add(selectedTests.get(j));
                                           }
                                       }
                                   }

                                }
                            }


                            if (GlobalClass.CheckArrayList(selectedTests)) {
                                for (int j = 0; j < selectedTests.size(); j++) {
                                    BaseModel selectedTestModel123 = selectedTests.get(j);
                                    if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(testRateMasterModel.getChilds())
                                            && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {

                                        tempselectedTests1.add(selectedTests.get(j).getName());
                                        tempselectedTests.add(selectedTestModel123);
                                    }
                                }
                            }


                            if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                String cartproduct = TextUtils.join(",", tempselectedTests1);
                                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                                alertDialogBuilder
                                        .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                        .setCancelable(true)
                                        .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }

                            if (GlobalClass.CheckArrayList(tempselectedTests)) {
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

                            //tejas t -----------------------------
                            selectedTests.add(testRateMasterModel);
                            mcallback.onCheckChange(selectedTests);
                            setFlagFortests = true;

                        }
                    } else {
                        //<-----------------TODO if tests is not selected from 'PROFILE' then max array size should be 4
                        if (GlobalClass.CheckArrayList(selectedTests) && selectedTests.size() > 4) {
                            new SweetAlertDialog(activity, SweetAlertDialog.WARNING_TYPE)
                                    .setContentText(MessageConstants.select_max_5_test)
                                    .setConfirmText(MessageConstants.OK)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                        }
                                    })
                                    .show();
                        } else {
                            if (GlobalClass.checkArray(testRateMasterModel.getChilds())) {
                                for (int i = 0; i < testRateMasterModel.getChilds().length; i++) {
                                    //tejas t -----------------------------

                                    if (GlobalClass.CheckArrayList(selectedTests)){
                                        for (int j = 0; j < selectedTests.size(); j++) {

                                            if (!GlobalClass.isNull(testRateMasterModel.getChilds()[i].getCode()) &&
                                                    !GlobalClass.isNull(selectedTests.get(j).getCode())&&
                                                    testRateMasterModel.getChilds()[i].getCode().equalsIgnoreCase(selectedTests.get(j).getCode())) {
                                                Log.v("TAG", "Cart selectedtestlist Description :" + selectedTests.get(j).getName() + "Cart selectedtestlist Code :" + selectedTests.get(j).getCode());

                                                tempselectedTests1.add(selectedTests.get(j).getName());
                                                tempselectedTests.add(selectedTests.get(j));
                                            }
                                        }
                                    }

                                }
                            }

                            if (GlobalClass.CheckArrayList(selectedTests)) {
                                if (GlobalClass.CheckArrayList(selectedTests)){
                                    for (int j = 0; j < selectedTests.size(); j++) {
                                        BaseModel selectedTestModel123 = selectedTests.get(j);
                                        if (GlobalClass.checkArray(selectedTestModel123.getChilds()) && GlobalClass.checkArray(testRateMasterModel.getChilds())
                                                && testRateMasterModel.checkIfChildsContained(selectedTestModel123)) {

                                            tempselectedTests1.add(selectedTests.get(j).getName());
                                            tempselectedTests.add(selectedTestModel123);
                                        }
                                    }
                                }

                            }


                            if (GlobalClass.CheckArrayList(tempselectedTests)) {
                                String cartproduct = TextUtils.join(",", tempselectedTests1);
                                alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                                alertDialogBuilder
                                        .setMessage(Html.fromHtml("As " + "<b>" + slectedpackage + "</b>" + " already includes " + "<b>" + cartproduct + "</b>" + " test(s),We have removed " + "<b>" + cartproduct + "</b>" + " test(s) from your Selected test list"))
                                        .setCancelable(true)
                                        .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.dismiss();
                                            }
                                        });
                                androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                            if (GlobalClass.CheckArrayList(tempselectedTests)) {
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

                            //tejas t -----------------------------
                            selectedTests.add(testRateMasterModel);
                            mcallback.onCheckChange(selectedTests);
                            setFlagFortests = true;
                        }
                    }
                }
            }
        });

        holder.imgChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isSelectedDueToParent) {

                    if (GlobalClass.CheckArrayList(selectedTests)) {
                        for (int i = 0; i < selectedTests.size(); i++) {
                            if (!GlobalClass.isNull(selectedTests.get(i).getType()) &&
                                    selectedTests.get(i).getType().equalsIgnoreCase("PROFILE")) {
                                selectedTests.remove(testRateMasterModel);
                                mcallback.onCheckChange(selectedTests);
                                flagToChangetheTest = false;
                            } else {
                                selectedTests.remove(testRateMasterModel);
                                mcallback.onCheckChange(selectedTests);
                            }
                        }
                    }


                } else {
                    alertDialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(activity);
                    alertDialogBuilder
                            .setMessage(Html.fromHtml(MessageConstants.parent_test_already_select + parentTestCode))
                            .setCancelable(true)
                            .setPositiveButton(MessageConstants.OK, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.dismiss();
                                }
                            });
                    androidx.appcompat.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }

            }

        });

        if (GlobalClass.CheckArrayList(testRateMasterModels)) {
            for (int i = 0; i < testRateMasterModels.size(); i++) {
                if (GlobalClass.CheckArrayList(testRateMasterModels.get(i).getTestRateMasterModels())) {
                    for (int j = 0; j < testRateMasterModels.get(i).getTestRateMasterModels().size(); j++) {
                        if (!GlobalClass.isNull(testRateMasterModels.get(i).getTestRateMasterModels().get(j).getName()) &&
                                !GlobalClass.isNull(getTestName) &&
                                testRateMasterModels.get(i).getTestRateMasterModels().get(j).getName().equalsIgnoreCase(getTestName)) {
                            defaultselectedModel.add(testRateMasterModels.get(i).getTestRateMasterModels().get(j));
                            mcallbackforAdd.onCheckChangeAddWoe(defaultselectedModel);
                        } else if (!GlobalClass.isNull(testRateMasterModels.get(i).getTestRateMasterModels().get(j).getCode()) &&
                                !GlobalClass.isNull(getTestName) &&
                                testRateMasterModels.get(i).getTestRateMasterModels().get(j).getCode().equalsIgnoreCase(getTestName)) {
                            defaultselectedModel.add(testRateMasterModels.get(i).getTestRateMasterModels().get(j));
                            mcallbackforAdd.onCheckChangeAddWoe(defaultselectedModel);
                        }
                    }
                }
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
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
        if (query.isEmpty()) {
            if (filteredList != null) {
                filteredList = null;
            }
            filteredList = new ArrayList<>();
            filteredList.addAll(testRateMasterModels);
//            Logger.verbose("FilteredListSizeAfterFilerQueryEmpty: " + String.valueOf(filteredList.size()));

        } else {
            if (filteredList != null) {
                filteredList = null;
            }
            filteredList = new ArrayList<>();
            for (Product_Rate_MasterModel testTypeModel : testRateMasterModels) {

                ArrayList<BaseModel> oldList = testTypeModel.getTestRateMasterModels();
                ArrayList<BaseModel> newList = new ArrayList<BaseModel>();
                for (BaseModel testModel : oldList) {
                    if (testModel.getName().toLowerCase().contains(query)) {
                        newList.add(testModel);
                    }
                }
                if (GlobalClass.CheckArrayList(newList)) {
                    Product_Rate_MasterModel nContinent = new Product_Rate_MasterModel(testTypeModel.getTestType(), newList);
                    filteredList.add(nContinent);
                }
            }
        }

        notifyDataSetChanged();

    }
}

