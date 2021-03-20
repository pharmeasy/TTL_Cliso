package com.example.e5322.thyrosoft.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E5322 on 06-06-2018.
 */

public class BaseModel implements Parcelable {

    private String product;
    private String billrate;
    private String location;
    private String name;
    private String fasting;
    private String code;
    private String type;
    private String trf;
    private String isCPL;
    private int isNHL;
    private String subtypes;
    Childs[] childs;
    Barcodes[] barcodes;
    Rate rate;
    private int isAB;

    protected BaseModel(Parcel in) {
        product = in.readString();
        billrate = in.readString();
        location = in.readString();
        name = in.readString();
        fasting = in.readString();
        code = in.readString();
        type = in.readString();
        trf = in.readString();
        isCPL = in.readString();
        subtypes = in.readString();
        childs = in.createTypedArray(Childs.CREATOR);
        barcodes = in.createTypedArray(Barcodes.CREATOR);
        rate = in.readParcelable(Rate.class.getClassLoader());
        isAB = in.readInt();
        isNHL = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product);
        dest.writeString(billrate);
        dest.writeString(location);
        dest.writeString(name);
        dest.writeString(fasting);
        dest.writeString(code);
        dest.writeString(type);
        dest.writeString(trf);
        dest.writeString(isCPL);
        dest.writeString(subtypes);
        dest.writeTypedArray(childs, flags);
        dest.writeTypedArray(barcodes, flags);
        dest.writeParcelable(rate, flags);
        dest.writeInt(isAB);
        dest.writeInt(isNHL);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        @Override
        public BaseModel createFromParcel(Parcel in) {
            return new BaseModel(in);
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }
    };


    public int getIsNHL() {
        return isNHL;
    }

    public void setIsNHL(int isNHL) {
        this.isNHL = isNHL;
    }

    public int getIsAB() {
        return isAB;
    }

    public void setIsAB(int isAB) {
        this.isAB = isAB;
    }

    public String getIsCPL() {
        return isCPL;
    }

    public void setIsCPL(String isCPL) {
        this.isCPL = isCPL;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBillrate() {
        return billrate;
    }

    public void setBillrate(String billrate) {
        this.billrate = billrate;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Barcodes[] getBarcodes() {
        return barcodes;
    }

    public void setBarcodes(Barcodes[] barcodes) {
        this.barcodes = barcodes;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFasting() {
        return fasting;
    }

    public void setFasting(String fasting) {
        this.fasting = fasting;
    }

    public Childs[] getChilds() {
        return childs;
    }

    public void setChilds(Childs[] childs) {
        this.childs = childs;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTrf() {
        return trf;
    }

    public void setTrf(String trf) {
        this.trf = trf;
    }

    public String getSubtypes() {
        return subtypes;
    }

    public void setSubtypes(String subtypes) {
        this.subtypes = subtypes;
    }

    public static class Barcodes implements Parcelable {
        public Barcodes() {
        }

        String specimen_type;

        String code;

        Barcodes(Parcel in) {
            specimen_type = in.readString();
            code = in.readString();
        }

        public static final Creator<Barcodes> CREATOR = new Creator<Barcodes>() {
            @Override
            public Barcodes createFromParcel(Parcel in) {
                return new Barcodes(in);
            }

            @Override
            public Barcodes[] newArray(int size) {
                return new Barcodes[size];
            }
        };

        public String getSpecimen_type() {
            return specimen_type;
        }

        public void setSpecimen_type(String specimen_type) {
            this.specimen_type = specimen_type;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        @Override
        public String toString() {
            return "ClassPojo [specimen_type = " + specimen_type + ", code = " + code + "]";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(specimen_type);
            dest.writeString(code);
        }
    }

    public static class Childs implements Parcelable {
        String code;
        String type;


        Childs(Parcel in) {
            code = in.readString();
            type = in.readString();

        }

        public static final Creator<Childs> CREATOR = new Creator<Childs>() {
            @Override
            public Childs createFromParcel(Parcel in) {
                return new Childs(in);
            }

            @Override
            public Childs[] newArray(int size) {
                return new Childs[size];
            }
        };

        public String getCode() {
            return code;
        }


        public void setCode(String code) {
            this.code = code;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return "ClassPojo [code = " + code + ", type = " + type + "]";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(code);
            dest.writeString(type);
        }
    }

    public static class Rate implements Parcelable {
        String b2c;
        String b2b;
        String cplr;
        String rplr;
        String NHLRate;

        public Rate() {
        }


        protected Rate(Parcel in) {
            b2c = in.readString();
            b2b = in.readString();
            cplr = in.readString();
            rplr = in.readString();
            NHLRate = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(b2c);
            dest.writeString(b2b);
            dest.writeString(cplr);
            dest.writeString(rplr);
            dest.writeString(NHLRate);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Rate> CREATOR = new Creator<Rate>() {
            @Override
            public Rate createFromParcel(Parcel in) {
                return new Rate(in);
            }

            @Override
            public Rate[] newArray(int size) {
                return new Rate[size];
            }
        };

        public String getNHLRate() {
            return NHLRate;
        }

        public void setNHLRate(String NHLRate) {
            this.NHLRate = NHLRate;
        }

        public String getRplr() {
            return rplr;
        }

        public void setRplr(String rplr) {
            this.rplr = rplr;
        }

        public String getCplr() {
            return cplr;
        }

        public void setCplr(String cplr) {
            this.cplr = cplr;
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

        @Override
        public String toString() {
            return "ClassPojo [b2c = " + b2c + ", b2b = " + b2b + "]";
        }


    }

    public boolean checkIfChildsContained(BaseModel tt) {
        boolean isChilds = false;
        if (getChilds() != null && getChilds().length > 0) {
            int ttChildSize = tt.getChilds().length;
            int commonChildsSize = 0;
            for (BaseModel.Childs tc : tt.getChilds()) {
                for (BaseModel.Childs tChild : getChilds()) {
                    if (tc.getCode() != null && tc.getCode().equalsIgnoreCase(tChild.getCode())) {
                        commonChildsSize++;
                    }
                }
            }
            if (commonChildsSize > 0 && commonChildsSize == ttChildSize) {
                isChilds = true;
            }
        }
        return isChilds;
    }

    public BaseModel() {
    }


    @Override
    public String toString() {
        return code;
    }
}