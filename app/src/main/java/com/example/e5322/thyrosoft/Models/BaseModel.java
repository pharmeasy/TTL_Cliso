package com.example.e5322.thyrosoft.Models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by E5322 on 06-06-2018.
 */

public class BaseModel implements Parcelable {

    private String product;
    private String billrate;


    Barcodes[] barcodes;

    Rate rate;

    public BaseModel() {
    }

    private String name;

    private String fasting;

    Childs[] childs;

    private String code;

    private String type;


    public BaseModel(Parcel in) {
        product = in.readString();
        billrate = in.readString();
        barcodes = in.createTypedArray(Barcodes.CREATOR);
        rate = in.readParcelable(Rate.class.getClassLoader());
        name = in.readString();
        fasting = in.readString();
        childs = in.createTypedArray(Childs.CREATOR);
        code = in.readString();
        type = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product);
        dest.writeString(billrate);
        dest.writeTypedArray(barcodes, flags);
        dest.writeParcelable(rate, flags);
        dest.writeString(name);
        dest.writeString(fasting);
        dest.writeTypedArray(childs, flags);
        dest.writeString(code);
        dest.writeString(type);
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

        public Rate() {
        }

        String b2b;

        public Rate(Parcel in) {
            b2c = in.readString();
            b2b = in.readString();
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(b2c);
            dest.writeString(b2b);
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
}
