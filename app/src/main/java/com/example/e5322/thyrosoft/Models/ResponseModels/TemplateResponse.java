package com.example.e5322.thyrosoft.Models.ResponseModels;

import java.util.List;

public class TemplateResponse {


    /**
     * response : Success
     * resId : RES0000
     * Handbill : [{"url":"http://crm.thyrocare.local/Images/Testimages/handbill_bkg-blu.jpg"},{"url":"http://crm.thyrocare.local/Images/Testimages/handbill_iis.png"}]
     */

    private String response;
    private String resId;
    private List<HandbillBean> Handbill;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getResId() {
        return resId;
    }

    public void setResId(String resId) {
        this.resId = resId;
    }

    public List<HandbillBean> getHandbill() {
        return Handbill;
    }

    public void setHandbill(List<HandbillBean> Handbill) {
        this.Handbill = Handbill;
    }

    public static class HandbillBean {
        /**
         * url : http://crm.thyrocare.local/Images/Testimages/handbill_bkg-blu.jpg
         */

        private String url;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
