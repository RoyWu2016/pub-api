package com.ai.api.controller;

import org.springframework.http.ResponseEntity;

import com.ai.commons.beans.ApiCallResult;

public interface ParameterV2 {
	
	ResponseEntity<ApiCallResult> getChinaTime();

	ResponseEntity<ApiCallResult> getChecklistPublicTestList(boolean refresh);

	ResponseEntity<ApiCallResult> getChecklistPublicDefectList(boolean refresh);

	ResponseEntity<ApiCallResult> getProductCategoryList(boolean refresh);

	ResponseEntity<ApiCallResult> getProductFamilyList(boolean refresh);

	ResponseEntity<ApiCallResult> getCountryList(boolean refresh);

	ResponseEntity<ApiCallResult> getTestSampleSizeList(boolean refresh);

	ResponseEntity<ApiCallResult> getProductTypeList(boolean refresh);

	ResponseEntity<ApiCallResult> getTextileProductCategories(boolean refresh);

	ResponseEntity<ApiCallResult> getAiOffices(boolean refresh);

	ResponseEntity<ApiCallResult> getContinents(boolean refresh);

	ResponseEntity<ApiCallResult> getCitiesByCountryId(String countryId, boolean refresh);

	ResponseEntity<ApiCallResult> getProvincesByCountryId(String countryId, boolean refresh);

	ResponseEntity<ApiCallResult> getCountriesByContinentId(String continentId, boolean refresh);

	ResponseEntity<ApiCallResult> getCitiesByProvinceId(String provinceId, boolean refresh);

	ResponseEntity<ApiCallResult> getAllCountries(boolean refresh);

	ResponseEntity<ApiCallResult> getSaleImage(String sicId,boolean refresh);

	ResponseEntity<ApiCallResult> isACAUser(String userName);

	ResponseEntity<ApiCallResult> getCommonImagesBase64(String resourceName, boolean refresh);

}
