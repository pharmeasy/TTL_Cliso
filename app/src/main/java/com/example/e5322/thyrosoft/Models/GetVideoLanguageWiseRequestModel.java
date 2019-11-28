package com.example.e5322.thyrosoft.Models;

import java.io.Serializable;

public class GetVideoLanguageWiseRequestModel implements Serializable {

    String App;
    String Language;

    public String getApp() {
        return App;
    }

    public void setApp(String app) {
        App = app;
    }

    public String getLanguage() {
        return Language;
    }

    public void setLanguage(String language) {
        Language = language;
    }
}
