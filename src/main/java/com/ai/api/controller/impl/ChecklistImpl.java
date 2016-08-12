package com.ai.api.controller.impl;

import java.util.List;

import com.ai.api.controller.Checklist;
import com.ai.api.service.ChecklistService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.checklist.api.ChecklistDetailBean;
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
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklists", method = RequestMethod.GET)
	public ResponseEntity<List<ChecklistSearchResultBean>> searchChecklist(@PathVariable("userId") String userID,
	                                                                       @RequestParam(value = "keyword",required = false) String keyword,
																		   @RequestParam(value = "pageNumber",required = false) Integer pageNumber) {

		List<ChecklistSearchResultBean> result = checklistService.searchChecklist(userID,keyword,pageNumber);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/publicChecklists", method = RequestMethod.GET)
	public ResponseEntity<List<ChecklistSearchResultBean>> searchPublicChecklist(@PathVariable("userId") String userId,
	                                                                             @RequestParam(value = "keyword",required = false) String keyword) {
		List<ChecklistSearchResultBean> result = checklistService.searchPublicChecklist(userId, keyword);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist", method = RequestMethod.POST)
	public ResponseEntity<String> createChecklist(@PathVariable("userId") String userId,ChecklistDetailBean checklistDetailBean){
		String result = checklistService.createChecklist(userId,checklistDetailBean);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateChecklist(@PathVariable("userId") String userId,@PathVariable("checklistId") String checklistId,ChecklistDetailBean checklistDetailBean){
		checklistDetailBean.setId(checklistId);
		String result = checklistService.updateChecklist(userId,checklistDetailBean);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}", method = RequestMethod.GET)
	public ResponseEntity<ChecklistDetailBean> getChecklist(@PathVariable("userId") String userId,@PathVariable("checklistId") String checklistId){
		ChecklistDetailBean result = checklistService.getChecklist(userId,checklistId);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklists/{checklistIds}", method = RequestMethod.DELETE)
	public ResponseEntity deleteChecklist(@PathVariable("userId") String userId,@PathVariable("checklistIds") String checklistIds){

		 boolean b = checklistService.deleteChecklist(userId,checklistIds);
		if(b){
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}