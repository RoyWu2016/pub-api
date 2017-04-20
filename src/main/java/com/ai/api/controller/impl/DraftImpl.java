
package com.ai.api.controller.impl;

import java.util.Date;
import java.util.List;

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

import com.ai.api.controller.Draft;
import com.ai.api.service.DraftService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.audit.AuditBookingBean;
import com.ai.commons.beans.order.draft.DraftOrder;
import com.ai.commons.beans.order.price.OrderPriceMandayViewBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.InspectionProductBookingBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
@Api(tags = { "Draft" }, description = "Draft booking APIs")
public class DraftImpl implements Draft {

	protected Logger logger = LoggerFactory.getLogger(DraftImpl.class);

	@Autowired
	private DraftService draftService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/drafts/{draftIds}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete Draft API", response = Boolean.class)
	public ResponseEntity<Boolean> deleteDrafts(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("draftIds") String draftIds) {
		try {
			logger.info("deleteDrafts. . .");
			boolean result = draftService.deleteDraftFromPsi(userId, draftIds);
			if (result) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("delete draft error: " + ExceptionUtils.getFullStackTrace(e));
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft", method = RequestMethod.POST)
	@ApiOperation(value = "Create Draft API", response = InspectionBookingBean.class)
	public ResponseEntity<InspectionBookingBean> createDraft(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @RequestParam(value = "serviceType", required = true) String serviceType) {
		if (userId == null || userId.isEmpty()) {
			logger.error("userId:" + userId);
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			InspectionBookingBean newDraft = draftService.createDraft(userId, serviceType);
			if (null != newDraft) {
				return new ResponseEntity<>(newDraft, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("create draft error: " + ExceptionUtils.getFullStackTrace(e));
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/previous-psi-order/{orderId}", method = RequestMethod.POST)
	@ApiOperation(value = "Create Draft From Previous Order API", response = InspectionBookingBean.class)
	public ResponseEntity<InspectionBookingBean> createDraftFromPreviousOrder(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("orderId") String orderId,
			@ApiParam(required = false) @RequestParam(value = "service-type", required = false) String serviceType) {
		try {
			InspectionBookingBean newDraft = draftService.createDraftFromPreviousOrder(userId, orderId,
					serviceType.toLowerCase());
			if (null != newDraft) {
				return new ResponseEntity<>(newDraft, HttpStatus.OK);
			}
		} catch (Exception e) {
			logger.error("create draft from previous order error: " + ExceptionUtils.getFullStackTrace(e));
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/{draftId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get User Draft API", response = InspectionBookingBean.class)
	public ResponseEntity<InspectionBookingBean> getDraft(
			@ApiParam(required = true, name = "userId") @PathVariable final String userId,
			@ApiParam(required = true, name = "draftId") @PathVariable final String draftId) {

		if (userId == null || userId.isEmpty() || draftId == null || draftId.isEmpty()) {
			logger.error("userId:" + userId + ", draftId:" + draftId + " can't be null!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			InspectionBookingBean draft = draftService.getDraft(userId, draftId);
			if (null != draft) {
				draft.getOrder().getOrderGeneralInfo().setBookingDate(new Date());
				return new ResponseEntity<>(draft, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
		} catch (Exception e) {
			logger.error("get draft error: " + ExceptionUtils.getFullStackTrace(e));
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/{draftId}", method = RequestMethod.PUT)
	@ApiOperation(value = "Save User Draft API", response = Boolean.class)
	public ResponseEntity<Boolean> saveDraft(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("draftId") String draftId,
			@ApiParam(required = true) @RequestBody InspectionBookingBean draft) {
		if (userId == null || userId.isEmpty() || draftId == null || draftId.isEmpty() || draft == null) {
			logger.error("userId:" + userId + ", draftId:" + draftId + ", draft:" + draft + " can't be null!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			draft.setDraftId(draftId);
			boolean result = draftService.saveDraft(userId, draft);
			if (result) {
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
	@ApiOperation(value = "Add Product API", response = InspectionProductBookingBean.class)
	public ResponseEntity<InspectionProductBookingBean> addProduct(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("draftId") String draftId) {
		try {
			InspectionProductBookingBean product = draftService.addProduct(userId, draftId);
			if (null != product) {
				return new ResponseEntity<>(product, HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/{draftId}/product/{productId}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete Product API", response = Boolean.class)
	public ResponseEntity<Boolean> deleteProduct(@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("draftId") String draftId,
			@ApiParam(required = true) @PathVariable("productId") String productId) {
		try {
			boolean result = draftService.deleteProduct(userId, productId);
			if (result) {
				return new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/draft/{draftId}/sampling-level/{samplingLevel}/price", method = RequestMethod.GET)
	@ApiOperation(value = "Calculate Pricing API", response = OrderPriceMandayViewBean.class)
	public ResponseEntity<OrderPriceMandayViewBean> calculatePricing(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("draftId") String draftId,
			@ApiParam(required = true) @PathVariable("samplingLevel") String samplingLevel,
			@ApiParam(required = false) @RequestParam(value = "measurementSamplingSize", required = false, defaultValue = "") String measurementSamplingSize) {
		if (userId == null || draftId == null || samplingLevel == null) {
			logger.error("userId:" + userId + ", draftId:" + draftId + ", sample level:" + samplingLevel
					+ " can't be null!");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		try {
			OrderPriceMandayViewBean newDraft = draftService.calculatePricing(userId, draftId, samplingLevel,
					measurementSamplingSize);
			if (null != newDraft) {
				return new ResponseEntity<>(newDraft, HttpStatus.OK);
			} else {
				logger.error("can't get price for draft " + draftId);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("calculate Pricing error: " + ExceptionUtils.getFullStackTrace(e));
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/psi-drafts", method = RequestMethod.GET)
	@ApiOperation(value = "Search User Drafts API", response = DraftOrder.class)
	public ResponseEntity<List<DraftOrder>> searchDraft(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = false) @RequestParam(value = "service-type", required = false,defaultValue = "1,2,3,4,6") String serviceType,
			@ApiParam(required = false) @RequestParam(value = "start", required = false, defaultValue = "") String startDate,
			@ApiParam(required = false) @RequestParam(value = "end", required = false, defaultValue = "") String endDate,
			@ApiParam(required = false) @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword,
			@ApiParam(required = false) @RequestParam(value = "page", required = false, defaultValue = "1") String pageNumber,
			@ApiParam(required = false) @RequestParam(value = "page-size", required = false, defaultValue = "20") String pageSize) {
		try {
			List<DraftOrder> draftList = draftService.searchDraft(userId, serviceType, startDate, endDate, keyword,
					pageNumber, pageSize);
			// if not data found, just return 200 with empty list
			return new ResponseEntity<>(draftList, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("get draft search error: " + ExceptionUtils.getFullStackTrace(e));
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

	}

	/*
	 * @Override
	 * 
	 * @TokenSecured
	 * 
	 * @RequestMapping(value = "/user/{userId}/draft/{draftId}/products", method
	 * = RequestMethod.PUT) public ResponseEntity<Boolean> saveProducts(
	 * 
	 * @PathVariable("userId") String userId,
	 * 
	 * @PathVariable("draftId") String draftId,
	 * 
	 * @RequestBody InspectionDraftProductsAndStepBean
	 * inspectionDraftProductsAndStepBean) { try { logger.
	 * info("Checking what non-duplicate product id should be delete before update multiple products: "
	 * ); InspectionBookingBean draft = draftService.getDraft(userId, draftId);
	 * if (null == draft) { return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
	 * List<InspectionProductBookingBean> list = draft.getProducts(); if
	 * (list.size() >
	 * inspectionDraftProductsAndStepBean.getDraftProductsList().size()) {
	 * 
	 * List<String> needTodeleteProductIdList = new ArrayList<String>();
	 * List<String> inputProductIdList = new ArrayList<String>(); for
	 * (InspectionProductBookingBean each : list) {
	 * needTodeleteProductIdList.add(each.getDraftProductId()); } for
	 * (InspectionProductBookingBean each :
	 * inspectionDraftProductsAndStepBean.getDraftProductsList()) {
	 * inputProductIdList.add(each.getDraftProductId()); }
	 * needTodeleteProductIdList.removeAll(inputProductIdList);
	 * 
	 * for (String id : needTodeleteProductIdList) {
	 * logger.debug("Deleting the product id: " + id); if
	 * (!draftService.deleteProduct(userId, id)) { return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); } } } } catch
	 * (Exception e) { e.printStackTrace(); }
	 * 
	 * for (InspectionProductBookingBean each :
	 * inspectionDraftProductsAndStepBean.getDraftProductsList()) { try {
	 * boolean result = draftService.saveProduct(userId, each); if (!result) {
	 * return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); } } catch
	 * (Exception e) { e.printStackTrace(); } } try { boolean result =
	 * draftService.saveDraftStep(userId, draftId,
	 * inspectionDraftProductsAndStepBean.getDraftSteps()); if (!result) {
	 * return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); } } catch
	 * (Exception e) { e.printStackTrace(); } return new
	 * ResponseEntity<>(HttpStatus.OK); }
	 * 
	 * @Override
	 * 
	 * @TokenSecured
	 * 
	 * @RequestMapping(value = "/user/{userId}/draft/{draftId}/step", method =
	 * RequestMethod.PUT) public ResponseEntity<Boolean> saveDraftStep(
	 * 
	 * @PathVariable("userId") String userId,
	 * 
	 * @PathVariable("draftId") String draftId,
	 * 
	 * @RequestBody List<DraftStepBean> draftSteps) { try { boolean result =
	 * draftService.saveDraftStep(userId, draftId, draftSteps); if (result) {
	 * return new ResponseEntity<>(HttpStatus.OK); } else { return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); } } catch (Exception
	 * e) { e.printStackTrace(); } return null; }
	 * 
	 * @Override
	 * 
	 * @TokenSecured
	 * 
	 * @RequestMapping(value =
	 * "/user/{userId}/draft/{draftId}/product/{productId}", method =
	 * RequestMethod.PUT) public ResponseEntity<Boolean>
	 * saveProduct(@PathVariable("userId") String userId,
	 * 
	 * @PathVariable("draftId") String draftId,
	 * 
	 * @PathVariable("productId") String productId,
	 * 
	 * @RequestBody InspectionProductBookingBean draftProduct) { try {
	 * draftProduct.setDraftProductId(productId);
	 * draftProduct.setDraftId(draftId); boolean result =
	 * draftService.saveProduct(userId, draftProduct); if (result) { return new
	 * ResponseEntity<>(HttpStatus.OK); } } catch (Exception e) {
	 * e.printStackTrace(); } return new
	 * ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); }
	 */

}
