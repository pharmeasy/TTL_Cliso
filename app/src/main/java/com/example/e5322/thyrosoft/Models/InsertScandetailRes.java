package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class InsertScandetailRes {

    /**
     * red_id : RES0001
     * response : SUCCESS
     * rodetails : [{"barcode":"100437001","source":"TAM03"}]
     */

    private String red_id;
    private String response;
    private List<RodetailsBean> rodetails;

    public String getRed_id() {
        return red_id;
    }

    public void setRed_id(String red_id) {
        this.red_id = red_id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<RodetailsBean> getRodetails() {
        return rodetails;
    }

    public void setRodetails(List<RodetailsBean> rodetails) {
        this.rodetails = rodetails;
    }

    public static class RodetailsBean {
        /**
         * barcode : 100437001
         * source : TAM03
         */

        private String barcode;
        private String source;

        public String getBarcode() {
            return barcode;
        }

        public void setBarcode(String barcode) {
            this.barcode = barcode;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
