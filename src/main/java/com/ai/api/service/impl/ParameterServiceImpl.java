package com.ai.api.service.impl;

import java.util.List;
import java.util.Map;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.dao.ParameterDao;
import com.ai.api.service.ParameterService;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import com.ai.commons.beans.params.product.SysProductTypeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * Created by Administrator on 2016/6/21 0021.
 */

@Service
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    @Qualifier("paramDao")
    private ParameterDao paramDao;

    @Override
    public List<ProductCategoryDtoBean> getProductCategoryList(){
        return paramDao.getProductCategoryList();
    }

    @Override
    public List<ProductFamilyDtoBean>  getProductFamilyList(){
        return paramDao.getProductFamilyList();
    }

	@Override
	public List<String>  getCountryList(){
		return paramDao.getCountryList();
	}

	@Override
	public Map<String,List<ChecklistTestSampleSizeBean>> getTestSampleSizeList(){
		return paramDao.getTestSampleSizeList();
	}

    @Override
    public List<SysProductTypeBean> getProductTypeList(){
        return paramDao.getProductTypeList();
    }
}
