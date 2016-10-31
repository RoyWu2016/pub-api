package com.ai.api.controller;

import java.util.List;
import java.util.Map;

import com.ai.api.bean.ChecklistSampleSize;
import com.ai.api.bean.ChinaTimeBean;
import com.ai.api.bean.CountryBean;
import com.ai.api.bean.DropdownListOptionBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import org.springframework.http.ResponseEntity;

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

	ResponseEntity<ApiCallResult> getContinents(boolean refresh);

	ResponseEntity<ApiCallResult> getCitiesByCountryId(String countryId, boolean refresh);

	ResponseEntity<ApiCallResult> getProvincesByCountryId(String countryId, boolean refresh);

	ResponseEntity<ApiCallResult> getCountriesByContinentId(String continentId, boolean refresh);

	ResponseEntity<ApiCallResult> getCitiesByProvinceId(String provinceId, boolean refresh);

	ResponseEntity<ApiCallResult> getAllCountries(boolean refresh);


}
