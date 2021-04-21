package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class BarcodeResponseModel {


    /**
     * Barcode : [{"Barcode":"U7198774"},{"Barcode":"U7198775"},{"Barcode":"M6884544"}]
     * RespID : RES0000
     * Response : SUCCESS!
     */

    private String RespID;
    private String Response;
    private ArrayList<BarcodeDTO> Barcode;

    public String getRespID() {
        return RespID;
    }

    public void setRespID(String RespID) {
        this.RespID = RespID;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public ArrayList<BarcodeDTO> getBarcode() {
        return Barcode;
    }

    public void setBarcode(ArrayList<BarcodeDTO> Barcode) {
        this.Barcode = Barcode;
    }

    public static class BarcodeDTO {
        /**
         * Barcode : U7198774
         */

        private String Barcode;

        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public String getBarcode() {
            return Barcode;
        }

        public void setBarcode(String Barcode) {
            this.Barcode = Barcode;
        }

    }
}
