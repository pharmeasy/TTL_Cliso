package com.example.e5322.thyrosoft.Models;

import java.util.List;

public class LeadPurposeResponseModel {


    /**
     * response : Success
     * respId : RES00001
     * purposeList : [{"data":"Orders","remarks":"You get 30%. T&C apply"},{"data":"TSP","remarks":"You get Rs. 40,000. T&C apply"},{"data":"HVC","remarks":"You get Rs. 5000. T&C apply"},{"data":"HR","remarks":"You get Rs. 5000. T&C apply."},{"data":"NHL","remarks":""}]
     */

    private String response;
    private String respId;
    private List<PurposeListDTO> purposeList;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRespId() {
        return respId;
    }

    public void setRespId(String respId) {
        this.respId = respId;
    }

    public List<PurposeListDTO> getPurposeList() {
        return purposeList;
    }

    public void setPurposeList(List<PurposeListDTO> purposeList) {
        this.purposeList = purposeList;
    }

    public static class PurposeListDTO {
        /**
         * data : Orders
         * remarks : You get 30%. T&C apply
         */

        private String data;
        private String remarks;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }
    }
}
