package com.ai.api.dao;

import java.util.Map;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.dao
 * <p>
 * Creation Date   : 2016/8/1 16:47
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


public interface DraftDao {

	public boolean deleteDrafts(Map<String,String> params);
}
