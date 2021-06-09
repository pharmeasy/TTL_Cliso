package com.example.e5322.thyrosoft.Models.ResponseModels;

import java.util.ArrayList;

public class getSCPTechnicianModel {
    ArrayList<getTechnicianlist> Technicianlist;
    String Response, ResId;

    public getSCPTechnicianModel() {
    }

    public ArrayList<getTechnicianlist> getTechnicianlist() {
        return Technicianlist;
    }

    public void setTechnicianlist(ArrayList<getTechnicianlist> technicianlist) {
        Technicianlist = technicianlist;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String response) {
        Response = response;
    }

    public String getResId() {
        return ResId;
    }

    public void setResId(String resId) {
        ResId = resId;
    }

    public class getTechnicianlist {
        String NAME, NED_NUMBER;

        public String getNAME() {
            return NAME;
        }

        public void setNAME(String NAME) {
            this.NAME = NAME;
        }

        public String getNED_NUMBER() {
            return NED_NUMBER;
        }

        public void setNED_NUMBER(String NED_NUMBER) {
            this.NED_NUMBER = NED_NUMBER;
        }
    }
}
