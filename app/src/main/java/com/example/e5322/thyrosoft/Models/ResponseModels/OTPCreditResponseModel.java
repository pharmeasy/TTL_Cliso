package com.example.e5322.thyrosoft.Models.ResponseModels;

import java.util.ArrayList;

public class OTPCreditResponseModel {

    /**
     * Output : [{"CREATETIME":"1/18/2020 12:11:02 PM","CREDIT":"YES","MOBILE":"9096455578","SENTTIME":"1/18/2020 12:11:20 PM","VERIFIED":"YES"},{"CREATETIME":"1/18/2020 12:08:06 PM","CREDIT":"YES","MOBILE":"7744847375","SENTTIME":"1/18/2020 12:08:25 PM","VERIFIED":"YES"},{"CREATETIME":"1/18/2020 12:04:56 PM","CREDIT":"YES","MOBILE":"7020602182","SENTTIME":"1/18/2020 12:05:36 PM","VERIFIED":"YES"},{"CREATETIME":"1/16/2020 5:21:51 PM","CREDIT":"YES","MOBILE":"7020602182","SENTTIME":"1/16/2020 5:22:15 PM","VERIFIED":"YES"}]
     * resId : RES0000
     * response : SUCCESS
     */

    private String resId;
    private String response;
    private ArrayList<OutputBean> Output;

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

    public ArrayList<OutputBean> getOutput() {
        return Output;
    }

    public void setOutput(ArrayList<OutputBean> Output) {
        this.Output = Output;
    }

    public static class OutputBean {
        /**
         * CREATETIME : 1/18/2020 12:11:02 PM
         * CREDIT : YES
         * MOBILE : 9096455578
         * SENTTIME : 1/18/2020 12:11:20 PM
         * VERIFIED : YES
         */

        private String CREATETIME;
        private String CREDIT;
        private String MOBILE;
        private String SENTTIME;
        private String VERIFIED;

        public String getCREATETIME() {
            return CREATETIME;
        }

        public void setCREATETIME(String CREATETIME) {
            this.CREATETIME = CREATETIME;
        }

        public String getCREDIT() {
            return CREDIT;
        }

        public void setCREDIT(String CREDIT) {
            this.CREDIT = CREDIT;
        }

        public String getMOBILE() {
            return MOBILE;
        }

        public void setMOBILE(String MOBILE) {
            this.MOBILE = MOBILE;
        }

        public String getSENTTIME() {
            return SENTTIME;
        }

        public void setSENTTIME(String SENTTIME) {
            this.SENTTIME = SENTTIME;
        }

        public String getVERIFIED() {
            return VERIFIED;
        }

        public void setVERIFIED(String VERIFIED) {
            this.VERIFIED = VERIFIED;
        }
    }
}
