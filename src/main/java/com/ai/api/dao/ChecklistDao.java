package com.ai.api.dao;

import java.util.List;

import com.ai.commons.beans.checklist.ChecklistSearchCriteriaBean;
import com.ai.commons.beans.checklist.ChecklistSearchResultBean;
import com.ai.commons.beans.report.ReportSearchCriteriaBean;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.dao
 * <p>
 * Creation Date   : 2016/7/28 10:51
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


public interface ChecklistDao {
	List<ChecklistSearchResultBean> searchChecklist(ChecklistSearchCriteriaBean criteria);
	List<ChecklistSearchResultBean> searchPublicChecklist(ChecklistSearchCriteriaBean criteria);
}
