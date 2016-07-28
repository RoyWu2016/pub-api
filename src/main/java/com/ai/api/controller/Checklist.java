package com.ai.api.controller;

import java.util.List;

import com.ai.commons.beans.checklist.ChecklistSearchResultBean;
import org.springframework.http.ResponseEntity;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.controller
 * <p>
 * Creation Date   : 2016/7/28 10:07
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


public interface Checklist {
	ResponseEntity<List<ChecklistSearchResultBean>> searchChecklist(String userID,String keyword);
}
