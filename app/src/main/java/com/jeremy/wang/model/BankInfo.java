package com.jeremy.wang.model;

/**
 * Created by changqing on 2018/1/30.
 */

public class BankInfo {


    /**
     * BandId :
     * BankName :
     * state : 0
     */

    private String BandId;
    private String BankName;
    private int state;

    public String getBandId() {
        return BandId;
    }

    public void setBandId(String BandId) {
        this.BandId = BandId;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String BankName) {
        this.BankName = BankName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}
