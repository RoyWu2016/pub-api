package com.ai.api.controller.impl;

import java.util.List;

import com.ai.api.controller.Checklist;
import com.ai.api.service.ChecklistService;
import com.ai.commons.beans.checklist.ChecklistSearchResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.controller.impl
 * <p>
 * Creation Date   : 2016/7/28 10:13
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


@RestController
public class ChecklistImpl implements Checklist {

	@Autowired
	private ChecklistService checklistService;

	@Override
	@RequestMapping(value = "/user/{userId}/checklist", method = RequestMethod.GET)
	public ResponseEntity<List<ChecklistSearchResultBean>> searchChecklist(@PathVariable("userId") String userID,
	                                                                       @RequestParam(value = "keyword",required = false) String keyword) {
		List<ChecklistSearchResultBean> result = checklistService.searchChecklist(userID,keyword);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
