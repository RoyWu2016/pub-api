/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.dao;

import java.util.List;

import com.ai.api.bean.OrderSearchBean;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.dao
 *
 *  File Name       : LTOrderDao.java
 *
 *  Creation Date   : Sep 3, 2016
 *
 *  Author          : Aashish Thakran
 *
 *  Purpose         : TODO
 *
 *
 *  History         : TODO
 *
 *</PRE>
 ***************************************************************************/

public interface LTOrderDao {

    public List<OrderSearchBean> searchLTOrders(String compId, String orderStatus, String pageSize, String pageNumber, String direction);
}
