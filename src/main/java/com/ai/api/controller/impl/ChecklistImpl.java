package com.ai.api.controller.impl;

import java.util.List;

import com.ai.api.controller.Checklist;
import com.ai.api.service.ChecklistService;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.checklist.vo.CKLChecklistSearchVO;
import com.ai.commons.beans.checklist.vo.CKLChecklistVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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

	protected Logger logger = LoggerFactory.getLogger(ChecklistImpl.class);

	@Autowired
	private ChecklistService checklistService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklists", method = RequestMethod.GET)
	public ResponseEntity<List<CKLChecklistSearchVO>> searchPrivateChecklist(@PathVariable("userId") String userId,
	                                                                       @RequestParam(value = "keyword",required = false) String keyword,
																		   @RequestParam(value = "pageNumber",required = false) String pageNumber) {
		logger.info("searchChecklist ...");
		logger.info("userId :"+userId);
		logger.info("keyword :"+keyword);
		logger.info("pageNumber :"+pageNumber);
		List<CKLChecklistSearchVO> result = checklistService.searchPrivateChecklist(userId,keyword, StringUtils.isBlank(pageNumber)?0:Integer.valueOf(pageNumber));
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/public-checklists", method = RequestMethod.GET)
	public ResponseEntity<List<CKLChecklistSearchVO>> searchPublicChecklist(@PathVariable("userId") String userId,
                                                                           @RequestParam(value = "keyword",required = false) String keyword,
                                                                           @RequestParam(value = "pageNumber",required = false) String pageNumber) {
		logger.info("searchPublicChecklist ...");
		logger.info("userId :"+userId);
		logger.info("keyword :"+keyword);
        logger.info("pageNumber :"+pageNumber);
		List<CKLChecklistSearchVO> result = checklistService.searchPublicChecklist(userId, keyword,StringUtils.isBlank(pageNumber)?0:Integer.valueOf(pageNumber));
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist", method = RequestMethod.POST)
	public ResponseEntity<String> createChecklist(@PathVariable("userId") String userId,@RequestBody CKLChecklistVO checklistVO){
        logger.info("createChecklist ...");
        logger.info("userId :"+userId);
        logger.info("checklistBean :"+checklistVO.toString());
		String result = checklistService.createChecklist(userId,checklistVO);
		if(result!=null){
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}", method = RequestMethod.PUT)
	public ResponseEntity<String> updateChecklist(@PathVariable("userId") String userId,
												  @PathVariable("checklistId") String checklistId,
												  @RequestBody CKLChecklistVO checklist){
        logger.info("updateChecklist ...");
        logger.info("userId :"+userId);
        logger.info("checklistId :"+checklistId);
        logger.info("checklist :"+checklist);

        checklist.setCheckListId(checklistId);
		String result = checklistService.updateChecklist(userId,checklistId,checklist);
		if(result!=null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}", method = RequestMethod.GET)
	public ResponseEntity<CKLChecklistVO> getChecklist(@PathVariable("userId") String userId,@PathVariable("checklistId") String checklistId){
        logger.info("getChecklist ...");
        logger.info("userId :"+userId);
        logger.info("checklistId :"+checklistId);
		CKLChecklistVO result = checklistService.getChecklist(userId,checklistId);
		if(result!=null){
            return new ResponseEntity<>(result,HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklists/{checklistIds}", method = RequestMethod.DELETE)
	public ResponseEntity deleteChecklist(@PathVariable("userId") String userId,@PathVariable("checklistIds") String checklistIds){
        logger.info("deleteChecklist ...");
        logger.info("userId :"+userId);
        logger.info("checklistIds :"+checklistIds);

		boolean b = checklistService.deleteChecklist(userId,checklistIds);
        if(b){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist-name/{checklistName}", method = RequestMethod.GET)
	public ResponseEntity checklistNameExist(@PathVariable("userId") String userId,@PathVariable("checklistName") String checklistName){
        logger.info("checklistNameExist ...");
        logger.info("userId :"+userId);
        logger.info("checklistName :"+checklistName);

		boolean b = checklistService.checklistNameExist(userId,checklistName);
        if(b){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}/feedback", method = RequestMethod.PUT)
	public ResponseEntity saveFeedback(@PathVariable("userId") String userId, @PathVariable("checklistId") String checklistId,@RequestBody String feedback){
        logger.info("saveFeedback ...");
        logger.info("userId :"+userId);
        logger.info("checklistId :"+checklistId);
        logger.info("feedback :"+feedback);
		boolean b = checklistService.saveFeedback(userId,checklistId,feedback);
        if(b){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/checklist/{checklistId}/approved", method = RequestMethod.PUT)
	public ResponseEntity approved(@PathVariable("userId") String userId,@PathVariable("checklistId") String checklistId){
        logger.info("approved ...");
        logger.info("userId :"+userId);
        logger.info("checklistId :"+checklistId);
		boolean b = checklistService.approved(userId,checklistId);
		if(b){
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
