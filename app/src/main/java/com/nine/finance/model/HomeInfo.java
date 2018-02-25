package com.nine.finance.model;

import java.io.Serializable;

/**
 * Created by changqing on 2018/2/25.
 */

public class HomeInfo implements Serializable {

    /**
     * id : 210000
     * createDate : null
     * updateDate : null
     * parentId : 000000
     * name : 辽宁省
     * shortName : 辽宁省
     */

    private String id;
    private Object createDate;
    private Object updateDate;
    private String parentId;
    private String name;
    private String shortName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Object createDate) {
        this.createDate = createDate;
    }

    public Object getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Object updateDate) {
        this.updateDate = updateDate;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }


}
