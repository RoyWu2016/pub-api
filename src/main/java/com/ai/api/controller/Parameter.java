package com.ai.api.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import com.ai.api.bean.ChecklistSampleSize;
import com.ai.api.bean.ChinaTimeBean;
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

	ResponseEntity<ChinaTimeBean> getChinaTime();

	ResponseEntity<List<CKLTestVO>> getChecklistPublicTestList(boolean refresh);

	ResponseEntity<List<CKLDefectVO>> getChecklistPublicDefectList(boolean refresh);

	ResponseEntity<List<ProductCategoryDtoBean>> getProductCategoryList(boolean refresh);

	ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList(boolean refresh);

	ResponseEntity<List<CountryBean>> getCountryList(boolean refresh);

	ResponseEntity<List<ChecklistSampleSize>> getTestSampleSizeList(boolean refresh);

	ResponseEntity<Map<String, Object>> getProductTypeList(boolean refresh);

	ResponseEntity<List<DropdownListOptionBean>> getTextileProductCategories(boolean refresh);

	ResponseEntity<List<DropdownListOptionBean>> getAiOffices(boolean refresh);
}
