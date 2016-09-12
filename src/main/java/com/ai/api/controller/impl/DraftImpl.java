
package com.ai.api.controller.impl;

import java.util.List;

import com.ai.api.bean.InspectionDraftProductBean;
import com.ai.api.controller.Draft;
import com.ai.api.service.DraftService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;
import org.apache.commons.lang.exception.ExceptionUtils;
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
    
	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/{draftId}/sampling-level/{samplingLevel}/price", 
			method = RequestMethod.GET)
	public ResponseEntity<InspectionBookingBean> calculatePricing(
			@PathVariable("userId") String userId,
			@PathVariable("draftId") String draftId, 
			@PathVariable("samplingLevel") String samplingLevel,
			@RequestParam("measurementSamplingSize") String measurementSamplingSize) {
		// TODO Auto-generated method stub
		try {
			InspectionBookingBean newDraft = draftService.calculatePricing(userId, draftId,samplingLevel, measurementSamplingSize);
			return new ResponseEntity<>(newDraft, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("calculate Pricing error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	 	@Override
	    @TokenSecured
	    @RequestMapping(value = "/user/{userId}/psi-drafts", method = RequestMethod.GET)
		public ResponseEntity<List<DraftOrder>> searchDraft(@PathVariable("userId")String userId,
				 					@RequestParam(value = "service-type", required = false ,defaultValue="") String serviceType,
				 					@RequestParam(value = "start", required = false ,defaultValue="") String startDate,
				 					@RequestParam(value = "end", required = false ,defaultValue="") String endDate,
				 					@RequestParam(value = "keyword", required = false ,defaultValue="") String keyword,
				 					@RequestParam(value = "page", required = false ,defaultValue="1") String pageNumber,
				 					@RequestParam(value = "page-size", required = false ,defaultValue="20") String pageSize) {
	  
			try {
				List<DraftOrder> draftList = draftService.searchDraft(userId, serviceType, startDate, endDate, keyword, pageNumber, pageSize);
				return new ResponseEntity<List<DraftOrder>>(draftList, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("get draft search error: " + ExceptionUtils.getFullStackTrace(e));
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}

		@Override
	    @TokenSecured
	    @RequestMapping(value = "/user/{userId}/draft/{draftId}/products", method = RequestMethod.PUT)
		public ResponseEntity<Boolean> saveProducts(
				@PathVariable("userId") String userId,
				@PathVariable("draftId") String draftId,
				@RequestBody List<InspectionProductBookingBean> draftProductsList) {
			// TODO Auto-generated method stub
			for(InspectionProductBookingBean each : draftProductsList) {
				try {
					boolean result = draftService.saveProduct(userId, each);
					if(!result){
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return new ResponseEntity<>(HttpStatus.OK);
		}
}

 