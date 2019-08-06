package com.example.e5322.thyrosoft.Models;

import java.io.Serializable;

public class HealthTipsApiResponseModel {
    HArt[] HArt;
    String RESPONSE;
    String RES_ID;

    public HealthTipsApiResponseModel.HArt[] getHArt() {
        return HArt;
    }

    public void setHArt(HealthTipsApiResponseModel.HArt[] HArt) {
        this.HArt = HArt;
    }

    public String getRESPONSE() {
        return RESPONSE;
    }

    public void setRESPONSE(String RESPONSE) {
        this.RESPONSE = RESPONSE;
    }

    public String getRES_ID() {
        return RES_ID;
    }

    public void setRES_ID(String RES_ID) {
        this.RES_ID = RES_ID;
    }

    public static class HArt implements Serializable {

        String Artical_Name;
        String Artical_link;
        String Imageurl;
        String Teaser;

        public String getArtical_Name() {
            return Artical_Name;
        }

        public void setArtical_Name(String artical_Name) {
            Artical_Name = artical_Name;
        }

        public String getArtical_link() {
            return Artical_link;
        }

        public void setArtical_link(String artical_link) {
            Artical_link = artical_link;
        }

        public String getImageurl() {
            return Imageurl;
        }

        public void setImageurl(String imageurl) {
            Imageurl = imageurl;
        }

        public String getTeaser() {
            return Teaser;
        }

        public void setTeaser(String teaser) {
            Teaser = teaser;
        }
    }
}
