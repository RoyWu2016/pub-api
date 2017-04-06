package com.ai.api.bean;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@SuppressWarnings("rawtypes")
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultBean implements Serializable {

	private static final long serialVersionUID = 2056744416949956331L;
	
	private List pageItems;
	private Integer pageNo;
    private Integer totalSize;
    
	public List getPageItems() {
		return pageItems;
	}
	public void setPageItems(List pageItems) {
		this.pageItems = pageItems;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getTotalSize() {
		return totalSize;
	}
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}
}
