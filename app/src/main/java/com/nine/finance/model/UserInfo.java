package com.nine.finance.model;

import java.io.Serializable;

/**
 * Created by changqing on 2018/2/6.
 */

public class UserInfo implements Serializable {

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


//    {
//        "userId": "EB6455D7C4D84389B0F9C8B83905CFBD",
//            "token": "75EC529AF1FB481688DE2099D0571AAC",
//            "name": "230404198309060519",
//            "nickName": "admin",
//            "mobile": "185",
//            "tel": null,
//            "address": "china beijing ",
//            "head": null,
//            "state": null,
//            "role": "1",
//            "idnum": "123"
//    }


    private String nationality = "";
    private String nativePlace = "";
    private String gender = "";
    private String ethnic = "";
    private String birthday = "";


    private String userId = "";
    private String token = "";
    private String idnum = "";
    private String name = "";
    private String nickName = "";
    private String mobile = "";
    private String tel = "";
    private String address = "";
    private String head = "";
    private String state = "";
    private String role = "";

    public String getUserId() {
        if (userId == null) userId = "";
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIDNum() {
        return idnum;
    }

    public void setIDNum(String IDNum) {
        this.idnum = IDNum;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getInativePlaced() {
        return nativePlace;
    }

    public void setInativePlaced(String inativePlaced) {
        this.nativePlace = inativePlaced;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEthnic() {
        return ethnic;
    }

    public void setEthnic(String ethnic) {
        this.ethnic = ethnic;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
