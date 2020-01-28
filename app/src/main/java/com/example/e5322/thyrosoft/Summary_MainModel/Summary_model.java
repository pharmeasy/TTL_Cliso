package com.example.e5322.thyrosoft.Summary_MainModel;

import android.os.Parcel;
import android.os.Parcelable;

public class Summary_model implements Parcelable {
    public String response;

    public String error;

    public String RES_ID;

    public Summary_model() {
    }

    public Woeditlist woeditlist;

    protected Summary_model(Parcel in) {
        response = in.readString();
        error = in.readString();
        RES_ID = in.readString();
    }

    public static final Creator<Summary_model> CREATOR = new Creator<Summary_model>() {
        @Override
        public Summary_model createFromParcel(Parcel in) {
            return new Summary_model(in);
        }

        @Override
        public Summary_model[] newArray(int size) {
            return new Summary_model[size];
        }
    };

    public String getResponse ()
    {
        return response;
    }

    public void setResponse (String response)
    {
        this.response = response;
    }

    public String getError ()
    {
        return error;
    }

    public void setError (String error)
    {
        this.error = error;
    }

    public String getRES_ID ()
    {
        return RES_ID;
    }

    public void setRES_ID (String RES_ID)
    {
        this.RES_ID = RES_ID;
    }

    public Woeditlist getWoeditlist ()
    {
        return woeditlist;
    }

    public void setWoeditlist (Woeditlist woeditlist)
    {
        this.woeditlist = woeditlist;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [response = "+response+", error = "+error+", RES_ID = "+RES_ID+", woeditlist = "+woeditlist+"]";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(response);
        dest.writeString(error);
        dest.writeString(RES_ID);
    }
}
