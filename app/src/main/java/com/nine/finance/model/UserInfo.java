package com.nine.finance.model;

/**
 * Created by changqing on 2018/2/6.
 */

public class UserInfo {
    /**
     * token :
     * IDNum :
     * name :
     * nickName :
     * mobile :
     * tel :
     * address :
     * head :
     * state : 1
     */

    private String token;
    private String IDNum;
    private String name;
    private String nickName;
    private String mobile;
    private String tel;
    private String address;
    private String head;
    private int state;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIDNum() {
        return IDNum;
    }

    public void setIDNum(String IDNum) {
        this.IDNum = IDNum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }


}
