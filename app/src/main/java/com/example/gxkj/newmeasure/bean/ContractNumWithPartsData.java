package com.example.gxkj.newmeasure.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class ContractNumWithPartsData implements Parcelable{

    private String _id;
    private String mtm_id;
    private String name;
    private ArrayList<Parts> parts;

    protected ContractNumWithPartsData(Parcel in) {
        _id = in.readString();
        mtm_id = in.readString();
        name = in.readString();
    }

    public static final Creator<ContractNumWithPartsData> CREATOR = new Creator<ContractNumWithPartsData>() {
        @Override
        public ContractNumWithPartsData createFromParcel(Parcel in) {
            return new ContractNumWithPartsData(in);
        }

        @Override
        public ContractNumWithPartsData[] newArray(int size) {
            return new ContractNumWithPartsData[size];
        }
    };

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getMtm_id() {
        return mtm_id;
    }

    public void setMtm_id(String mtm_id) {
        this.mtm_id = mtm_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Parts> getParts() {
        return parts;
    }

    public void setParts(ArrayList<Parts> parts) {
        this.parts = parts;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(mtm_id);
        dest.writeString(name);
    }

    private static class Parts implements Parcelable{
        private String name;

        protected Parts(Parcel in) {
            name = in.readString();
        }

        public static final Creator<Parts> CREATOR = new Creator<Parts>() {
            @Override
            public Parts createFromParcel(Parcel in) {
                return new Parts(in);
            }

            @Override
            public Parts[] newArray(int size) {
                return new Parts[size];
            }
        };

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(name);
        }
    }
}
