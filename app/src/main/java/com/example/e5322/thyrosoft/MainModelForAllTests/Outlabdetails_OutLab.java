package com.example.e5322.thyrosoft.MainModelForAllTests;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by E5322 on 07-06-2018.
 */

public class Outlabdetails_OutLab implements Parcelable {
    String code, name, product, type, trf;

    Rate_OutLab rate;
    Sampletype_OutLab[] sampletype;


    Outlabdetails_OutLab(Parcel in) {
        code = in.readString();
        name = in.readString();
        product = in.readString();
        type = in.readString();
        trf = in.readString();
        rate = in.readParcelable(Rate_OutLab.class.getClassLoader());
        sampletype = in.createTypedArray(Sampletype_OutLab.CREATOR);
    }

    public static final Creator<Outlabdetails_OutLab> CREATOR = new Creator<Outlabdetails_OutLab>() {
        @Override
        public Outlabdetails_OutLab createFromParcel(Parcel in) {
            return new Outlabdetails_OutLab(in);
        }

        @Override
        public Outlabdetails_OutLab[] newArray(int size) {
            return new Outlabdetails_OutLab[size];
        }
    };

    public Outlabdetails_OutLab() {
    }

    public String getType() {
        return type;
    }

    public String getTrf() {
        return trf;
    }

    public void setTrf(String trf) {
        this.trf = trf;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Rate_OutLab getRate() {
        return rate;
    }

    public void setRate(Rate_OutLab rate) {
        this.rate = rate;
    }

    public Sampletype_OutLab[] getSampletype() {
        return sampletype;
    }

    public void setSampletype(Sampletype_OutLab[] sampletype) {
        this.sampletype = sampletype;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(product);
        dest.writeString(type);
        dest.writeString(trf);
        dest.writeParcelable(rate, flags);
        dest.writeTypedArray(sampletype, flags);
    }

    public static class Rate_OutLab implements Parcelable {
        String b2b, b2c;

        public Rate_OutLab() {
        }

        protected Rate_OutLab(Parcel in) {
            b2b = in.readString();
            b2c = in.readString();
        }

        public static final Creator<Rate_OutLab> CREATOR = new Creator<Rate_OutLab>() {
            @Override
            public Rate_OutLab createFromParcel(Parcel in) {
                return new Rate_OutLab(in);
            }

            @Override
            public Rate_OutLab[] newArray(int size) {
                return new Rate_OutLab[size];
            }
        };

        public String getB2c() {
            return b2c;
        }

        public void setB2c(String b2c) {
            this.b2c = b2c;
        }

        public String getB2b() {

            return b2b;
        }

        public void setB2b(String b2b) {
            this.b2b = b2b;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(b2b);
            dest.writeString(b2c);
        }
    }

    public static class Sampletype_OutLab implements Parcelable {
        String outlabsampletype;

        public Sampletype_OutLab() {
        }

        protected Sampletype_OutLab(Parcel in) {
            outlabsampletype = in.readString();
        }

        public static final Creator<Sampletype_OutLab> CREATOR = new Creator<Sampletype_OutLab>() {
            @Override
            public Sampletype_OutLab createFromParcel(Parcel in) {
                return new Sampletype_OutLab(in);
            }

            @Override
            public Sampletype_OutLab[] newArray(int size) {
                return new Sampletype_OutLab[size];
            }
        };

        public String getOutlabsampletype() {
            return outlabsampletype;
        }

        public void setOutlabsampletype(String outlabsampletype) {
            this.outlabsampletype = outlabsampletype;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(outlabsampletype);
        }
    }

    @Override
    public String toString() {
        return
                ", rate=" + rate +
                        ", sampletype=" + Arrays.toString(sampletype)
                ;
    }
}
