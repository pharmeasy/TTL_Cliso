package com.example.e5322.thyrosoft.Models.ResponseModels;

import java.util.ArrayList;

public class FAQTypesResponseModel {
    private String response;
    private ArrayList<String> FAQ_type_spinnner_names;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public ArrayList<String> getFAQ_type_spinnner_names() {
        return FAQ_type_spinnner_names;
    }

    public void setFAQ_type_spinnner_names(ArrayList<String> FAQ_type_spinnner_names) {
        this.FAQ_type_spinnner_names = FAQ_type_spinnner_names;
    }
}
