package com.ai.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.ai.api.bean.InspectionDraftBean;
import com.ai.api.bean.UserBean;
import com.ai.api.bean.consts.ConstMap;
import com.ai.api.dao.DraftDao;
import com.ai.api.service.DraftService;
import com.ai.api.service.UserService;
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
		String login = userService.getCustById(userId).getLogin();
		params.put("login",login);
		params.put("ids",ids);
		return draftDao.deleteDrafts(params);
	}

	@Override
	public InspectionDraftBean createDraft(String userId, String serviceType) throws Exception{

		UserBean userBean = userService.getCustById(userId);
		String parentId = userBean.getCompany().getParentCompanyId();
		if (parentId == null) parentId = "";
		return draftDao.createDraft(userId, userBean.getCompany().getId(),
				 parentId, ConstMap.serviceTypeMap.get(serviceType));
	}

	@Override
	public InspectionDraftBean getDraft(String userId, String draftId) throws Exception {
		return draftDao.getDraft(userId, draftId);
	}
}
