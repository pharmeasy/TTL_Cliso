package com.example.e5322.thyrosoft.Models;

public class Tokenresponse {


    /**
     * RespId : RES0000
     * Response : SUCCESS
     * Token : j1hh8wytWxurg+ocKY1ZihADgxWZq5ynb5rMO+fAjdU=
     */

    private String RespId;
    private String Response;
    private String Token;
    private String RequestId;

    public String getRequestId() {
        return RequestId;
    }

    public void setRequestId(String requestId) {
        RequestId = requestId;
    }

    public String getRespId() {
        return RespId;
    }

    public void setRespId(String RespId) {
        this.RespId = RespId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String Token) {
        this.Token = Token;
    }
}
