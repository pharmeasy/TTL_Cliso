package com.example.e5322.thyrosoft.Models;

public class IsnhlmasterDTO {

    /**
     * isSelected : false
     * isShowpopup : true
     * name : TTL
     * url :
     */

    private Boolean isSelected;
    private Boolean isShowpopup;
    private String name;
    private String url;

    public Boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Boolean isIsShowpopup() {
        return isShowpopup;
    }

    public void setIsShowpopup(Boolean isShowpopup) {
        this.isShowpopup = isShowpopup;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }



    @Override
    public String toString() {
        return name;
    }
}
