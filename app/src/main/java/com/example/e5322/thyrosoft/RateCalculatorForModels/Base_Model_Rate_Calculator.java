package com.example.e5322.thyrosoft.RateCalculatorForModels;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Priyanka on 09-06-2018.
 */

public class Base_Model_Rate_Calculator implements Parcelable {

    String product;

    String billrate;

    String gettotalcount;

    Barcodes[] barcodes;

    Rate rate;

    String name;

    String fasting;

    Childs[] childs;

    String code;
    String isCPL;

    String type;


    public String getIsCart() {
        return isCart;
    }

    public void setIsCart(String isCart) {
        this.isCart = isCart;
    }

    String isCart;
    String is_lock;

    public String getIs_lock() {
        return is_lock;
    }

    public void setIs_lock(String is_lock) {
        this.is_lock = is_lock;
    }

    protected Base_Model_Rate_Calculator(Parcel in) {
        product = in.readString();
        billrate = in.readString();
        gettotalcount = in.readString();
        rate = in.readParcelable(Rate.class.getClassLoader());
        name = in.readString();
        fasting = in.readString();
        code = in.readString();
        isCPL=in.readString();
        type = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(product);
        dest.writeString(billrate);
        dest.writeString(gettotalcount);
        dest.writeTypedArray(barcodes, flags);
        dest.writeParcelable(rate, flags);
        dest.writeString(name);
        dest.writeString(fasting);
        dest.writeTypedArray(childs, flags);
        dest.writeString(code);
        dest.writeString(isCPL);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Base_Model_Rate_Calculator> CREATOR = new Creator<Base_Model_Rate_Calculator>() {
        @Override
        public Base_Model_Rate_Calculator createFromParcel(Parcel in) {
            return new Base_Model_Rate_Calculator(in);
        }

        @Override
        public Base_Model_Rate_Calculator[] newArray(int size) {
            return new Base_Model_Rate_Calculator[size];
        }
    };

    public String getGettotalcount() {
        return gettotalcount;
    }

    public void setGettotalcount(String gettotalcount) {
        this.gettotalcount = gettotalcount;
    }

    public Base_Model_Rate_Calculator() {
    }

    public String getIsCPL() {
        return isCPL;
    }

    public void setIsCPL(String isCPL) {
        this.isCPL = isCPL;
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

    public class Barcodes implements Parcelable {
        String specimen_type;

        String code;

        protected Barcodes(Parcel in) {
            specimen_type = in.readString();
            code = in.readString();
        }

        public final Creator<Barcodes> CREATOR = new Creator<Barcodes>() {
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

    public class Childs implements Parcelable {
        String code;

        String type;

        protected Childs(Parcel in) {
            code = in.readString();
            type = in.readString();
        }

        public final Creator<Childs> CREATOR = new Creator<Childs>() {
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

    public class Rate implements Parcelable {
        String b2c;

        String b2b;
        String cplr;
        String rplr;

        protected Rate(Parcel in) {
            b2c = in.readString();
            b2b = in.readString();
            cplr = in.readString();
            rplr = in.readString();
        }

        public final Creator<Rate> CREATOR = new Creator<Rate>() {
            @Override
            public Rate createFromParcel(Parcel in) {
                return new Rate(in);
            }

            @Override
            public Rate[] newArray(int size) {
                return new Rate[size];
            }
        };

        public String getCplr() {
            return cplr;
        }

        public String getRplr() {
            return rplr;
        }

        public void setCplr(String cplr) {
            this.cplr = cplr;
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

        @Override
        public String toString() {
            return "ClassPojo [b2c = " + b2c + ", b2b = " + b2b + "+]";
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(b2c);
            dest.writeString(b2b);
            dest.writeString(cplr);
            dest.writeString(rplr);

        }
    }

    public boolean checkIfChildsContained(Base_Model_Rate_Calculator tt) {
        boolean isChilds = false;
        if (getChilds() != null && getChilds().length > 0) {
            int ttChildSize = tt.getChilds().length;
            int commonChildsSize = 0;
            for (Base_Model_Rate_Calculator.Childs tc : tt.getChilds()) {
                for (Base_Model_Rate_Calculator.Childs tChild : getChilds()) {
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

