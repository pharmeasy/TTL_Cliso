package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;
import java.util.List;

public class Cmpdt_Model {


    /**
     * Contact_Array_list : [{"CATEGORY":"SUPPORT","DESIGNATION":"","ECODE":"E0365","EMAIL_ID":"sureshp@thyrocare.com","IMG":null,"MOBILE":"8451800524","NAME":"MR PONVELLINGASURESH C","ROLE":"Issues related to Consignment, WOE and Samples"},{"CATEGORY":"SUPPORT","DESIGNATION":"Sr. Assistant- Laboratory","ECODE":"E0556","EMAIL_ID":"udaya.moolya@thyrocare.com","IMG":null,"MOBILE":"8451800526","NAME":"MR UDAYA MOOLYA","ROLE":"Issues related to Consignment, WOE and Samples"},{"CATEGORY":"SUPPORT","DESIGNATION":"Officer - Customer Support","ECODE":"E2825","EMAIL_ID":"navneet.singh@thyrocare.com","IMG":null,"MOBILE":"9158045399","NAME":"MR NAVNEET SINGH","ROLE":" Manages BTech Queries and Complaints"},{"CATEGORY":"SUPPORT","DESIGNATION":"Officer - Audit","ECODE":"E3083","EMAIL_ID":"nandan.k@thyrocare.com","IMG":null,"MOBILE":"9769900235","NAME":"MR NANDAN K","ROLE":"Manages Audit and Compliances"},{"CATEGORY":"SUPPORT","DESIGNATION":"Sr. Executive -Network","ECODE":"E1206","EMAIL_ID":"mahesh.kumar@thyrocare.com","IMG":null,"MOBILE":"7738159210","NAME":"MR MAHESH J KUMAR","ROLE":"South Franchisee Support ,Queries related to credits ,debits and operations"},{"CATEGORY":"SUPPORT","DESIGNATION":"Sr. Executive - Customer Support","ECODE":"E2378","EMAIL_ID":"bhakti.satam@thyrocare.com","IMG":null,"MOBILE":"9870467495","NAME":"MS BHAKTI SATAM","ROLE":" Manages  Complaints"},{"CATEGORY":"SUPPORT","DESIGNATION":"Officer -Materials","ECODE":"E2136","EMAIL_ID":"prabhakar.noule@thyrocare.com","IMG":null,"MOBILE":"8451007411","NAME":"MR PRABHAKAR NOULE","ROLE":"Manages LME Pick ups and  arrangement"},{"CATEGORY":"SUPPORT","DESIGNATION":"Officer -Laboratory","ECODE":"E0297","EMAIL_ID":" virendray@thyrocare.com","IMG":null,"MOBILE":"9004616932","NAME":"MR VIRENDRA YADAV","ROLE":"Manages sample Logistic ,Consignment and WOE"},{"CATEGORY":"SUPPORT","DESIGNATION":"Executive -Network","ECODE":"E4579","EMAIL_ID":" harminder.singh@thyrocare.com","IMG":null,"MOBILE":"9870940975","NAME":"MR HARMINDER PARMAR","ROLE":"North Franchisee Support ,Queries related to credits ,debits and operations"},{"CATEGORY":"SUPPORT","DESIGNATION":"Sr Executive","ECODE":"E2959","EMAIL_ID":"poornima.sharma@thyrocare.com","IMG":null,"MOBILE":"9022940588","NAME":"MS POORNIMA SHARMA","ROLE":"To keep the network motivated, support them with their requirements."}]
     * response : Success
     */

    private String response;
    private ArrayList<ContactArrayListBean> Contact_Array_list;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<ContactArrayListBean> getContact_Array_list() {
        return Contact_Array_list;
    }

    public void setContact_Array_list(ArrayList<ContactArrayListBean> Contact_Array_list) {
        this.Contact_Array_list = Contact_Array_list;
    }

    public static class ContactArrayListBean {
        /**
         * CATEGORY : SUPPORT
         * DESIGNATION :
         * ECODE : E0365
         * EMAIL_ID : sureshp@thyrocare.com
         * IMG : null
         * MOBILE : 8451800524
         * NAME : MR PONVELLINGASURESH C
         * ROLE : Issues related to Consignment, WOE and Samples
         */

        private String CATEGORY;
        private String DESIGNATION;
        private String ECODE;
        private String EMAIL_ID;
        private Object IMG;
        private String MOBILE;
        private String NAME;
        private String ROLE;

        public String getCATEGORY() {
            return CATEGORY;
        }

        public void setCATEGORY(String CATEGORY) {
            this.CATEGORY = CATEGORY;
        }

        public String getDESIGNATION() {
            return DESIGNATION;
        }

        public void setDESIGNATION(String DESIGNATION) {
            this.DESIGNATION = DESIGNATION;
        }

        public String getECODE() {
            return ECODE;
        }

        public void setECODE(String ECODE) {
            this.ECODE = ECODE;
        }

        public String getEMAIL_ID() {
            return EMAIL_ID;
        }

        public void setEMAIL_ID(String EMAIL_ID) {
            this.EMAIL_ID = EMAIL_ID;
        }

        public Object getIMG() {
            return IMG;
        }

        public void setIMG(Object IMG) {
            this.IMG = IMG;
        }

        public String getMOBILE() {
            return MOBILE;
        }

        public void setMOBILE(String MOBILE) {
            this.MOBILE = MOBILE;
        }

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getROLE() {
            return ROLE;
        }

        public void setROLE(String ROLE) {
            this.ROLE = ROLE;
        }
    }
}
