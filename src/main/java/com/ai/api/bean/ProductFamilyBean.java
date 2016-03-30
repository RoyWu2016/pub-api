package com.ai.api.bean;

import com.ai.commons.beans.customer.ProductFamilyInfoBean;
import com.ai.api.bean.RelevantCategoryInfoBean;

import java.util.List;

/**
 * Created by KK on 3/21/2016.
 */
public class ProductFamilyBean {

    private String product_type_selection;

    private String remember_selectd_family;

    private List<ProductFamilyInfoBean> product_family_info;

    private List<RelevantCategoryInfoBean> relevant_category_info;

    public String getProduct_type_selection() {
        return product_type_selection;
    }

    public void setProduct_type_selection(String product_type_selection) {
        this.product_type_selection = product_type_selection;
    }

    public String getRemember_selectd_family() {
        return remember_selectd_family;
    }

    public void setRemember_selectd_family(String remember_selectd_family) {
        this.remember_selectd_family = remember_selectd_family;
    }

    public List<ProductFamilyInfoBean> getProduct_family_info() {
        return product_family_info;
    }

    public void setProduct_family_info(List<ProductFamilyInfoBean> product_family_info) {
        this.product_family_info = product_family_info;
    }

    public List<RelevantCategoryInfoBean> getRelevant_category_info() {
        return relevant_category_info;
    }

    public void setRelevant_category_info(List<RelevantCategoryInfoBean> relevant_category_info) {
        this.relevant_category_info = relevant_category_info;
    }
}
