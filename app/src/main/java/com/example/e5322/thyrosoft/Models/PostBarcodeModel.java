package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class PostBarcodeModel {


    /**
     * consignmentBarcodes : [{"Barcode":"A1234567"},{"Barcode":"B1234567"}]
     * PouchCode : AA845721
     * DacCode : TAM03
     */

    private String PouchCode;
    private String DacCode;
    private ArrayList<ConsignmentBarcodesDTO> consignmentBarcodes;

    public String getPouchCode() {
        return PouchCode;
    }

    public void setPouchCode(String PouchCode) {
        this.PouchCode = PouchCode;
    }

    public String getDacCode() {
        return DacCode;
    }

    public void setDacCode(String DacCode) {
        this.DacCode = DacCode;
    }

    public ArrayList<ConsignmentBarcodesDTO> getConsignmentBarcodes() {
        return consignmentBarcodes;
    }

    public void setConsignmentBarcodes(ArrayList<ConsignmentBarcodesDTO> consignmentBarcodes) {
        this.consignmentBarcodes = consignmentBarcodes;
    }

    public static class ConsignmentBarcodesDTO {
        /**
         * Barcode : A1234567
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
