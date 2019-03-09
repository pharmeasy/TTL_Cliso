package com.example.e5322.thyrosoft.Models.PincodeMOdel;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class FilterPostRequestlModel {
    String apikey;
    String SearchBy;
    String SearchValue;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getSearchBy() {
        return SearchBy;
    }

    public void setSearchBy(String searchBy) {
        SearchBy = searchBy;
    }

    public String getSearchValue() {
        return SearchValue;
    }

    public void setSearchValue(String searchValue) {
        SearchValue = searchValue;
    }
}
