package com.example.e5322.thyrosoft.Models.ResponseModels;

public class WOEResponseModel {
    private String RES_ID, barcode_patient_id, message, status, barcode_id;

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public String getBarcode_patient_id() {
        return barcode_patient_id;
    }

    public void setBarcode_patient_id(String barcode_patient_id) {
        this.barcode_patient_id = barcode_patient_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBarcode_id() {
        return barcode_id;
    }

    public void setBarcode_id(String barcode_id) {
        this.barcode_id = barcode_id;
    }
}
