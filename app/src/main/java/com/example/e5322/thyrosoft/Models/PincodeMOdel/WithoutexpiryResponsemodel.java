package com.example.e5322.thyrosoft.Models.PincodeMOdel;

/**
 * Created by e4904@thyrocare.com on 3/7/18.
 */

public class WithoutexpiryResponsemodel {

    private String Response;
    private String message;
    private String ResId;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getResId() {
        return ResId;
    }

    public void setResId(String resId) {
        ResId = resId;
    }
}
