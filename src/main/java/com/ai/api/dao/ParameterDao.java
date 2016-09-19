/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao;

import java.util.List;
import java.util.Map;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import com.ai.commons.beans.params.ClassifiedBean;
import com.ai.commons.beans.params.product.SysProductTypeBean;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao
 *
 *  File Name       : ParameterDao.java
 *
 *  Creation Date   : May 24, 2016
 *
 *  Author          : Allen Zhang
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

public interface ParameterDao {

	List<ProductCategoryDtoBean> getProductCategoryList();

	List<ProductFamilyDtoBean> getProductFamilyList();

	List<String> getCountryList();

	Map<String,List<ChecklistTestSampleSizeBean>> getTestSampleSizeList();

	List<CKLTestVO> getChecklistPublicTestList();

    List<CKLDefectVO> getChecklistPublicDefectList();

	List<SysProductTypeBean> getProductTypeList();
	
	List<ClassifiedBean> getTextileProductCategories();
}
