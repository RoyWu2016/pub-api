package com.ai.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.ai.api.bean.InspectionDraftBean;
import com.ai.api.bean.UserBean;
import com.ai.api.bean.consts.ConstMap;
import com.ai.api.controller.Parameter;
import com.ai.api.dao.DraftDao;
import com.ai.api.service.DraftService;
import com.ai.api.service.UserService;
import com.ai.api.util.AIUtil;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.service.impl
 * <p>
 * Creation Date   : 2016/8/1 17:04
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/


@Service
public class DraftServiceImpl implements DraftService {

	protected Logger logger = LoggerFactory.getLogger(DraftServiceImpl.class);

	@Autowired
	private DraftDao draftDao;

	@Autowired
	@Qualifier("userService")
	private UserService userService;

	@Override
	public boolean deleteDraft(String userId, String ids) throws Exception {
		Map<String,String> params = new HashMap<String, String>();
		String login = userService.getLoginByUserId(userId);//customerDao.getGeneralUser(userId).getLogin();
		params.put("login",login);
		params.put("ids",ids);
		return draftDao.deleteDrafts(params);
	}

	@Override
	public boolean deleteDraftFromPsi(String userId, String draftIds) throws Exception {
		return draftDao.deleteDraftsFromPsi(userId, draftIds);
	}

	@Override
	public InspectionBookingBean createDraft(String userId, String serviceType) throws Exception{

		UserBean userBean = userService.getCustById(userId);
		String parentId = userBean.getCompany().getParentCompanyId();
		if (parentId == null) parentId = "";
		return draftDao.createDraft(userId, userBean.getCompany().getId(),
				 parentId, ConstMap.serviceTypeMap.get(serviceType));
	}

	@Override
	public InspectionBookingBean createDraftFromPreviousOrder(String userId, String orderId) throws Exception{
		String companyId = "";
		String parentId = "";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return draftDao.createDraftFromPreviousOrder(userId,companyId,parentId,orderId);
	}

	@Override
	public InspectionBookingBean getDraft(String userId, String draftId) throws Exception {
		return draftDao.getDraft(userId, draftId);
	}

	@Override
	public boolean saveDraft(String userId,InspectionBookingBean draft) throws Exception {
		return draftDao.saveDraft(userId, draft);
	}

	@Override
	public boolean addProduct(String userId,String draftId) throws Exception {
		return draftDao.addProduct(userId,draftId);
	}

	@Override
	public boolean saveProduct(String userId,InspectionProductBookingBean draftProduct) throws Exception {
		return draftDao.saveProduct(userId, draftProduct);
	}

	@Override
	public boolean deleteProduct(String userId,String productId) throws Exception {
		return draftDao.deleteProduct(userId, productId);
	}
	
	@Override
	public InspectionBookingBean calculatePricing(String userId, String draftId,
			String samplingLevel,String measurementSamplingSize) throws Exception {
		// TODO Auto-generated method stub
		UserBean userBean = userService.getCustById(userId);
		String parentId = userBean.getCompany().getParentCompanyId();
		String companyId = userBean.getCompany().getId();
		if(null == companyId) {
			companyId = "";
		}
		if(null == parentId) {
			parentId = "";
		}
		return draftDao.calculatePricing(userId,companyId,parentId,
				draftId,samplingLevel,measurementSamplingSize);
	}
}
