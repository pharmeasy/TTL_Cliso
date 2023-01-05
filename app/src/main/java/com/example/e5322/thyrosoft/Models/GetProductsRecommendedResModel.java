package com.example.e5322.thyrosoft.Models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class GetProductsRecommendedResModel {


    @SerializedName("Response")
    private String response;
    @SerializedName("Res_ID")
    private String res_ID;
    @SerializedName("ProductList")
    private List<ProductListDTO> productList;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getRes_ID() {
        return res_ID;
    }

    public void setRes_ID(String res_ID) {
        this.res_ID = res_ID;
    }

    public List<ProductListDTO> getProductList() {
        return productList;
    }

    public void setProductList(List<ProductListDTO> productList) {
        this.productList = productList;
    }

    public static class ProductListDTO {
        @SerializedName("TestsAsked")
        private String testsAsked;
        @SerializedName("TestsRecommended")
        private String testsRecommended;
        @SerializedName("TestsRecoDisplayName")
        private String testsRecoDisplayName;
        @SerializedName("RecommendationMsg")
        private String recommendationMsg;
        @SerializedName("TestsPackageList")
        private ArrayList<String> testsPackageList;

        private String price;
        private String b2brate;
        private String b2cRate;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getB2brate() {
            return b2brate;
        }

        public void setB2brate(String b2brate) {
            this.b2brate = b2brate;
        }

        public String getB2cRate() {
            return b2cRate;
        }

        public void setB2cRate(String b2cRate) {
            this.b2cRate = b2cRate;
        }

        public String getTestsAsked() {
            return testsAsked;
        }

        public void setTestsAsked(String testsAsked) {
            this.testsAsked = testsAsked;
        }

        public String getTestsRecommended() {
            return testsRecommended;
        }

        public void setTestsRecommended(String testsRecommended) {
            this.testsRecommended = testsRecommended;
        }

        public String getTestsRecoDisplayName() {
            return testsRecoDisplayName;
        }

        public void setTestsRecoDisplayName(String testsRecoDisplayName) {
            this.testsRecoDisplayName = testsRecoDisplayName;
        }

        public String getRecommendationMsg() {
            return recommendationMsg;
        }

        public void setRecommendationMsg(String recommendationMsg) {
            this.recommendationMsg = recommendationMsg;
        }

        public ArrayList<String> getTestsPackageList() {
            return testsPackageList;
        }

        public void setTestsPackageList(ArrayList<String> testsPackageList) {
            this.testsPackageList = testsPackageList;
        }
    }
}