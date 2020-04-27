package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class GetScanResponse {


    /**
     * RODETAILS : [{"Closing_stock":"100","Opening_balance":"100","Used_stock":"0","Wastage_stock":"0"},{"Closing_stock":"83","Opening_balance":"100","Used_stock":"5","Wastage_stock":"12"}]
     * red_id : RES0000
     * response : SUCCESS
     */

    private String red_id;
    private String response;
    private List<RODETAILSBean> RODETAILS;

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

    public List<RODETAILSBean> getRODETAILS() {
        return RODETAILS;
    }

    public void setRODETAILS(List<RODETAILSBean> RODETAILS) {
        this.RODETAILS = RODETAILS;
    }

    public static class RODETAILSBean {
        /**
         * Closing_stock : 100
         * Opening_balance : 100
         * Used_stock : 0
         * Wastage_stock : 0
         */

        private String Closing_stock;
        private String Opening_balance;
        private String Used_stock;
        private String Wastage_stock;

        public String getClosing_stock() {
            return Closing_stock;
        }

        public void setClosing_stock(String Closing_stock) {
            this.Closing_stock = Closing_stock;
        }

        public String getOpening_balance() {
            return Opening_balance;
        }

        public void setOpening_balance(String Opening_balance) {
            this.Opening_balance = Opening_balance;
        }

        public String getUsed_stock() {
            return Used_stock;
        }

        public void setUsed_stock(String Used_stock) {
            this.Used_stock = Used_stock;
        }

        public String getWastage_stock() {
            return Wastage_stock;
        }

        public void setWastage_stock(String Wastage_stock) {
            this.Wastage_stock = Wastage_stock;
        }
    }
}
