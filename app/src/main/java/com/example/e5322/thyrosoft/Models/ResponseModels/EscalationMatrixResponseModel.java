package com.example.e5322.thyrosoft.Models.ResponseModels;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EscalationMatrixResponseModel {

    @SerializedName("ResponseId")
    private String ResponseId;

    @SerializedName("Response")
    private String Response;

    @SerializedName("MatrixDetails")
    ArrayList<MatrixDetails> matrixDetails;

    @SerializedName("Mobile")
    private String Mobile;

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getResponseId() {
        return ResponseId;
    }

    public void setResponseId(String responseId) {
        ResponseId = responseId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public ArrayList<MatrixDetails> getMatrixDetails() {
        return matrixDetails;
    }

    public void setMatrixDetails(ArrayList<MatrixDetails> matrixDetails) {
        this.matrixDetails = matrixDetails;
    }

    public class MatrixDetails {

        @SerializedName("Type")
        private String Type;

        @SerializedName("Incharge")
        private String Incharge;

        @SerializedName("L1")
        private String L1;

        @SerializedName("L2")
        private String L2;

        @SerializedName("L3")
        private String L3;

        public String getType() {
            return Type;
        }

        public void setType(String type) {
            Type = type;
        }

        public String getIncharge() {
            return Incharge;
        }

        public void setIncharge(String incharge) {
            Incharge = incharge;
        }

        public String getL1() {
            return L1;
        }

        public void setL1(String l1) {
            L1 = l1;
        }

        public String getL2() {
            return L2;
        }

        public void setL2(String l2) {
            L2 = l2;
        }

        public String getL3() {
            return L3;
        }

        public void setL3(String l3) {
            L3 = l3;
        }
    }
}