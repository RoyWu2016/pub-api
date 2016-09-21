package com.ai.api.service.impl;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ai.api.bean.UserBean;
import com.ai.api.bean.consts.ConstMap;
import com.ai.api.dao.DraftDao;
import com.ai.api.exception.AIException;
import com.ai.api.service.DraftService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.order.draft.DraftStepBean;
import com.ai.commons.beans.order.price.OrderPriceMandayViewBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;

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
		String companyId = "null";
		String parentId = "null";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return draftDao.deleteDraftsFromPsi(userId,companyId,parentId, draftIds);
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
		String companyId = "null";
		String parentId = "null";
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
		String companyId = "null";
		String parentId = "null";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return draftDao.getDraft(userId,companyId,parentId, draftId);
	}

	@Override
	public boolean saveDraft(String userId,InspectionBookingBean draft) throws Exception {
		String companyId = "null";
		String parentId = "null";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return draftDao.saveDraft(userId,companyId,parentId, draft);
	}

	@Override
	public InspectionProductBookingBean addProduct(String userId,String draftId) throws Exception {
		String companyId = "null";
		String parentId = "null";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return draftDao.addProduct(userId,companyId,parentId,draftId);
	}

	@Override
	public boolean saveProduct(String userId,InspectionProductBookingBean draftProduct) throws Exception {
		String companyId = "null";
		String parentId = "null";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return draftDao.saveProduct(userId,companyId,parentId,draftProduct);
	}

	@Override
	public boolean deleteProduct(String userId,String productId) throws Exception {
		String companyId = "null";
		String parentId = "null";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return draftDao.deleteProduct(userId,companyId,parentId,productId);
	}
	
	@Override
	public OrderPriceMandayViewBean calculatePricing(String userId, String draftId,
			String samplingLevel,String measurementSamplingSize) throws Exception {
		// TODO Auto-generated method stub
		UserBean userBean = userService.getCustById(userId);
		String parentId = "null";
		String companyId = "null";
		if(null != userBean) {
			parentId = userBean.getCompany().getParentCompanyId();
			if (null == parentId) {
				parentId = "";
			}
			companyId = userBean.getCompany().getId();
		}
		return draftDao.calculatePricing(userId,companyId,parentId,
				draftId,samplingLevel,measurementSamplingSize);
	}
	
	@Override
	public List<DraftOrder> searchDraft(String userId, String serviceType, String startDate, String endDate,
			String keyWord, String pageNumber, String pageSize) throws IOException, AIException {
		String companyId = "null";
		String parentId = "null";
		UserBean user = userService.getCustById(userId);
		if (null!=user){
			parentId = user.getCompany().getParentCompanyId();
			if (parentId == null) parentId = "";
			companyId = user.getCompany().getId();
		}
		return draftDao.searchDraft(userId, companyId, parentId, serviceType, startDate, endDate, keyWord, pageSize, pageNumber);
	}

	@Override
	public boolean saveDraftStep(String userId, String draftId, List<DraftStepBean> draftSteps) throws Exception {
		// TODO Auto-generated method stub
		return draftDao.saveDraftStep(userId, draftId, draftSteps);
	}
}

