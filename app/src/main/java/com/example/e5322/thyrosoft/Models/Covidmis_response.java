package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class Covidmis_response {


    /**
     * Output : [{"Aadhar":"http://www.charbi.com\\Net\\assets\\cov\\Adh20200504143453348.jpg","Ccc":"8921","Mobile":"8830913359","PatientName":"MR KRISHNA VAIRAGE","Prescription":"http://www.charbi.com\\Net\\assets\\cov\\Pre20200504143453363.jpg","Trf":"http://www.charbi.com\\Net\\assets\\cov\\TRF20200504143453348.jpg"},{"Aadhar":"http://www.charbi.com\\Net\\assets\\cov\\Adh20200504144730616.jpg","Ccc":"8001","Mobile":"7081726290","PatientName":"MR NILESH CHAUHAN","Prescription":"http://www.charbi.com\\Net\\assets\\cov\\Pre20200504144730647.jpg","Trf":"http://www.charbi.com\\Net\\assets\\cov\\TRF20200504144730600.jpg"}]
     * resId : RES0000
     * response : SUCCESS
     */

    private String resId;
    private String response;
    private List<OutputBean> Output;

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public List<OutputBean> getOutput() {
        return Output;
    }

    public void setOutput(List<OutputBean> Output) {
        this.Output = Output;
    }

    public static class OutputBean {
        /**
         * Aadhar : http://www.charbi.com\Net\assets\cov\Adh20200504143453348.jpg
         * Ccc : 8921
         * Mobile : 8830913359
         * PatientName : MR KRISHNA VAIRAGE
         * Prescription : http://www.charbi.com\Net\assets\cov\Pre20200504143453363.jpg
         * Trf : http://www.charbi.com\Net\assets\cov\TRF20200504143453348.jpg
         */

        private String Aadhar;
        private String Ccc;
        private String Mobile;
        private String PatientName;
        private String Prescription;
        private String Trf;
        private String Aadhar1;
        private String Trf1;

        public String getAadhar1() {
            return Aadhar1;
        }

        public String getTrf1() {
            return Trf1;
        }

        public String getAadhar() {
            return Aadhar;
        }

        public void setAadhar(String Aadhar) {
            this.Aadhar = Aadhar;
        }

        public String getCcc() {
            return Ccc;
        }

        public void setCcc(String Ccc) {
            this.Ccc = Ccc;
        }

        public String getMobile() {
            return Mobile;
        }

        public void setMobile(String Mobile) {
            this.Mobile = Mobile;
        }

        public String getPatientName() {
            return PatientName;
        }

        public void setPatientName(String PatientName) {
            this.PatientName = PatientName;
        }

        public String getPrescription() {
            return Prescription;
        }

        public void setPrescription(String Prescription) {
            this.Prescription = Prescription;
        }

        public String getTrf() {
            return Trf;
        }

        public void setTrf(String Trf) {
            this.Trf = Trf;
        }
    }
}
