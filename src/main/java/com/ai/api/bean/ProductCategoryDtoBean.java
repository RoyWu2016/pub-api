package com.ai.api.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class ProductCategoryDtoBean implements Serializable {

    private String id;
    private String name;

    public ProductCategoryDtoBean(){}

    public ProductCategoryDtoBean(JSONObject obj){
        this.id = obj.getString("id");
        this.name = obj.getString("name");
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
