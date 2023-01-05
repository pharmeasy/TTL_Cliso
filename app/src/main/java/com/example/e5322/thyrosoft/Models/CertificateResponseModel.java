package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class CertificateResponseModel {


    /**
     * Certificatedtl : [{"category":"OLC","date":"31-12-2020","name":"MR GIRISH","source_code":"A3121","title":"SWAB COLLECTION TECHNICIAN TRAINING","url":"http://staging.charbi.com/training_certificate/NBT_Certificare_STech/index.html?=31-12-2020&=MR GIRISH (A3121)"}]
     * response : Success
     */

    private String response;
    private ArrayList<CertificatedtlDTO> Certificatedtl;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<CertificatedtlDTO> getCertificatedtl() {
        return Certificatedtl;
    }

    public void setCertificatedtl(ArrayList<CertificatedtlDTO> Certificatedtl) {
        this.Certificatedtl = Certificatedtl;
    }

    public static class CertificatedtlDTO {
        /**
         * category : OLC
         * date : 31-12-2020
         * name : MR GIRISH
         * source_code : A3121
         * title : SWAB COLLECTION TECHNICIAN TRAINING
         * url : http://staging.charbi.com/training_certificate/NBT_Certificare_STech/index.html?=31-12-2020&=MR GIRISH (A3121)
         */

        private String category;
        private String date;
        private String name;
        private String source_code;
        private String title;
        private String url;

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSource_code() {
            return source_code;
        }

        public void setSource_code(String source_code) {
            this.source_code = source_code;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }
}
