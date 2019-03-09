package com.example.e5322.thyrosoft.Models.PincodeMOdel;

/**
 * Created by e4904@thyrocare.com on 30/5/18.
 */

public class SendSmspostRequestlModel {
    String apikey;
    String ToMobile;
    String message;
    String FromMobile;

    public String getApikey() {
        return apikey;
    }

    public void setApikey(String apikey) {
        this.apikey = apikey;
    }

    public String getToMobile() {
        return ToMobile;
    }

    public void setToMobile(String toMobile) {
        ToMobile = toMobile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFromMobile() {
        return FromMobile;
    }

    public void setFromMobile(String fromMobile) {
        FromMobile = fromMobile;
    }
}
