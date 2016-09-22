package com.ai.api.service;

import java.util.List;
import java.util.Map;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import com.ai.commons.beans.params.ClassifiedBean;
import com.ai.commons.beans.params.product.SysProductTypeBean;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public interface ParameterService {

    List<ProductCategoryDtoBean> getProductCategoryList();

    List<ProductFamilyDtoBean> getProductFamilyList();

	List<String> getCountryList();

	Map<String,List<ChecklistTestSampleSizeBean>> getTestSampleSizeList();

    List<CKLTestVO> getChecklistPublicTestList();

    List<CKLDefectVO> getChecklistPublicDefectList();

    List<SysProductTypeBean> getProductTypeList();
    
    List<ClassifiedBean> getTextileProductCategories();
    
    List<ClassifiedBean> getAiOffices();
}
