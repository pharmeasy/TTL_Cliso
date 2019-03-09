package com.example.e5322.thyrosoft.Models.PincodeMOdel;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class MergePostRequestModel {
    String apikey;
    String category;
    String SelectedId;
    String OtherId;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSelectedId() {
        return SelectedId;
    }

    public void setSelectedId(String selectedId) {
        SelectedId = selectedId;
    }

    public String getOtherId() {
        return OtherId;
    }

    public void setOtherId(String otherId) {
        OtherId = otherId;
    }
}
