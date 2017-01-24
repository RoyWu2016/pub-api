package com.ai.api.bean;

import java.io.Serializable;

public class TagSearchBean implements Serializable {

	private static final long serialVersionUID = 2391607644344262088L;
	
	private String id;
    private String name;
    private String description;
    private String tagLevel;
    
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getTagLevel() {
		return tagLevel;
	}
	public void setTagLevel(String tagLevel) {
		this.tagLevel = tagLevel;
	}
}
