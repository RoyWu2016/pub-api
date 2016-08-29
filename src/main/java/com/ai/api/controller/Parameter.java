package com.ai.api.controller;

import java.util.List;
import java.util.Map;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import org.springframework.http.ResponseEntity;

/**
 * Created by Henry Yue on 2016/6/21 0021.
 */
public interface Parameter {

    ResponseEntity<List<ProductCategoryDtoBean>> getProductCategoryList() ;

    ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList();

	ResponseEntity<List<String>> getCountryList();

	ResponseEntity<Map<String,List<ChecklistTestSampleSizeBean>>> getTestSampleSizeList();

    ResponseEntity<List<CKLTestVO>> getChecklistPublicTestList();

    ResponseEntity<List<CKLDefectVO>> getChecklistPublicDefectList();

    ResponseEntity<Map<String, Object>> getProductTypeList();
}
