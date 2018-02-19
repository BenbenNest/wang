package com.nine.finance.model;

import java.io.Serializable;

/**
 * Created by changqing on 2018/2/19.
 */

public class BranchInfo implements Serializable {

    /**
     * bankId :
     * bankName :
     * branchId :
     * branchName :
     */

    private String bankId;
    private String bankName;
    private String branchId;
    private String branchName;

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

    public String getBranchId() {
        return branchId;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

}
