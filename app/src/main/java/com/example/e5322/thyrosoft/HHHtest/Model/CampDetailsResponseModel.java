package com.example.e5322.thyrosoft.HHHtest.Model;

import java.util.List;

public class CampDetailsResponseModel {


    /**
     * camp : [{"amountCollected":"2500","campAddress":"HILTON MUMBAI INTERNATIONAL AIRPORT Sahar Airport Road, Andheri East, Mumbai, 400099,","campID":"CMP000824","campName":null,"campPincode":"400099","numberOfCollection":"8","refDrEmail":"medicaldirector@globusmedicare.com","refDrNumber":"9632965038","refDrname":"DR SAMEER PARATE","refHospital":"Globus Medicare","scfType":null,"sourceCode":"COV01"},{"amountCollected":"2500","campAddress":"Globus Medicare, Shop no. 3 - 8, Shubham Center II, Cardinal Gracias Road, Landmark - Near P&G Plaza","campID":"CMP000825","campName":null,"campPincode":"400099","numberOfCollection":"10","refDrEmail":"medicaldirector@globusmedicare.com","refDrNumber":"9632965038","refDrname":"DR SAMEER PARATE","refHospital":"Globus Medicare","scfType":null,"sourceCode":"COV01"},{"amountCollected":"2500","campAddress":"Hotel Pacific Residency,CTS No. 82/1, Village, RK Mandir Rd, Kondivita, Andheri East, Mumbai, Mahara","campID":"CMP000826","campName":null,"campPincode":"400059","numberOfCollection":"4","refDrEmail":"medicaldirector@globusmedicare.com","refDrNumber":"9870688193","refDrname":"DR SAMEER PARATE","refHospital":"Globus Medicare","scfType":null,"sourceCode":"COV01"},{"amountCollected":"2500","campAddress":"Prabhat bhavan kapad bazar 1st floor opp dilshad hotel Mahim ","campID":"CMP000833","campName":null,"campPincode":"400055","numberOfCollection":"18","refDrEmail":"mohgn.phd@mcgm.gov.in","refDrNumber":"9372017587","refDrname":"Dr Pinki","refHospital":"Prabhat Nursing Home (BMC G/ North Ward)","scfType":null,"sourceCode":"COV01"}]
     * response : Success
     * responseID : RES000
     */

    private String response;
    private String responseID;
    private List<CampBean> camp;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResponseID() {
        return responseID;
    }

    public void setResponseID(String responseID) {
        this.responseID = responseID;
    }

    public List<CampBean> getCamp() {
        return camp;
    }

    public void setCamp(List<CampBean> camp) {
        this.camp = camp;
    }

    public static class CampBean {
        /**
         * amountCollected : 2500
         * campAddress : HILTON MUMBAI INTERNATIONAL AIRPORT Sahar Airport Road, Andheri East, Mumbai, 400099,
         * campID : CMP000824
         * campName : null
         * campPincode : 400099
         * numberOfCollection : 8
         * refDrEmail : medicaldirector@globusmedicare.com
         * refDrNumber : 9632965038
         * refDrname : DR SAMEER PARATE
         * refHospital : Globus Medicare
         * scfType : null
         * sourceCode : COV01
         */

        private String amountCollected;
        private String campAddress;
        private String campID;
        private Object campName;
        private String campPincode;
        private String numberOfCollection;
        private String refDrEmail;
        private String refDrNumber;
        private String refDrname;
        private String refHospital;
        private Object scfType;
        private String sourceCode;

        public String getAmountCollected() {
            return amountCollected;
        }

        public void setAmountCollected(String amountCollected) {
            this.amountCollected = amountCollected;
        }

        public String getCampAddress() {
            return campAddress;
        }

        public void setCampAddress(String campAddress) {
            this.campAddress = campAddress;
        }

        public String getCampID() {
            return campID;
        }

        public void setCampID(String campID) {
            this.campID = campID;
        }

        public Object getCampName() {
            return campName;
        }

        public void setCampName(Object campName) {
            this.campName = campName;
        }

        public String getCampPincode() {
            return campPincode;
        }

        public void setCampPincode(String campPincode) {
            this.campPincode = campPincode;
        }

        public String getNumberOfCollection() {
            return numberOfCollection;
        }

        public void setNumberOfCollection(String numberOfCollection) {
            this.numberOfCollection = numberOfCollection;
        }

        public String getRefDrEmail() {
            return refDrEmail;
        }

        public void setRefDrEmail(String refDrEmail) {
            this.refDrEmail = refDrEmail;
        }

        public String getRefDrNumber() {
            return refDrNumber;
        }

        public void setRefDrNumber(String refDrNumber) {
            this.refDrNumber = refDrNumber;
        }

        public String getRefDrname() {
            return refDrname;
        }

        public void setRefDrname(String refDrname) {
            this.refDrname = refDrname;
        }

        public String getRefHospital() {
            return refHospital;
        }

        public void setRefHospital(String refHospital) {
            this.refHospital = refHospital;
        }

        public Object getScfType() {
            return scfType;
        }

        public void setScfType(Object scfType) {
            this.scfType = scfType;
        }

        public String getSourceCode() {
            return sourceCode;
        }

        public void setSourceCode(String sourceCode) {
            this.sourceCode = sourceCode;
        }
    }
}
