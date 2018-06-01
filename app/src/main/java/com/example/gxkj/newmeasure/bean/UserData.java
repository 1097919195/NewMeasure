package com.example.gxkj.newmeasure.bean;

/**
 * Created by Administrator on 2018/6/1 0001.
 */

public class UserData {
    /**
     * _id : 5b06a5b09134ca08662ec758
     * name : lts
     * mobile : 18888780080
     * nickname : duc
     * state : 1
     * remark : null
     * mtm_id : 5b06a4b99134ca429858e256
     * updated_at : 2018-06-01 15:28:42
     * created_at : 2018-05-24 19:44:48
     * last_login_at : 2018-06-01 15:28:42
     * last_ip : 117.149.174.132
     */

    private String _id;
    private String name;
    private String mobile;
    private String nickname;
    private String state;
    private String remark;
    private String mtm_id;
    private String updated_at;
    private String created_at;
    private String last_login_at;
    private String last_ip;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMtm_id() {
        return mtm_id;
    }

    public void setMtm_id(String mtm_id) {
        this.mtm_id = mtm_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getLast_login_at() {
        return last_login_at;
    }

    public void setLast_login_at(String last_login_at) {
        this.last_login_at = last_login_at;
    }

    public String getLast_ip() {
        return last_ip;
    }

    public void setLast_ip(String last_ip) {
        this.last_ip = last_ip;
    }
}
