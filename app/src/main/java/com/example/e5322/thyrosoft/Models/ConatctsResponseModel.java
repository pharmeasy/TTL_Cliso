package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class ConatctsResponseModel {


    /**
     * Contact_Array_list : [{"CATEGORY":"NUECLEAR","DESIGNATION":"CONSULTANT","ECODE":"C0121","EMAIL_ID":"sachin.patil@nueclear.com","IMG":null,"MOBILE":"9967773773","NAME":"DRSACHINPATIL","ROLE":""},{"CATEGORY":"NUECLEAR","DESIGNATION":"SREXECUTIVE","ECODE":"E1206","EMAIL_ID":"mahesh.kumar@thyrocare.com","IMG":null,"MOBILE":"7738159210","NAME":"MRMAHESHJKUMAR","ROLE":""},{"CATEGORY":"NUECLEAR","DESIGNATION":"EXECUTIVE","ECODE":"0517E","EMAIL_ID":"asad.kazi@thyrocare.com","IMG":null,"MOBILE":"7039082601","NAME":"MRASADKAZI","ROLE":""}]
     * response : Success
     */

    private String response;
    private ArrayList<ContactArrayListDTO> Contact_Array_list;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<ContactArrayListDTO> getContact_Array_list() {
        return Contact_Array_list;
    }

    public void setContact_Array_list(ArrayList<ContactArrayListDTO> Contact_Array_list) {
        this.Contact_Array_list = Contact_Array_list;
    }

    public static class ContactArrayListDTO {
        /**
         * CATEGORY : NUECLEAR
         * DESIGNATION : CONSULTANT
         * ECODE : C0121
         * EMAIL_ID : sachin.patil@nueclear.com
         * IMG : null
         * MOBILE : 9967773773
         * NAME : DRSACHINPATIL
         * ROLE :
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
