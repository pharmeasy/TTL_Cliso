package com.example.e5322.thyrosoft.Activity;

import java.util.ArrayList;

public class LeggyVideoModel {
    private String RES_ID, response;
    ArrayList<LeggyVideoListModel> Output;

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getRESPONSE() {
        return response;
    }

    public void setRESPONSE(String RESPONSE) {
        this.response = RESPONSE;
    }

    public ArrayList<LeggyVideoListModel> getOutput() {
        return Output;
    }

    public void setOutput(ArrayList<LeggyVideoListModel> output) {
        Output = output;
    }
}