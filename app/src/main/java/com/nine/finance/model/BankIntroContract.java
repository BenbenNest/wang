package com.nine.finance.model;

/**
 * Created by changqing on 2018/3/10.
 */

public class BankIntroContract {

    private String bankId = "";
    private String introduceStr = "";
    private String agreementStr = "";

    public String getBankId() {
        return bankId;
    }

    public void setBankId(String bankId) {
        this.bankId = bankId;
    }

    public String getIntroduceStr() {
        return introduceStr;
    }

    public void setIntroduceStr(String introduceStr) {
        this.introduceStr = introduceStr;
    }

    public String getAgreementStr() {
        return agreementStr;
    }

    public void setAgreementStr(String agreementStr) {
        this.agreementStr = agreementStr;
    }
}
