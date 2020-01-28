package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class InsertreasonResponse {


    /**
     * red_id : RES0001
     * response : SUCCESS
     * rodetails : [{"SDATE":"1/18/2020 4:56:58 PM","remarks":"Testing entry","rowaste":"4","source":"TAM03"}]
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
         * SDATE : 1/18/2020 4:56:58 PM
         * remarks : Testing entry
         * rowaste : 4
         * source : TAM03
         */

        private String SDATE;
        private String remarks;
        private String rowaste;
        private String source;

        public String getSDATE() {
            return SDATE;
        }

        public void setSDATE(String SDATE) {
            this.SDATE = SDATE;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getRowaste() {
            return rowaste;
        }

        public void setRowaste(String rowaste) {
            this.rowaste = rowaste;
        }

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }
    }
}
