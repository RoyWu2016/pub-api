package com.ai.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ai.api.bean.ChecklistSampleSize;
import com.ai.api.bean.CountryBean;
import com.ai.api.bean.DropdownListOptionBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;

/**
 * Created by Henry Yue on 2016/6/21 0021.
 */
public interface Parameter {

    ResponseEntity<List<ProductCategoryDtoBean>> getProductCategoryList() ;

    ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList();

	ResponseEntity<List<CountryBean>> getCountryList();

	ResponseEntity<List<ChecklistSampleSize>> getTestSampleSizeList();

    ResponseEntity<List<CKLTestVO>> getChecklistPublicTestList();

    ResponseEntity<List<CKLDefectVO>> getChecklistPublicDefectList();

    ResponseEntity<Map<String, Object>> getProductTypeList();
    
    ResponseEntity<List<DropdownListOptionBean>> getTextileProductCategories();
    
    ResponseEntity<List<DropdownListOptionBean>> getAiOffices();
}
