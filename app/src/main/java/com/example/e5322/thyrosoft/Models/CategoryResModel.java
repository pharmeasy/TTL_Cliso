package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

/*{"RespID": "RES0000","Resp": "SUCCESS","CategoryList": [{"Category": "HOSPITAL"},{"Category": "LAB" },{"Category": "DOCTOR"}]}*/

public class CategoryResModel {

    private String RespID;
    private String Resp;
    private ArrayList<CategoryList> CategoryList;

    public String getRespID() {
        return RespID;
    }

    public void setRespID(String respID) {
        this.RespID = respID;
    }

    public String getResp() {
        return Resp;
    }

    public void setResp(String resp) {
        this.Resp = resp;
    }

    public ArrayList<CategoryList> getCategoryList() {
        return CategoryList;
    }

    public void setCategoryList(ArrayList<CategoryList> categoryList) {
        this.CategoryList = categoryList;
    }

    public static class CategoryList {

         String Category;

        public String getCategory() {
            return Category;
        }

        public void setCategory(String category) {
            Category = category;
        }
    }
}
