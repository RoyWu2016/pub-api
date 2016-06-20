package com.ai.api.bean;

import java.util.List;

/**
 * Created by KK on 5/17/2016.
 */
public class SysProductFamilyBean {
    private List<String> categoryId;
    private List<String> id;
    private List<String> name;

    public List<String> getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(List<String> categoryId) {
        this.categoryId = categoryId;
    }

    public List<String> getId() {
        return id;
    }

    public void setId(List<String> id) {
        this.id = id;
    }

    public List<String> getName() {
        return name;
    }

    public void setName(List<String> name) {
        this.name = name;
    }
}
