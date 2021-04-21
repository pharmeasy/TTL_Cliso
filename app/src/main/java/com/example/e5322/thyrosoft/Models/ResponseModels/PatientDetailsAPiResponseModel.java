package com.example.e5322.thyrosoft.Models.ResponseModels;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;

public class PatientDetailsAPiResponseModel implements Serializable {


    private String ResId;
    private String Response;
    private ArrayList<PatientInfoBean> patientInfo;

    public String getResId() {
        return ResId;
    }

    public void setResId(String ResId) {
        this.ResId = ResId;
    }

    public String getResponse() {
        return Response;
    }

    public void setResponse(String Response) {
        this.Response = Response;
    }

    public ArrayList<PatientInfoBean> getPatientInfo() {
        return patientInfo;
    }

    public void setPatientInfo(ArrayList<PatientInfoBean> patientInfo) {
        this.patientInfo = patientInfo;
    }

    public static class PatientInfoBean {


        private String Address;
        private String DOB;
        private String Email;
        private String FName;
        private String Gender;
        private String Id;
        private String LName;
        private String Name;
        private String PatientId;
        private String Pincode;

        public String getAddress() {
            return Address;
        }

        public void setAddress(String Address) {
            this.Address = Address;
        }

        public String getDOB() {
            return DOB;
        }

        public void setDOB(String DOB) {
            this.DOB = DOB;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String Email) {
            this.Email = Email;
        }

        public String getFName() {
            return FName;
        }

        public void setFName(String FName) {
            this.FName = FName;
        }

        public String getGender() {
            return Gender;
        }

        public void setGender(String Gender) {
            this.Gender = Gender;
        }

        public String getId() {
            return Id;
        }

        public void setId(String Id) {
            this.Id = Id;
        }

        public String getLName() {
            return LName;
        }

        public void setLName(String LName) {
            this.LName = LName;
        }

        public String getName() {
            return Name;
        }

        public void setName(String Name) {
            this.Name = Name;
        }

        public String getPatientId() {
            return PatientId;
        }

        public void setPatientId(String PatientId) {
            this.PatientId = PatientId;
        }

        public String getPincode() {
            return Pincode;
        }

        public void setPincode(String Pincode) {
            this.Pincode = Pincode;
        }

        @NonNull
        @Override
        public String toString() {
            return Name;
        }
    }
}
