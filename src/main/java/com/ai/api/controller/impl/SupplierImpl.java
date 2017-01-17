package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.commons.codec.digest.DigestUtils;
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

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.UserBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.controller.Supplier;
import com.ai.api.exception.AIException;
import com.ai.api.service.FactoryService;
import com.ai.api.service.OrderService;
import com.ai.api.service.ParameterService;
import com.ai.api.service.UserService;
import com.ai.api.util.RedisUtil;
import com.ai.commons.DateUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.OrderFactoryBean;
import com.ai.commons.beans.supplier.SupplierSearchResultBean;
import com.ai.userservice.common.util.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
public class SupplierImpl implements Supplier {
	private static final Logger logger = LoggerFactory.getLogger(SupplierImpl.class);

	@Autowired
	FactoryService factoryService;

	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;

	// @Autowired
	// ApiCallResult<JSONObject> callResult;

	@Autowired
	ParameterService parameterService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/suppliers", method = RequestMethod.GET)
	public ResponseEntity<List<SupplierSearchResultBean>> getUserSupplierById(@PathVariable("userId") String userId)
			throws IOException, AIException {
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
	public ResponseEntity<SupplierDetailBean> getUserSupplierDetailInfoById(@PathVariable("userId") String userId,
			@PathVariable("supplierId") String supplierId) throws IOException, AIException {
		System.out.println("get user's supplier detail by supplierId: " + supplierId);

		SupplierDetailBean result = factoryService.getUserSupplierDetailInfoById(userId, supplierId);

		if (result != null) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/supplier/{supplierId}", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> updateUserSupplierDetailInfo(@PathVariable("userId") String userId,
			@PathVariable("supplierId") String supplierId, @RequestBody SupplierDetailBean supplierDetailBean)
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
	public ResponseEntity<ApiCallResult> deleteSuppliers(@PathVariable("userId") String userId,
			@PathVariable("supplierIds") String supplierIds) throws IOException, AIException {
		logger.info("deleting supplier for user: " + userId);
		ApiCallResult callResult = factoryService.deleteSuppliers(supplierIds);
		return new ResponseEntity<>(callResult, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/supplier", method = RequestMethod.POST)
	public ResponseEntity<SupplierDetailBean> createSupplier(@PathVariable("userId") String userId,
			@RequestBody SupplierDetailBean supplierDetailBean) throws IOException, AIException {
		if (null != supplierDetailBean && ("").equals(supplierDetailBean.getUserId())) {
			supplierDetailBean.setUserId(userId);
		}
		String supplierId = factoryService.createSupplier(supplierDetailBean);
		if (null != supplierId) {
			SupplierDetailBean result = factoryService.getUserSupplierDetailInfoById(userId, supplierId);
			if (null == result) {
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			} else {
				return new ResponseEntity<>(result, HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@RequestMapping(value = "/order/{orderId}/factory", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getFactoryConfirm(@PathVariable("orderId") String orderId,
			@RequestParam("password") String password) {
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

						object.put("ChinaDatetime", parameterService.getChinaTime().getDatetime());
						object.put("productCategoryList", parameterService.getProductCategoryList(false));
						object.put("productFamilyList", parameterService.getProductFamilyList(false));
					} catch (Exception e) {
						logger.error(
								"error occur while adding [userCompanyNameChinaDatetime productCategoryList productFamilyList] to result",
								e);
					}
					try {
						SupplierDetailBean result = factoryService.getUserSupplierDetailInfoById("nullUserId",
								orderBean.getOrder().getOrderSupplier().getSupplierId());
						String userId = orderBean.getOrder().getOrderGeneralInfo().getUserId();
						UserBean userBean = userService.getCustById(userId);
						object.getJSONObject("order").put("orderSupplier", result);
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
								objectFactory.put("factoryProductLines",strArray);
							}
							object.getJSONObject("order").put("orderFactory", objectFactory);
						}else {
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
	@RequestMapping(value = "/order/{orderId}/factory", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> updateFactoryConfirm(@PathVariable("orderId") String orderId,
			@RequestParam("password") String password, @RequestParam("inspectionDate") String inspectionDateString,
			@RequestParam("containerReadyDate") String containReadyTime,
			@RequestBody OrderFactoryBean orderFactoryBean) {
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
	@RequestMapping(value = "/order/{orderId}/supplier", method = RequestMethod.PUT)
	public ResponseEntity<ApiCallResult> updateSupplierDetailInfo(@PathVariable("orderId") String orderId,
			@RequestParam("password") String password, @RequestBody SupplierDetailBean supplierDetailBean)
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

}
