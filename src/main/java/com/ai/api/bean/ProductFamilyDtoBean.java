package com.ai.api.bean;

import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/6/22 0022.
 */
public class ProductFamilyDtoBean implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1730409263387710974L;
	
	private String id;
    private String categoryName;
    private String categoryId;
    private String name;

    public ProductFamilyDtoBean(){}

    public ProductFamilyDtoBean(JSONObject obj){
        this.id = obj.getString("id");
        this.categoryId = obj.getString("categoryId");
        this.name = obj.getString("name");
        this.categoryName = obj.getString("categoryName");
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
}
