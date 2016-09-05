package com.ai.api.controller.impl;

import com.ai.api.controller.Draft;
import com.ai.api.service.DraftService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	public ResponseEntity<Boolean> deleteDrafts(@PathVariable("userId") String userId, @PathVariable("draftIds") String draftIds) {
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
	public ResponseEntity<InspectionBookingBean> createDraft(@PathVariable("userId") String userId,
	                                                       @RequestParam("serviceType") String serviceType) {
		try {
            InspectionBookingBean newDraft = draftService.createDraft(userId, serviceType);
			return new ResponseEntity<>(newDraft, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("create draft error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/previous-psi-order/{orderId}", method = RequestMethod.POST)
	public ResponseEntity<InspectionBookingBean> createDraftFromPreviousOrder(@PathVariable("userId") String userId,
																			  @PathVariable("orderId") String orderId) {
		try {
			InspectionBookingBean newDraft = draftService.createDraftFromPreviousOrder(userId, orderId);
			return new ResponseEntity<>(newDraft, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("create draft from previous order error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/{draftId}", method = RequestMethod.GET)
	public ResponseEntity<InspectionBookingBean> getDraft(@PathVariable final String userId,
	                                                    @PathVariable final String draftId) {

		try {
            InspectionBookingBean draft = draftService.getDraft(userId, draftId);
			return new ResponseEntity<>(draft, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get draft error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/{draftId}", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> saveDraft(@PathVariable("userId")String userId,
											 @PathVariable("draftId") String draftId,
											 @RequestBody InspectionBookingBean draft) {
		try {
            draft.setDraftId(draftId);
			boolean result = draftService.saveDraft(userId, draft);
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
    @RequestMapping(value = "/user/{userId}/draft/{draftId}/product", method = RequestMethod.POST)
    public ResponseEntity<Boolean> addProduct(@PathVariable("userId")String userId,
                                               @PathVariable("draftId") String draftId) {
        try {
            boolean result = draftService.addProduct(userId, draftId);
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
    @RequestMapping(value = "/user/{userId}/draft/{draftId}/product/{productId}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> saveProduct(@PathVariable("userId")String userId,
                                               @PathVariable("draftId") String draftId,
                                               @PathVariable("productId") String productId,
                                               @RequestBody InspectionProductBookingBean draftProduct) {
        try {
            draftProduct.setDraftProductId(productId);
            draftProduct.setDraftId(draftId);
            boolean result = draftService.saveProduct(userId, draftProduct);
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
    @RequestMapping(value = "/user/{userId}/draft/{draftId}/product/{productId}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteProduct(@PathVariable("userId")String userId,
                                               @PathVariable("draftId") String draftId,
                                               @PathVariable("productId") String productId) {
        try {
            boolean result = draftService.deleteProduct(userId,productId);
            if(result){
                return new ResponseEntity<>(HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
