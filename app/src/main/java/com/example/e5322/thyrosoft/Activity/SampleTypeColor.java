package com.example.e5322.thyrosoft.Activity;

import android.content.Context;
import android.widget.LinearLayout;

import com.example.e5322.thyrosoft.API.Constants;
import com.example.e5322.thyrosoft.GlobalClass;
import com.example.e5322.thyrosoft.MainModelForAllTests.Outlabdetails_OutLab;
import com.example.e5322.thyrosoft.R;
import com.example.e5322.thyrosoft.RateCalculatorForModels.Base_Model_Rate_Calculator;

import java.util.ArrayList;

public class SampleTypeColor {

    private Context mContext;
    String comefrom;
    int position = 0;
    ArrayList<Base_Model_Rate_Calculator> base_model_rate_calculators;
    ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs;


    public SampleTypeColor(Context context, ArrayList<Base_Model_Rate_Calculator> base_model_rate_calculators, int position) {
        this.mContext = context;
        this.position = position;
        this.base_model_rate_calculators = base_model_rate_calculators;
    }

    public SampleTypeColor(Context context, String type, ArrayList<Outlabdetails_OutLab> outlabdetails_outLabs, int position) {
        this.mContext = context;
        this.comefrom = type;
        this.position = position;
        this.outlabdetails_outLabs = outlabdetails_outLabs;
    }

    public void ttlcolor(LinearLayout lin_color) {
        boolean edta = false;
        boolean serum = false;
        boolean urine = false;
        boolean fluride = false;
        boolean lithium = false;
        boolean sodium = false;
        boolean other = false;

        try {
            if (GlobalClass.CheckArrayList(base_model_rate_calculators)) {
                if (GlobalClass.checkArray( base_model_rate_calculators.get(position).getBarcodes())){
                    for (int j = 0; j < base_model_rate_calculators.get(position).getBarcodes().length; j++) {
                        if (GlobalClass.checkArray(base_model_rate_calculators.get(position).getBarcodes())) {
                            if (!GlobalClass.isNull(base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type()) && base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type().equalsIgnoreCase(Constants.EDTA)) {
                                if (!edta) {
                                    edta = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.edta));
                                }
                            } else if (!GlobalClass.isNull(base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type()) && base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type().equalsIgnoreCase(Constants.SERUM)) {
                                if (!serum) {
                                    serum = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.serum));
                                }
                            } else if (!GlobalClass.isNull(base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type()) && base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type().equalsIgnoreCase(Constants.URINE)) {
                                if (!urine) {
                                    urine = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.urine));
                                }

                            } else if (!GlobalClass.isNull(base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type()) && base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type().equalsIgnoreCase(Constants.FLUORIDE)) {
                                if (!fluride) {
                                    fluride = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.flouride));
                                }

                            } else if (!GlobalClass.isNull(base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type()) && base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type().equalsIgnoreCase(Constants.LITHIUMHEPARIN)) {
                                if (!lithium) {
                                    lithium = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.lithium));
                                }

                            } else if (!GlobalClass.isNull(base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type()) && base_model_rate_calculators.get(position).getBarcodes()[j].getSpecimen_type().equalsIgnoreCase(Constants.SODIUMHEPARIN)) {
                                if (!sodium) {
                                    sodium = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.sodium));
                                }
                            } else {
                                if (!other) {
                                    other = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.other));
                                }

                            }
                        }
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void outLabcolor(LinearLayout lin_color) {
        boolean edta = false;
        boolean serum = false;
        boolean urine = false;
        boolean fluride = false;
        boolean lithium = false;
        boolean sodium = false;
        boolean other = false;
        try {
            if (GlobalClass.CheckArrayList(outlabdetails_outLabs)) {
                if (GlobalClass.checkArray(outlabdetails_outLabs.get(position).getSampletype())) {
                    for (int j = 0; j < outlabdetails_outLabs.get(position).getSampletype().length; j++) {
                        if (outlabdetails_outLabs.get(position).getSampletype() != null || outlabdetails_outLabs.get(position).getSampletype().length != 0) {
                            if (!GlobalClass.isNull(outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype()) && outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype().equalsIgnoreCase(Constants.EDTA)) {
                                if (!edta) {
                                    edta = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.edta));
                                }
                            } else if (!GlobalClass.isNull(outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype()) && outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype().equalsIgnoreCase(Constants.SERUM)) {

                                if (!serum) {
                                    serum = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.serum));
                                }

                            } else if (!GlobalClass.isNull(outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype()) && outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype().equalsIgnoreCase(Constants.URINE)) {
                                if (!urine) {
                                    urine = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.urine));
                                }
                            } else if (!GlobalClass.isNull(outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype()) && outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype().equalsIgnoreCase(Constants.FLUORIDE)) {
                                if (!fluride) {
                                    fluride = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.flouride));
                                }

                            } else if (!GlobalClass.isNull(outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype()) && outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype().equalsIgnoreCase(Constants.LITHIUMHEPARIN)) {
                                if (!lithium) {
                                    lithium = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.lithium));
                                }

                            } else if (!GlobalClass.isNull(outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype()) && outlabdetails_outLabs.get(position).getSampletype()[j].getOutlabsampletype().equalsIgnoreCase(Constants.SODIUMHEPARIN)) {
                                if (!sodium) {
                                    sodium = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.sodium));
                                }
                            } else {
                                if (!other) {
                                    other = true;
                                    GlobalClass.dynamicolordot(mContext, lin_color, mContext.getResources().getColor(R.color.other));
                                }
                            }
                        }

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
