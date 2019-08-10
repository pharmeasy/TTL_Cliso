package com.example.e5322.thyrosoft.Cliso_BMC.Models;

import java.util.ArrayList;

public class MainMaterialModel {
    String Code,Response,ResponseId;
    ArrayList<MaterialDetailsModel> MaterialDetails;

    public MainMaterialModel() {
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String responseId) {
        ResponseId = responseId;
    }

    public ArrayList<MaterialDetailsModel> getMaterialDetails() {
        return MaterialDetails;
    }

    public void setMaterialDetails(ArrayList<MaterialDetailsModel> materialDetails) {
        MaterialDetails = materialDetails;
    }
}
