package com.example.e5322.thyrosoft.Models.RequestModels;

public class DownloadReceiptRequestModel {
    private String apikey,barcode;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}
