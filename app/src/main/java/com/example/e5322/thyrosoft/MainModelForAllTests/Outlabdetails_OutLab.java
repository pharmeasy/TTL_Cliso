package com.example.e5322.thyrosoft.MainModelForAllTests;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Created by E5322 on 07-06-2018.
 */

public class Outlabdetails_OutLab implements Parcelable {
    String code, name,isCPL, product, type, trf;
    int isNHL;

    Rate_OutLab rate;
    Sampletype_OutLab[] sampletype;



    public Outlabdetails_OutLab() {
    }

    protected Outlabdetails_OutLab(Parcel in) {
        code = in.readString();
        name = in.readString();
        isCPL = in.readString();
        product = in.readString();
        type = in.readString();
        trf = in.readString();
        isNHL = in.readInt();
        rate = in.readParcelable(Rate_OutLab.class.getClassLoader());
        sampletype = in.createTypedArray(Sampletype_OutLab.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(code);
        dest.writeString(name);
        dest.writeString(isCPL);
        dest.writeString(product);
        dest.writeString(type);
        dest.writeString(trf);
        dest.writeInt(isNHL);
        dest.writeParcelable(rate, flags);
        dest.writeTypedArray(sampletype, flags);
    }

    @Override
    public int describeContents() {
        return 0;
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

    public int getIsNHL() {
        return isNHL;
    }

    public void setIsNHL(int isNHL) {
        this.isNHL = isNHL;
    }

    public String getIsCPL() {
        return isCPL;
    }

    public void setIsCPL(String isCPL) {
        this.isCPL = isCPL;
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




    public static class Rate_OutLab implements Parcelable {
        String b2b, b2c,cplr,rplr,NHLRate;

        public Rate_OutLab() {
        }


        protected Rate_OutLab(Parcel in) {
            b2b = in.readString();
            b2c = in.readString();
            cplr = in.readString();
            rplr = in.readString();
            NHLRate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(b2b);
            dest.writeString(b2c);
            dest.writeString(cplr);
            dest.writeString(rplr);
            dest.writeString(NHLRate);
        }

        @Override
        public int describeContents() {
            return 0;
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

        public String getNHLRate() {
            return NHLRate;
        }

        public void setNHLRate(String NHLRate) {
            this.NHLRate = NHLRate;
        }

        public String getCplr() {
            return cplr;
        }

        public void setCplr(String cplr) {
            this.cplr = cplr;
        }

        public String getRplr() {
            return rplr;
        }

        public void setRplr(String rplr) {
            this.rplr = rplr;
        }

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
