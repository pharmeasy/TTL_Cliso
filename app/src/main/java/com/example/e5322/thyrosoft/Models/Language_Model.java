package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class Language_Model {


    /**
     * Output : [{"IID_NEW":"50","LANGUAGE":"Hindi"},{"IID_NEW":"56","LANGUAGE":"Telugu"},{"IID_NEW":"80","LANGUAGE":"English"}]
     * resId : RSS0000
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
         * IID_NEW : 50
         * LANGUAGE : Hindi
         */

        private String IID_NEW;
        private String LANGUAGE;

        public String getIID_NEW() {
            return IID_NEW;
        }

        public void setIID_NEW(String IID_NEW) {
            this.IID_NEW = IID_NEW;
        }

        public String getLANGUAGE() {
            return LANGUAGE;
        }

        public void setLANGUAGE(String LANGUAGE) {
            this.LANGUAGE = LANGUAGE;
        }
    }
}
