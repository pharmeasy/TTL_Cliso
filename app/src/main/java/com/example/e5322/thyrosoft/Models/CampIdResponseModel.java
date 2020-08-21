package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;
import java.util.List;

public class CampIdResponseModel {


    /**
     * Output : [{"AmountToBeCollected":"0","CampID":"CMP000063","CampLocationAddress":"Concord Galaxy, Below Marol Metro Station, Near Fire Bridge, Andheri - Kurla Rd, Mumbai, Maharashtra","CampType":"COVID","Pincode":"400059","ReferringDoctorName":"Dr Subhashis ","ReferringDoctorNumber":"9647100657","ReferringDrEmail":"subhashisbsmc@gmail.com","ReferringHospital":"Self","SourceCode":"CO343"},{"AmountToBeCollected":"0","CampID":"CMP000064","CampLocationAddress":"Hotel Blue Executive, JB Metal Building, Ansa Industrial Estate, Chandivali, Andheri East, Mumbai","CampType":"COVID","Pincode":"400072","ReferringDoctorName":"Dr Subhashis ","ReferringDoctorNumber":"9647100657","ReferringDrEmail":"subhashisbsmc@gmail.com","ReferringHospital":"Self","SourceCode":"CO343"},{"AmountToBeCollected":"0","CampID":"CMP000065","CampLocationAddress":"Collection O Celesta, Collection O Bamandaya Pada, Raje Shivaji Nagar, Marol, Andheri East, Mumbai","CampType":"COVID","Pincode":"400072","ReferringDoctorName":"Dr Subhashis ","ReferringDoctorNumber":"9647100657","ReferringDrEmail":"subhashisbsmc@gmail.com","ReferringHospital":"Self","SourceCode":"CO343"},{"AmountToBeCollected":"0","CampID":"CMP000066","CampLocationAddress":"Hotel Blue Sapphire Residency, 1, Telephone Exchange Rd, Saki Naka, Mumbai, Maharashtra","CampType":"COVID","Pincode":"400072","ReferringDoctorName":"Dr Subhashis ","ReferringDoctorNumber":"9647100657","ReferringDrEmail":"subhashisbsmc@gmail.com","ReferringHospital":"Self","SourceCode":"CO343"},{"AmountToBeCollected":"0","CampID":"CMP000069","CampLocationAddress":"OYO Townhouse 339 Worli, OTH Townhouse,Dr E Moses Rd, Gandhi Nagar, Upper Worli, Mahalakshmi, Mumbai","CampType":"COVID","Pincode":"400018","ReferringDoctorName":"Dr Subhashis ","ReferringDoctorNumber":"9647100657","ReferringDrEmail":"subhashisbsmc@gmail.com","ReferringHospital":"Self","SourceCode":"CO343"}]
     * response : SUCCESS
     * responseID : RSS0000
     */

    private String response;
    private String responseID;
    private String SampleType;
    private String Test;
    private ArrayList<OutputBean> Output;

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

    public ArrayList<OutputBean> getOutput() {
        return Output;
    }

    public void setOutput(ArrayList<OutputBean> Output) {
        this.Output = Output;
    }

    public String getSampleType() {
        return SampleType;
    }

    public void setSampleType(String sampleType) {
        SampleType = sampleType;
    }

    public String getTest() {
        return Test;
    }

    public void setTest(String test) {
        Test = test;
    }

    public static class OutputBean {
        /**
         * AmountToBeCollected : 0
         * CampID : CMP000063
         * CampLocationAddress : Concord Galaxy, Below Marol Metro Station, Near Fire Bridge, Andheri - Kurla Rd, Mumbai, Maharashtra
         * CampType : COVID
         * Pincode : 400059
         * ReferringDoctorName : Dr Subhashis
         * ReferringDoctorNumber : 9647100657
         * ReferringDrEmail : subhashisbsmc@gmail.com
         * ReferringHospital : Self
         * SourceCode : CO343
         */

        private String AmountToBeCollected;
        private String CampID;
        private String CampLocationAddress;
        private String CampType;
        private String Pincode;
        private String ReferringDoctorName;
        private String ReferringDoctorNumber;
        private String ReferringDrEmail;
        private String ReferringHospital;
        private String SourceCode;

        public String getAmountToBeCollected() {
            return AmountToBeCollected;
        }

        public void setAmountToBeCollected(String AmountToBeCollected) {
            this.AmountToBeCollected = AmountToBeCollected;
        }

        public String getCampID() {
            return CampID;
        }

        public void setCampID(String CampID) {
            this.CampID = CampID;
        }

        public String getCampLocationAddress() {
            return CampLocationAddress;
        }

        public void setCampLocationAddress(String CampLocationAddress) {
            this.CampLocationAddress = CampLocationAddress;
        }

        public String getCampType() {
            return CampType;
        }

        public void setCampType(String CampType) {
            this.CampType = CampType;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String Pincode) {
            this.Pincode = Pincode;
        }

        public String getReferringDoctorName() {
            return ReferringDoctorName;
        }

        public void setReferringDoctorName(String ReferringDoctorName) {
            this.ReferringDoctorName = ReferringDoctorName;
        }

        public String getReferringDoctorNumber() {
            return ReferringDoctorNumber;
        }

        public void setReferringDoctorNumber(String ReferringDoctorNumber) {
            this.ReferringDoctorNumber = ReferringDoctorNumber;
        }

        public String getReferringDrEmail() {
            return ReferringDrEmail;
        }

        public void setReferringDrEmail(String ReferringDrEmail) {
            this.ReferringDrEmail = ReferringDrEmail;
        }

        public String getReferringHospital() {
            return ReferringHospital;
        }

        public void setReferringHospital(String ReferringHospital) {
            this.ReferringHospital = ReferringHospital;
        }

        public String getSourceCode() {
            return SourceCode;
        }

        public void setSourceCode(String SourceCode) {
            this.SourceCode = SourceCode;
        }
    }
}
