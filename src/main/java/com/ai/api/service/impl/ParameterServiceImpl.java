package com.ai.api.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.ai.api.bean.ChinaTimeBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.dao.ParameterDao;
import com.ai.api.service.ParameterService;
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

@Service
public class ParameterServiceImpl implements ParameterService {

    @Autowired
    @Qualifier("paramDao")
    private ParameterDao paramDao;

    @Override
    public List<ProductCategoryDtoBean> getProductCategoryList(boolean refresh){
        return paramDao.getProductCategoryList(refresh);
    }

    @Override
    public List<ProductFamilyDtoBean>  getProductFamilyList(boolean refresh){
        return paramDao.getProductFamilyList(refresh);
    }

	@Override
	public List<GeoCountryCallingCodeBean>  getCountryList(boolean refresh){
		return paramDao.getCountryList(refresh);
	}

	@Override
	public Map<String,List<ChecklistTestSampleSizeBean>> getTestSampleSizeList(boolean refresh){
		return paramDao.getTestSampleSizeList(refresh);
	}

    @Override
    public List<CKLTestVO> getChecklistPublicTestList(boolean refresh){
        return paramDao.getChecklistPublicTestList(refresh);
    }

    @Override
    public List<CKLDefectVO> getChecklistPublicDefectList(boolean refresh){
        return paramDao.getChecklistPublicDefectList(refresh);
    }

    @Override
    public List<SysProductTypeBean> getProductTypeList(boolean refresh){
        return paramDao.getProductTypeList(refresh);
    }

	@Override
	public List<TextileCategoryBean> getTextileProductCategories(boolean refresh) {
		// TODO Auto-generated method stub
		return paramDao.getTextileProductCategories(refresh);
	}

	@Override
	public List<ClassifiedBean> getAiOffices(boolean refresh) {
		// TODO Auto-generated method stub
		return paramDao.getAiOffices(refresh);
	}

    @Override
    public ChinaTimeBean getChinaTime() {

        ChinaTimeBean chinaTimeBean = new ChinaTimeBean();
        Calendar cale = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss", Locale.ENGLISH);
        int unitTimeStamp = (int) (System.currentTimeMillis() / 1000);
        chinaTimeBean.setDatetime(sdf.format(cale.getTime()));
        chinaTimeBean.setTimezone("UTC+8");
        chinaTimeBean.setUnixTimeStamp(unitTimeStamp);
        return chinaTimeBean;
    }

    @Override
    public String getSaleImage(String sicId){
        return paramDao.getSaleImage(sicId);
    }
}

