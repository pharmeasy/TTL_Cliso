package com.example.e5322.thyrosoft.Models;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

@SuppressLint("ParcelCreator")
public class Camp_Intimatgion_List_Model implements Parcelable {
    String sr_no;
    String camp_date;
    String camp_profile;
    String camp_rate;
    String camp_days;
    String camp_address;
    String camp_duration;
    String camp_remark;

    public String getCamp_remark() {
        return camp_remark;
    }

    public void setCamp_remark(String camp_remark) {
        this.camp_remark = camp_remark;
    }



    public String getSample_per_day() {
        return sample_per_day;
    }

    public void setSample_per_day(String sample_per_day) {
        this.sample_per_day = sample_per_day;
    }

    String sample_per_day;

    public String getCamp_rate() {
        return camp_rate;
    }

    public void setCamp_rate(String camp_rate) {
        this.camp_rate = camp_rate;
    }

    public String getCamp_days() {
        return camp_days;
    }

    public void setCamp_days(String camp_days) {
        this.camp_days = camp_days;
    }

    public String getCamp_address() {
        return camp_address;
    }

    public void setCamp_address(String camp_address) {
        this.camp_address = camp_address;
    }

    public String getCamp_duration() {
        return camp_duration;
    }

    public void setCamp_duration(String camp_duration) {
        this.camp_duration = camp_duration;
    }

    public String getCamp_date() {
        return camp_date;
    }

    public void setCamp_date(String camp_date) {
        this.camp_date = camp_date;
    }

    public String getCamp_profile() {
        return camp_profile;
    }

    public void setCamp_profile(String camp_profile) {
        this.camp_profile = camp_profile;
    }

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
