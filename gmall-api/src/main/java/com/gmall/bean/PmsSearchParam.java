package com.gmall.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Tengxq
 * @Date 2020/3/2 11:35
 * @Describe
 * @Version 1.0
 **/

public class PmsSearchParam implements Serializable {

    private String keyword;

    private String catalog3Id;

    private String[] valueId;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getCatalog3Id() {
        return catalog3Id;
    }

    public void setCatalog3Id(String catalog3Id) {
        this.catalog3Id = catalog3Id;
    }

    public String[] getValueId() {
        return valueId;
    }

    public void setValueId(String[] valueId) {
        this.valueId = valueId;
    }
}
