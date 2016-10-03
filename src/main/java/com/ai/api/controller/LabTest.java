/***************************************************************************
 * This document contains confidential and proprietary information
 * subject to non-disclosure agreements with AsiaInspection. This
 * information shall not be distributed or copied without written
 * permission from the AsiaInspection.
 ***************************************************************************/
package com.ai.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.ai.aims.services.model.TagTestMap;

/***************************************************************************
 *<PRE>
 *  Project Name    : api
 *
 *  Package Name    : com.ai.api.controller.impl
 *
 *  File Name       : LabTest.java
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

public interface LabTest {

	public ResponseEntity<List<TagTestMap>> searchLTTests(String userId, List<String> countryName, List<String> productCategory, List<String> keywords, Integer pageNumber, Integer pageSize);

}
