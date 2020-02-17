package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class LeadResponseModel {


    /**
     * Productlist : [{"Product_id":"P175","Product_name":"AAROGYAM 1.1","Rate":1000,"is_booking":0,"tests":"AAROGYAM 1.1"},{"Product_id":"P179","Product_name":"AAROGYAM 1.2","Rate":1500,"is_booking":0,"tests":"AAROGYAM 1.2"},{"Product_id":"P180","Product_name":"AAROGYAM 1.3","Rate":2000,"is_booking":0,"tests":"AAROGYAM 1.3"},{"Product_id":"P207","Product_name":"AAROGYAM 1.4","Rate":2500,"is_booking":0,"tests":"AAROGYAM 1.4"},{"Product_id":"P270","Product_name":"AAROGYAM 1.5","Rate":3000,"is_booking":0,"tests":"AAROGYAM 1.5"},{"Product_id":"P208","Product_name":"AAROGYAM 1.6","Rate":3500,"is_booking":0,"tests":"AAROGYAM 1.6"},{"Product_id":"P201","Product_name":"AAROGYAM 1.7","Rate":4000,"is_booking":0,"tests":"AAROGYAM 1.7"},{"Product_id":"P418","Product_name":"AAROGYAM 1.8","Rate":4500,"is_booking":0,"tests":"AAROGYAM 1.8"},{"Product_id":"P523","Product_name":"AAROGYAM A","Rate":660,"is_booking":0,"tests":"AAROGYAM A"},{"Product_id":"P524","Product_name":"AAROGYAM B","Rate":825,"is_booking":0,"tests":"AAROGYAM B"},{"Product_id":"P525","Product_name":"AAROGYAM C","Rate":1650,"is_booking":0,"tests":"AAROGYAM C"},{"Product_id":"P519","Product_name":"AAROGYAM X","Rate":5000,"is_booking":0,"tests":"AAROGYAM X"},{"Product_id":"PROJ1015820","Product_name":"AAROGYAM PLATINUM","Rate":5000,"is_booking":1,"tests":"VELNESS PLATINUM,COMPLETE URINE ANALYSIS,FBS,PPBS"},{"Product_id":"PROJ1015810","Product_name":"AAROGYAM SILVER","Rate":2500,"is_booking":1,"tests":"AAROGYAM 1.3,COMPLETE URINE ANALYSIS,FBS,PPBS"},{"Product_id":"PROJ1015811","Product_name":"AAROGYAM GOLD","Rate":3500,"is_booking":1,"tests":"AAROGYAM 1.4,COMPLETE URINE ANALYSIS,FBS,PPBS"}]
     * RESPONSE : SUCCESS
     * RES_ID : RES00021
     */

    private String RESPONSE;
    private String RES_ID;
    private ArrayList<ProductlistBean> Productlist;

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public ArrayList<ProductlistBean> getProductlist() {
        return Productlist;
    }

    public void setProductlist(ArrayList<ProductlistBean> Productlist) {
        this.Productlist = Productlist;
    }

    public static class ProductlistBean {
        /**
         * Product_id : P175
         * Product_name : AAROGYAM 1.1
         * Rate : 1000
         * is_booking : 0
         * tests : AAROGYAM 1.1
         */

        private String Product_id;
        private String Product_name;
        private int Rate;
        private int is_booking;
        private String tests;

        public String getProduct_id() {
            return Product_id;
        }

        public void setProduct_id(String Product_id) {
            this.Product_id = Product_id;
        }

        public String getProduct_name() {
            return Product_name;
        }

        public void setProduct_name(String Product_name) {
            this.Product_name = Product_name;
        }

        public int getRate() {
            return Rate;
        }

        public void setRate(int Rate) {
            this.Rate = Rate;
        }

        public int getIs_booking() {
            return is_booking;
        }

        public void setIs_booking(int is_booking) {
            this.is_booking = is_booking;
        }

        public String getTests() {
            return tests;
        }

        public void setTests(String tests) {
            this.tests = tests;
        }


        @Override
        public String toString() {
            return "" + Product_name;
        }
    }


}
