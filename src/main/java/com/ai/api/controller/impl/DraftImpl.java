package com.ai.api.controller.impl;

import com.ai.api.bean.InspectionDraftBean;
import com.ai.api.controller.Draft;
import com.ai.api.service.DraftService;
import com.ai.commons.annotation.TokenSecured;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Creation Date   : 2016/8/1 17:18
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
public class DraftImpl implements Draft {

	protected Logger logger = LoggerFactory.getLogger(DraftImpl.class);

	@Autowired
	private DraftService draftService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/drafts/{draftIds}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteDraftFrom(@PathVariable("userId")String userId,@PathVariable("draftIds") String draftIds) {
		try {
			boolean result = draftService.deleteDraftFromPsi(userId, draftIds);
			if(result){
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft", method = RequestMethod.POST)
	public ResponseEntity<InspectionDraftBean> createDraft(@PathVariable("userId") String userId,
	                                                       @RequestParam("serviceType") String serviceType) {
		try {
			InspectionDraftBean newDraft = draftService.createDraft(userId, serviceType);
			return new ResponseEntity<>(newDraft, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("create draft error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/{draftId}", method = RequestMethod.GET)
	public ResponseEntity<InspectionDraftBean> getDraft(@PathVariable final String userId,
	                                                    @PathVariable final String draftId) {

		try {
			InspectionDraftBean draft = draftService.getDraft(userId, draftId);
			return new ResponseEntity<>(draft, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get draft error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
