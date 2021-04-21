package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class ScanBarcodeResponseModel {


    /**
     * PouchCode : [{"Barcode":"TEST123"}]
     * RespID : RES0000
     * Response : SUCCESS!
     */

    private String RespID;
    private String Response;
    private ArrayList<PouchCodeDTO> Barcode;

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

    public ArrayList<PouchCodeDTO> getBarcode() {
        return Barcode;
    }

    public void setBarcode(ArrayList<PouchCodeDTO> PouchCode) {
        this.Barcode = PouchCode;
    }

    public static class PouchCodeDTO {
        /**
         * Barcode : TEST123
         */

        private String Barcode;

        public String getBarcode() {
            return Barcode;
        }

        public void setBarcode(String Barcode) {
            this.Barcode = Barcode;
        }
    }
}
