package com.example.gxkj.newmeasure.bean;

/**
 * Created by Administrator on 2018/6/4 0004.
 */

public class MeasureWeChat {

    /**
     * _id : 5b0cc91e9134ca08a46ed9de
     * openID : onB531HhWyhSX4BksjE7EsFOfIr4
     * subscribe : true
     * name : 我有时光机
     * nickname : 我有时光机
     * gender : 1
     * city :
     * province :
     * country : 阿鲁巴
     * unionid : null
     * avatar : http://thirdwx.qlogo.cn/mmopen/vi_32/DgkIyM4AtyvQveD2IlekeDUEtoxcta9Sqla2btxeoSBFP3vqGMUQdXkdVZicHMTqwhrujtPS5fQPewT1RWRPJIw/132
     * updated_at : 2018-05-31 16:12:04
     * created_at : 2018-05-29 11:29:34
     * height : 3
     * weight : 2
     * remark : 123451234567890123456789012345678934567123451234567890123456789012345678934567123451234567890123456789012345678934567
     * measureTimes : 1
     * qrcode : {"path":"http://ts.npclo.com/images/qrcode/onB531HhWyhSX4BksjE7EsFOfIr4.png","relative_path":"/images/qrcode/onB531HhWyhSX4BksjE7EsFOfIr4.png"}
     */

    private String _id;
    private String openID;
    private String name;
    private String nickname;
    private int gender;
    private String city;
    private String province;
    private String country;
    private Object unionid;
    private String avatar;
    private String height;
    private String weight;
    private String remark;
    private int measureTimes;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOpenID() {
        return openID;
    }

    public void setOpenID(String openID) {
        this.openID = openID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Object getUnionid() {
        return unionid;
    }

    public void setUnionid(Object unionid) {
        this.unionid = unionid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getMeasureTimes() {
        return measureTimes;
    }

    public void setMeasureTimes(int measureTimes) {
        this.measureTimes = measureTimes;
    }

}
