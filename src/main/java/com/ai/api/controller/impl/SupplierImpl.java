package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.UserBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.controller.Supplier;
import com.ai.api.exception.AIException;
import com.ai.api.service.*;
import com.ai.api.util.RedisUtil;
import com.ai.commons.DateUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.PageBean;
import com.ai.commons.beans.audit.AuditBookingBean;
import com.ai.commons.beans.audit.api.ApiAuditOrderBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.OrderFactoryBean;
import com.ai.commons.beans.psi.api.ApiOrderFactoryBean;
import com.ai.commons.beans.psi.api.ApiOrderPriceMandayViewBean;
import com.ai.commons.beans.supplier.SupplierSearchResultBean;
import com.ai.userservice.common.util.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@Api(tags = { "Supplier" }, description = "Supplier APIs")
public class SupplierImpl implements Supplier {
	private static final Logger logger = LoggerFactory.getLogger(SupplierImpl.class);

	@Autowired
	@Qualifier("apiFactoryService")
	FactoryService factoryService;

	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;

	// @Autowired
	// ApiCallResult<JSONObject> callResult;

	@Autowired
	ParameterService parameterService;

	@Autowired
	private AuditService auditorService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/suppliers", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's Supplier List", response = SupplierSearchResultBean.class)
	public ResponseEntity<List<SupplierSearchResultBean>> getSuppliersByUserId(
			@ApiParam(required = true) @PathVariable("userId") String userId) throws IOException, AIException {
		System.out.println("get user's suppliers by userId: " + userId);

		List<FactorySearchBean> factorySearchBeenList = factoryService.getSuppliersByUserId(userId);

		if (factorySearchBeenList != null) {
			try {
				List<SupplierSearchResultBean> result = new ArrayList<SupplierSearchResultBean>();
				for (FactorySearchBean factorySearchBean : factorySearchBeenList) {
					SupplierSearchResultBean supplierSearchResultBean = new SupplierSearchResultBean();
					supplierSearchResultBean.setId(factorySearchBean.getSupplierId());
					supplierSearchResultBean.setName(factorySearchBean.getSupplierName());
					supplierSearchResultBean.setCity(factorySearchBean.getCity());
					supplierSearchResultBean.setCountry(factorySearchBean.getCountry());
					supplierSearchResultBean.setContact(factorySearchBean.getContact());
					supplierSearchResultBean.setEmail(factorySearchBean.getEmail());
					supplierSearchResultBean.setTelephone(factorySearchBean.getTelephone());
					String createdDate = factorySearchBean.getCreatedDate();
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
					Date cdate = sdf.parse(createdDate);
					supplierSearchResultBean.setCreatedDate(createdDate);
					supplierSearchResultBean.setCreatedDateUnixTimestamp(cdate.getTime());
					String updateDate = factorySearchBean.getUpdateDate();
					Date udate = sdf.parse(updateDate);
					supplierSearchResultBean.setUpdateDate(updateDate);
					supplierSearchResultBean.setUpdateDateUnixTimestamp(udate.getTime());
					result.add(supplierSearchResultBean);
				}
				return new ResponseEntity<>(result, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("", e);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/supplier/{supplierId}", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's Supplier Detail Info", response = SupplierDetailBean.class)
	public ResponseEntity<ApiCallResult> getSupplierDetailInfoById(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("supplierId") String supplierId) throws IOException, AIException {
		System.out.println("get user's supplier detail by supplierId: " + supplierId);

		ApiCallResult result = factoryService.getUserSupplierDetailInfoById(userId, supplierId);

		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/supplier/{supplierId}", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User's Supplier Detail Info", response = Boolean.class)
	public ResponseEntity<ApiCallResult> updateUserSupplierDetailInfo(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @PathVariable("supplierId") String supplierId,
			@ApiParam(required = true) @RequestBody SupplierDetailBean supplierDetailBean)
			throws IOException, AIException {
		logger.info("updating supplier detail info for user: " + userId);
		ApiCallResult callResult = new ApiCallResult();
		// Map<String, String> result = new HashMap<String,String>();
		if (factoryService.updateSupplierDetailInfo(supplierDetailBean)) {
			// result.put("success","true");
			callResult.setContent(true);
			return new ResponseEntity<>(callResult, HttpStatus.OK);
		} else {
			callResult.setContent(false);
			return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/suppliers/{supplierIds}", method = RequestMethod.DELETE)
	@ApiOperation(value = "Delete User's Suppliers Info", response = Boolean.class)
	public ResponseEntity<ApiCallResult> deleteSuppliers(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "sepearted by comma when multiple format like '123,232,334'", required = true) @PathVariable("supplierIds") String supplierIds)
			throws IOException, AIException {
		logger.info("deleting supplier for user: " + userId);
		ApiCallResult callResult = factoryService.deleteSuppliers(supplierIds);
		return new ResponseEntity<>(callResult, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/supplier", method = RequestMethod.POST)
	@ApiOperation(value = "Add User's Supplier", response = SupplierDetailBean.class)
	public ResponseEntity<ApiCallResult> createSupplier(
			@ApiParam(required = true) @PathVariable("userId") String userId,
			@ApiParam(required = true) @RequestBody SupplierDetailBean supplierDetailBean)
			throws IOException, AIException {
		if (null != supplierDetailBean && ("").equals(supplierDetailBean.getUserId())) {
			supplierDetailBean.setUserId(userId);
		}
		String supplierId = factoryService.createSupplier(supplierDetailBean);
		if (null != supplierId) {
			ApiCallResult result = factoryService.getUserSupplierDetailInfoById(userId, supplierId);
			if (null != result.getMessage()) {
				return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@RequestMapping(value = "/order/{orderId}/factory", method = RequestMethod.GET)
	@ApiOperation(value = "Get Inspection Factory Confirmation Info", response = InspectionBookingBean.class)
	public ResponseEntity<ApiCallResult> getFactoryConfirm(
			@ApiParam(required = true) @PathVariable("orderId") String orderId,
			@ApiParam(required = true) @RequestParam("password") String password) {
		logger.info("getFactoryConfirm ...");
		logger.info("orderId:" + orderId);
		ApiCallResult callResult = new ApiCallResult();
		try {
			InspectionBookingBean orderBean = orderService.getInspectionOrder("nullUserId", orderId);
			if (orderBean != null && orderBean.getOrder().getOrderGeneralInfo().getSupplierValidateCode() != null) {
				String validateCode = orderBean.getOrder().getOrderGeneralInfo().getSupplierValidateCode();
				String pw = MD5.toMD5(validateCode);

				if (pw.equalsIgnoreCase(password)) {
					JSONObject object = (JSONObject) JSON.toJSON(orderBean);

					String newPW = DigestUtils.shaHex(password);
					object.put("updateConfirmSupplierPwd", newPW);

					try {
						UserBean u = userService.getCustById(orderBean.getOrder().getOrderGeneralInfo().getUserId());
						object.put("userCompanyName", u.getCompany().getName());
						object.put("allowPostponementBySuppliers",
								u.getPreferences().getBooking().isAllowPostponementBySuppliers());
						object.put("ChinaDatetime", parameterService.getChinaTime().getDatetime());
						object.put("productCategoryList", parameterService.getProductCategoryList(false));
						object.put("productFamilyList", parameterService.getProductFamilyList(false));
					} catch (Exception e) {
						logger.error(
								"error occur while adding [userCompanyNameChinaDatetime productCategoryList productFamilyList] to result",
								e);
					}
					try {
						ApiCallResult result = factoryService.getUserSupplierDetailInfoById("nullUserId",
								orderBean.getOrder().getOrderSupplier().getSupplierId());
						String userId = orderBean.getOrder().getOrderGeneralInfo().getUserId();
						UserBean userBean = userService.getCustById(userId);
						object.getJSONObject("order").put("orderSupplier", result.getContent());
						object.getJSONObject("order").put("rates", userBean.getRate());
					} catch (Exception e) {
						logger.error("change SupplierProductLines from String to Array failed! ", e);
					}
					try {
						OrderFactoryBean factory = orderBean.getOrder().getOrderFactory();
						if (null == factory) {
							factory = factoryService
									.getOrderFactory(orderBean.getOrder().getOrderSupplier().getSupplierId());
							JSONObject objectFactory = (JSONObject) JSON.toJSON(factory);
							String str = factory.getFactoryProductLines();
							if (null != str) {
								String[] strArray = str.split(";");
								objectFactory.put("factoryProductLines", strArray);
							}
							object.getJSONObject("order").put("orderFactory", objectFactory);
						} else {
							String str = factory.getFactoryProductLines();
							if (null != str) {
								String[] strArray = str.split(";");
								object.getJSONObject("order").getJSONObject("orderFactory").put("factoryProductLines",
										strArray);
							}
						}
					} catch (Exception e) {
						logger.error("change factoryProductLines from String to Array failed! ", e);
					}
					try {
						callResult = orderService.getOrderActionEdit(orderId);
						object.put("editable", callResult.getContent());

						callResult = orderService.getOrderActionCancel(orderId);
						object.put("cancelable", callResult.getContent());
					} catch (Exception e) {
						logger.error("error occurred! getOrderAction failed", e);
						object.put("editable", null);
						object.put("cancelable", null);
					}
					callResult.setContent(object);
					return new ResponseEntity<>(callResult, HttpStatus.OK);
				}
				logger.info("incorrect pw !   [" + password + "] || should be :" + pw);
				callResult.setMessage("Incorrect password!");
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("Get order error!");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("error in getSupplierConfirm", e);
			callResult.setMessage("Internal service error.");
			e.printStackTrace();
		}
		return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@RequestMapping(value = "/order/{orderId}/audit-factory", method = RequestMethod.GET)
	@ApiOperation(value = "Get Audit Factory Confirmation Info", response = AuditBookingBean.class)
	public ResponseEntity<ApiCallResult> getAuditFactoryConfirm(
			@ApiParam(required = true) @PathVariable("orderId") String orderId,
			@ApiParam(required = true) @RequestParam("password") String password) {
		logger.info("getAuditFactoryConfirm ...");
		logger.info("orderId:" + orderId);
		ApiCallResult callResult = new ApiCallResult();
		try {
            callResult = auditorService.getOrderDetail("nullUserId", orderId);
//            ApiAuditOrderBean apiAuditOrderBean1 = JSON.toJavaObject(JSON.t(callResult.getContent()),ApiAuditOrderBean.class);
			ApiAuditOrderBean apiAuditOrderBean = (ApiAuditOrderBean) auditorService.getOrderDetail("nullUserId", orderId)
					.getContent();
			if (null != apiAuditOrderBean && null != apiAuditOrderBean.getOrderGeneralInfo().getSupplierValidateCode()) {
				String validateCode = apiAuditOrderBean.getOrderGeneralInfo().getSupplierValidateCode();
				String pw = MD5.toMD5(validateCode);

				if (pw.equalsIgnoreCase(password)) {
					JSONObject object = (JSONObject) JSON.toJSON(apiAuditOrderBean);

					String newPW = DigestUtils.shaHex(password);
					object.put("updateConfirmSupplierPwd", newPW);

					try {
						UserBean u = userService.getCustById(apiAuditOrderBean.getOrderGeneralInfo().getUserId());
						object.put("userCompanyName", u.getCompany().getName());
						object.put("allowPostponementBySuppliers",
								u.getPreferences().getBooking().isAllowPostponementBySuppliers());
						object.put("ChinaDatetime", parameterService.getChinaTime().getDatetime());
						object.put("productCategoryList", parameterService.getProductCategoryList(false));
						object.put("productFamilyList", parameterService.getProductFamilyList(false));
					} catch (Exception e) {
						logger.error(
								"error occur while adding [userCompanyNameChinaDatetime productCategoryList productFamilyList] to result",
								e);
					}
					callResult.setContent(object);
					return new ResponseEntity<>(callResult, HttpStatus.OK);
				}
				logger.info("incorrect pw !   [" + password + "] || should be :" + pw);
				callResult.setMessage("Incorrect password!");
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("Get order error!");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("error in getSupplierConfirm", e);
			callResult.setMessage("Internal service error.");
			e.printStackTrace();
		}
		return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@RequestMapping(value = "/order/{orderId}/factory", method = RequestMethod.PUT)
	@ApiOperation(value = "Confirm Inspection Factory Info", response = InspectionBookingBean.class)
	public ResponseEntity<ApiCallResult> updateFactoryConfirm(
			@ApiParam(required = true) @PathVariable("orderId") String orderId,
			@ApiParam(required = true) @RequestParam("password") String password,
			@ApiParam(value = "format like 2016-12-01", required = true) @RequestParam("inspectionDate") String inspectionDateString,
			@ApiParam(value = "format like 2016-12-01", required = true) @RequestParam("containerReadyDate") String containReadyTime,
			@ApiParam(required = true) @RequestBody OrderFactoryBean orderFactoryBean) {
		logger.info("updateSupplierConfirm ...");
		logger.info("orderId:" + orderId);
		ApiCallResult callResult = new ApiCallResult();
		String cachePassword = RedisUtil.hget("passwordCache", orderId);
		InspectionBookingBean orderBean = null;
		inspectionDateString = DateUtils.toStringWithAINewInteral(inspectionDateString);
		containReadyTime = DateUtils.toStringWithAINewInteral(containReadyTime);
		if (null == cachePassword) {
			orderBean = orderService.getInspectionOrder("nullUserId", orderId);
		}
		try {
			if (orderBean != null) {
				String validateCode = orderBean.getOrder().getOrderGeneralInfo().getSupplierValidateCode();
				String pw = DigestUtils.shaHex(MD5.toMD5(validateCode));
				if (pw.equalsIgnoreCase(password)) {
					RedisUtil.hset("passwordCache", orderId, pw, RedisUtil.HOUR * 24 * 3);
					logger.info("Password saved into redis successfully...");
					callResult = factoryService.supplierConfirmOrder(orderId, inspectionDateString, containReadyTime,
							orderFactoryBean);
					if (null == callResult.getMessage()) {
						return new ResponseEntity<>(callResult, HttpStatus.OK);
					} else {
						logger.info("failed confirming order !");
						return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				logger.info("incorrect pw !   [" + password + "] || should be :" + pw);
				callResult.setMessage("Incorrect password.");
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				if (null != cachePassword) {
					if (cachePassword.equalsIgnoreCase(password)) {
						callResult = factoryService.supplierConfirmOrder(orderId, inspectionDateString,
								containReadyTime, orderFactoryBean);
						if (null == callResult.getMessage()) {
							return new ResponseEntity<>(callResult, HttpStatus.OK);
						} else {
							logger.info("failed confirming order !");
							return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
						}
					} else {
						logger.info("incorrect pw !   [" + password + "] || should be :" + cachePassword);
						callResult.setMessage("Incorrect password.");
						return new ResponseEntity<>(callResult, HttpStatus.OK);
					}
				}
				logger.info("can not get order by id:" + orderId);
				callResult.setMessage("can not get order by id:" + orderId);
			}
		} catch (Exception e) {
			logger.error("error in updateSupplierConfirm", e);
			callResult.setMessage("Error exception: " + e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@RequestMapping(value = "/order/{orderId}/audit-factory", method = RequestMethod.PUT)
	@ApiOperation(value = "Confirm Audit Factory Info", response = AuditBookingBean.class)
	public ResponseEntity<ApiCallResult> updateAuditFactoryConfirm(
			@ApiParam(required = true) @PathVariable("orderId") String orderId,
			@ApiParam(required = true) @RequestParam("password") String password,
			@ApiParam(value = "format like 2016-12-01", required = true) @RequestParam("auditDate") String auditDate,
			@ApiParam(value = "format like 2016-12-01", required = true) @RequestParam("containerReadyDate") String containReadyTime,
			@ApiParam(required = true) @RequestBody ApiOrderFactoryBean orderFactoryBean) {
		logger.info("updateAuditFactoryConfirm ...");
		logger.info("orderId:" + orderId);
		ApiCallResult callResult = new ApiCallResult();
		String cachePassword = RedisUtil.hget("passwordCache", orderId);
		auditDate = DateUtils.toStringWithAINewInteral(auditDate);
		containReadyTime = DateUtils.toStringWithAINewInteral(containReadyTime);
		try {
			if (null == cachePassword) {
				AuditBookingBean auditBookingBean = (AuditBookingBean) auditorService
						.getOrderDetail("nullUserId", orderId).getContent();
				if (null != auditBookingBean
						&& null != auditBookingBean.getOrderGeneralInfo().getSupplierValidateCode()) {
					String validateCode = auditBookingBean.getOrderGeneralInfo().getSupplierValidateCode();
					cachePassword = DigestUtils.shaHex(MD5.toMD5(validateCode));
					RedisUtil.hset("passwordCache", orderId, cachePassword, RedisUtil.HOUR * 24 * 3);
				} else {
					logger.info("can not get order by id:" + orderId);
					callResult.setMessage("can not get order by id:" + orderId);
					return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			}
			if (cachePassword.equalsIgnoreCase(password)) {
				callResult = auditorService.supplierConfirmOrder(orderId, auditDate, containReadyTime,
						orderFactoryBean);
				if (null == callResult.getMessage()) {
					logger.info("success updateAuditFactoryConfirm!!!!");
					return new ResponseEntity<>(callResult, HttpStatus.OK);
				}
			} else {
				logger.info("incorrect pw !   [" + password + "] || should be :" + cachePassword);
				callResult.setMessage("Incorrect password.");
			}
		} catch (Exception e) {
			logger.error("error in updateAuditFactoryConfirm", e);
			callResult.setMessage("Error exception: " + e);
		}
		return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@RequestMapping(value = "/order/{orderId}/supplier", method = RequestMethod.PUT)
	@ApiOperation(value = "Update User's Supplier Info Without Login", response = InspectionBookingBean.class)
	public ResponseEntity<ApiCallResult> updateSupplierDetailInfo(
			@ApiParam(required = true) @PathVariable("orderId") String orderId,
			@ApiParam(required = true) @RequestParam("password") String password,
			@ApiParam(required = true) @RequestBody SupplierDetailBean supplierDetailBean)
			throws IOException, AIException {
		logger.info("updateUserSupplierDetailInfo orderId: " + orderId);
		ApiCallResult callResult = new ApiCallResult();
		String cachePassword = RedisUtil.hget("passwordCache", orderId);
		InspectionBookingBean orderBean = null;
		if (null == cachePassword) {
			orderBean = orderService.getInspectionOrder("nullUserId", orderId);
		}
		if (orderBean != null) {
			String validateCode = orderBean.getOrder().getOrderGeneralInfo().getSupplierValidateCode();
			String pw = DigestUtils.shaHex(MD5.toMD5(validateCode));
			if (pw.equalsIgnoreCase(password)) {
				RedisUtil.hset("passwordCache", orderId, pw, RedisUtil.HOUR * 24 * 3);
				logger.info("Password saved into redis successfully...");
				boolean b = factoryService.updateSupplierDetailInfo(supplierDetailBean);
				if (b) {
					callResult.setContent(true);
					return new ResponseEntity<>(callResult, HttpStatus.OK);
				} else {
					logger.info("updateUserSupplierDetailInfo failed.");
					callResult.setMessage("updateUserSupplierDetailInfo failed.");
					callResult.setContent(false);
					return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				logger.info("updateUserSupplierDetailInfo  password not matched!");
				callResult.setMessage("Incorrect Password.");
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			}
		} else {
			if (null != cachePassword) {
				if (cachePassword.equalsIgnoreCase(password)) {
					boolean b = factoryService.updateSupplierDetailInfo(supplierDetailBean);
					if (b) {
						callResult.setContent(true);
						return new ResponseEntity<>(callResult, HttpStatus.OK);
					} else {
						logger.info("updateUserSupplierDetailInfo failed.");
						callResult.setMessage("updateUserSupplierDetailInfo failed.");
						callResult.setContent(false);
						return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
					}
				} else {
					logger.info("incorrect pw !   [" + password + "] || should be :" + cachePassword);
					callResult.setMessage("Incorrect password.");
					return new ResponseEntity<>(callResult, HttpStatus.OK);
				}
			}
			logger.info("updateUserSupplierDetailInfo can not find by id:" + orderId);
			callResult.setMessage("updateUserSupplierDetailInfo can not find by id:" + orderId);
			return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	public static void main(String args[]){
		String str2 = "{\"orderSupplier\":{\"supplierName\":\"Test Jerome\",\"supplierAddress\":\"Cc\",\"supplierMGRNumber\":\"+1 201-555-5555\",\"supplierCountry\":\"Philippines\",\"supplierId\":\"C04C7D4CFCEEFC7848258052006C8150\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"supplierCity\":\"Cebu\",\"supplierMGRMobile\":\"+1 201-555-5555\",\"supplierMGREmail\":[\"sneha.shejwal@asiainspection.com\",\"jerome.ramos@asiainspection.com\"],\"supplierPostCode\":\"6000\",\"supplierProductLines\":[\"bigCat1_s1\",\"bigCat1_s2\",\"bigCat1_s3\",\"bigCat1_s4\",\"bigCat1_s5\",\"bigCat1_s6\",\"bigCat1_s7\"],\"supplierMGRName\":\"Jerome Ramos\"},\"orderGeneralInfo\":{\"serviceType\":\"5\",\"supplierValidateCode\":\"497421\",\"orderNumber\":\"R-Cloud-AIDEV-1703873\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"allowChangeInspectionDate\":false,\"serviceTypeText\":\"MA\",\"icneeded\":false,\"actualInspectionDateUnixTimestamp\":1489161600,\"container40\":0,\"actualInspectionDate\":\"2017-Mar-11\",\"container20\":0,\"orderPlacer\":\"sneha.shejwal@asiainspection.com\",\"expectedShipDateUnixTimestamp\":0,\"container40HQ\":0,\"bookingDateUnixTimestamp\":1489075200,\"userId\":\"48826F725B644EB1A5BEDAB48E6317E5\",\"companyId\":\"973EA05C363A4C93A07E614A65AE5F40\",\"statusText\":\"FINISHED\",\"bookingDate\":\"2017-Mar-10\",\"clientRefNb\":\"MA Ref 1003\",\"status\":\"60\"},\"orderFactory\":{\"factoryMGRMobile\":\"919970135285\",\"factoryCity\":\"29103194\",\"factoryId\":\"C04C7D4CFCEEFC7848258052006C8150\",\"factoryMGRNumber\":\"9970135285\",\"factoryName\":\"sneha\",\"factoryAddress\":\"-\",\"isMutiLocation\":false,\"factoryMGRName\":\"sneha s\"},\"draft\":{\"isAnOrderNotADraft\":true},\"orderAuditFieldsList\":[{\"fieldName\":\"Social Accountability\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes workers interviews and a short assessment of the general working conditions found in the factory.\",\"id\":\"6E657C10CDC04E38AC5512E59B5A8438\",\"fieldType\":\"OPTIONAL\",\"selected\":true,\"fieldId\":\"8E9FDED1122B47469E884532D7993075\"},{\"fieldName\":\"Procurement Conditions\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes an assessment of the raw materials and components supply as well as information on subcontractors practice.\",\"id\":\"0FAEE5C66A144AF993FE1DE018D2DD45\",\"fieldType\":\"OPTIONAL\",\"selected\":true,\"fieldId\":\"34C2B0047D5B48D197B45AAD383062AA\"},{\"fieldName\":\"R&D - Sampling Capability\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes an assessment of the capability of the factory to design and develop new products.\\n\",\"id\":\"3A514B9627974F118880138CA54AA99F\",\"fieldType\":\"OPTIONAL\",\"selected\":true,\"fieldId\":\"FD249BFED0E846F79ECCAFA9C4055F7E\"},{\"fieldName\":\"Hygiene & Security\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes an assessment of the sanitary conditions and the security level of the facilities.\\n\",\"id\":\"6370CF89F96E4113BB81730D924DC8E8\",\"fieldType\":\"OPTIONAL\",\"selected\":true,\"fieldId\":\"8703ADB95072427793DACE385D990F48\"},{\"fieldName\":\"Environment\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes a short assessment of the factory policy with environmental issues.\\n\",\"id\":\"1C783C92DB50495098C8455939C9E4BB\",\"fieldType\":\"OPTIONAL\",\"selected\":true,\"fieldId\":\"548721F08E9647789112AD7237705944\"},{\"fieldName\":\"Factory Profile\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section provides you with general information about the factory.\",\"id\":\"91055A506FF6404CB64D0378F78880AE\",\"fieldType\":\"MANDATORY\",\"selected\":true,\"fieldId\":\"45E27FBCDABF4142A33A7CA6F5926111\"},{\"fieldName\":\"Work-Flow and Organization Charts\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes a report on the workflows at the factory, as well as a chart of the factory's organization.\",\"id\":\"CC6777D389574647B385D695F54479DF\",\"fieldType\":\"MANDATORY\",\"selected\":true,\"fieldId\":\"DD341638FE624F678A8130480ED52E4E\"},{\"fieldName\":\"Production Lines-Capacity\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes a calculation chart of the overall production capacity, as well as an output check of the production lines running during the Audit.\\n\",\"id\":\"D1595F76301E470586ED9C5F0A65C07B\",\"fieldType\":\"MANDATORY\",\"selected\":true,\"fieldId\":\"5EC69BF4F7CD428484C4DBF54DF605AE\"},{\"fieldName\":\"Factory Facilities-Machinery Conditions\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes a review of the machineries and equipment tools.\\n\",\"id\":\"FE089AB5DADF4852A18BD747675BCDE1\",\"fieldType\":\"MANDATORY\",\"selected\":true,\"fieldId\":\"D7E414B00B084857B0C18BC946B71539\"},{\"fieldName\":\"Quality Assurance System\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"The section includes an assessment of the internal quality processes, QC facilities, quality team, records and quality certificates.\\n\",\"id\":\"1112783CDF53482A8B2915208D5DF134\",\"fieldType\":\"MANDATORY\",\"selected\":true,\"fieldId\":\"084C8039C52C490DA0FAA93323AF14F8\"},{\"fieldName\":\"RoHS Standards\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"description\":\"This section includes level of awareness and/or compliance of the factory regarding RoHS standards (Restriction of use of certain Hazardous Substances in Electrical and Electronic Equipment).\\n\",\"id\":\"CB5B2B0FBAC347B48A98F2342C44D856\",\"fieldType\":\"OPTIONAL\",\"selected\":true,\"fieldId\":\"9233862F14FC466ABAAF9C12E216C5CA\"},{\"specificField\":\"new ist1\",\"orderId\":\"2896524908404272936BF711F35802EE\",\"weight\":\"1\",\"id\":\"9AAA8E9680454F9C8E1FFCF15A9787B5\",\"fieldType\":\"SPECIFIC\",\"selected\":false}],\"orderPrice\":{\"orderId\":\"2896524908404272936BF711F35802EE\",\"totalMandayByClient\":2,\"measurementSampleQuantity\":0,\"sampleCollectedAndSentByAI\":false,\"optionalCharge\":300.0,\"auditNbOfMDBookedByClient\":2,\"auditUnitPrice\":649.0,\"totalQuantity\":0.0,\"sampleCollectionRequestByClient\":false,\"auditPrice\":1298.0,\"inspUnitPrice\":649.0,\"sampleSizeUnit\":\"Pcs\",\"sampleLevel\":\"II\",\"nbOfExtraReports\":0,\"measurementSampleLevel\":\"S4\",\"optionalQuantity\":6,\"totalCharge\":1727.0,\"sampleCollectionRate\":30.0,\"factoryWorkers\":\"2\",\"otherCharge\":300.0,\"inspectionCharge\":1298.0,\"fabricSampleLevel\":\"0\",\"expressBookingFee\":129.0,\"sampleSizeTotal\":0.0,\"extraReportRate\":69.0,\"nbOfContainers\":0,\"optionalUnitPrice\":50.0,\"pointsQtyToInspect\":0},\"orderExtra\":{\"cancelable\":false,\"isReInspection\":false,\"orderId\":\"2896524908404272936BF711F35802EE\",\"editable\":false},\"orderAuditGeneral\":{\"contactFactoryOrNot\":false,\"orderId\":\"2896524908404272936BF711F35802EE\",\"followSA8000Guidelines\":true,\"totalNumberOfBuilding\":1,\"factoryAreaSqm\":0,\"sampleSize\":\"2\",\"poNumber\":\"PO1003\"}}";
		ApiAuditOrderBean aa = JSON.parseObject(str2, ApiAuditOrderBean.class);
		logger.info("");
		logger.info("");

	}

}
