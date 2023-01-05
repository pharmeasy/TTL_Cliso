package com.example.e5322.thyrosoft.Models;

import java.util.ArrayList;

public class GetTestCodeResponseModel {




    private String Response;
    private String ResID;
    private ArrayList<String> TestCodeMappinpList;

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public String getResID() {
        return ResID;
    }

    public void setResID(String ResID) {
        this.ResID = ResID;
    }

    public ArrayList<String> getTestCodeMappinpList() {
        return TestCodeMappinpList;
    }

    public void setTestCodeMappinpList(ArrayList<String> TestCodeMappinpList) {
        this.TestCodeMappinpList = TestCodeMappinpList;
    }
}
