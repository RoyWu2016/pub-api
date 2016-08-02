package com.ai.api.controller.impl;

import com.ai.api.controller.Draft;
import com.ai.api.service.DraftService;
import com.ai.commons.annotation.TokenSecured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	@RequestMapping(value = "/user/{userId}/draft/{draftIds}", method = RequestMethod.DELETE)
	public ResponseEntity<String> deleteDraft(@PathVariable("userId")String userId,@PathVariable("draftIds") String draftIds) {
		boolean b = false;
		try {
			b = draftService.deleteDraft(userId, draftIds);
		}catch (Exception e){
			e.printStackTrace();
		}
		if(b){
			return new ResponseEntity<>("delete success", HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}
}
