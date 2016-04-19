package com.ai.api.bean;

import com.ai.commons.beans.customer.ProductFamilyInfoBean;

import java.util.List;

/**
 * Created by KK on 3/21/2016.
 */
public class ProductFamilyBean {

    private String productTypeSelection;

    private String rememberSelectdFamily;

    private List<ProductFamilyInfoBean> productFamilyInfo;

    private List<RelevantCategoryInfoBean> relevantCategoryInfo;

    public String getProductTypeSelection() {
        return productTypeSelection;
    }

    public void setProductTypeSelection(String productTypeSelection) {
        this.productTypeSelection = productTypeSelection;
    }

    public String getRememberSelectdFamily() {
        return rememberSelectdFamily;
    }

    public void setRememberSelectdFamily(String rememberSelectdFamily) {
        this.rememberSelectdFamily = rememberSelectdFamily;
    }

    public List<ProductFamilyInfoBean> getProductFamilyInfo() {
        return productFamilyInfo;
    }

    public void setProductFamilyInfo(List<ProductFamilyInfoBean> productFamilyInfo) {
        this.productFamilyInfo = productFamilyInfo;
    }

    public List<RelevantCategoryInfoBean> getRelevantCategoryInfo() {
        return relevantCategoryInfo;
    }

    public void setRelevantCategoryInfo(List<RelevantCategoryInfoBean> relevantCategoryInfo) {
        this.relevantCategoryInfo = relevantCategoryInfo;
    }
}
