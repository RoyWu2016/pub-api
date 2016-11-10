package com.ai.api.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.ai.api.bean.ChinaTimeBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import com.ai.commons.beans.params.ClassifiedBean;
import com.ai.commons.beans.params.GeoCountryCallingCodeBean;
import com.ai.commons.beans.params.TextileCategoryBean;
import com.ai.commons.beans.params.product.SysProductTypeBean;

/**
 * Created by Administrator on 2016/6/21 0021.
 */
public interface ParameterService {

	List<ProductCategoryDtoBean> getProductCategoryList(boolean refresh);

	List<GeoCountryCallingCodeBean> getCountryList(boolean refresh);

	Map<String, List<ChecklistTestSampleSizeBean>> getTestSampleSizeList(boolean refresh);

	List<CKLTestVO> getChecklistPublicTestList(boolean refresh);

	List<CKLDefectVO> getChecklistPublicDefectList(boolean refresh);

	List<SysProductTypeBean> getProductTypeList(boolean refresh);

	List<TextileCategoryBean> getTextileProductCategories(boolean refresh);

	List<ClassifiedBean> getAiOffices(boolean refresh);

	List<ProductFamilyDtoBean> getProductFamilyList(boolean refresh);

	ChinaTimeBean getChinaTime();

	String getSaleImage(String sicId);

	ServiceCallResult getLostPasswordByEmail(String email);

}
