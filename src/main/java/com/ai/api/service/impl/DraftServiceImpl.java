package com.ai.api.service.impl;

import java.util.HashMap;
import java.util.Map;

import com.ai.api.dao.CustomerDao;
import com.ai.api.dao.DraftDao;
import com.ai.api.service.DraftService;
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

	@Autowired
	private DraftDao draftDao;

	@Autowired
	@Qualifier("customerDao")
	private CustomerDao customerDao;

	@Override
	public boolean deleteDraft(String userId, String ids) throws Exception {
		Map<String,String> params = new HashMap<String, String>();
		String login = customerDao.getGeneralUser(userId).getLogin();
		params.put("login",login);
		params.put("ids",ids);
		return draftDao.deleteDrafts(params);
	}
}
