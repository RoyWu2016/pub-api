package com.ai.api.bean;

import org.json.JSONObject;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class ProductFamilyDtoBean {

    private String id;
    private String categoryId;
    private String name;

    public ProductFamilyDtoBean(){}

    public ProductFamilyDtoBean(JSONObject obj){
        this.id = obj.getString("id");
        this.categoryId = obj.getString("categoryId");
        this.name = obj.getString("name");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
