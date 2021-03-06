package com.nine.finance.model;

import java.io.Serializable;

/**
 * Created by changqing on 2018/1/30.
 */

public class BankInfo implements Serializable {


    /**
     * state : null
     * bankId : 5D3C80C6090146EF90C18C61DB2C5132
     * bankName : 花旗
     */

    private String state;
    private String bankId;
    private String bankName;
    private String cardNum;

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }
}
