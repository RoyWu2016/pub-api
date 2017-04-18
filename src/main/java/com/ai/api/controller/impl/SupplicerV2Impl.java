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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.UserBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.controller.SupplicerV2;
import com.ai.api.exception.AIException;
import com.ai.api.service.APIFileService;
import com.ai.api.service.AuditService;
import com.ai.api.service.FactoryService;
import com.ai.api.service.OrderService;
import com.ai.api.service.ParameterService;
import com.ai.api.service.UserService;
import com.ai.api.util.RedisUtil;
import com.ai.commons.DateUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.audit.AuditBookingBean;
import com.ai.commons.beans.audit.api.ApiAuditOrderBean;
import com.ai.commons.beans.audit.api.ApiAuditSupplierConfirmInfoBean;
import com.ai.commons.beans.fileservice.ApiFileMetaBean;
import com.ai.commons.beans.fileservice.FileMetaBean;
import com.ai.commons.beans.params.api.ApiProductCategoryBean;
import com.ai.commons.beans.params.api.ApiProductFamilyBean;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.commons.beans.psi.OrderFactoryBean;
import com.ai.commons.beans.psi.api.ApiOrderFactoryBean;
import com.ai.commons.beans.supplier.SupplierSearchResultBean;
import com.ai.userservice.common.util.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@Api(tags = { "Supplier V2" }, description = "Supplier V2 APIs")
public class SupplicerV2Impl implements SupplicerV2 {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("apiFactoryService")
	FactoryService factoryService;
	
	@Autowired
	OrderService orderService;

	@Autowired
	UserService userService;

	@Autowired
	private APIFileService myFileService;

	@Autowired
	ParameterService parameterService;

	@Autowired
	private AuditService auditorService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/suppliers", method = RequestMethod.GET)
	@ApiOperation(value = "Get User's Supplier List", response = SupplierSearchResultBean.class)
	public ResponseEntity<ApiCallResult> getSuppliersByUserId(
			@ApiParam(required = true) @PathVariable("userId") String userId) throws IOException, AIException {
		// TODO Auto-generated method stub
		List<FactorySearchBean> factorySearchBeenList = factoryService.getSuppliersByUserId(userId);
		ApiCallResult rest = new ApiCallResult();
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
				rest.setContent(result);
				return new ResponseEntity<>(rest, HttpStatus.OK);
			} catch (Exception e) {
				logger.error("", e);
				rest.setMessage("getSuppliersByUserId error: " + e.toString());
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(rest, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/v2/{userId}/supplier/{supplierId}", method = RequestMethod.GET)
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
	@RequestMapping(value = "/user/v2/{userId}/supplier/{supplierId}", method = RequestMethod.PUT)
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
	@RequestMapping(value = "/user/v2/{userId}/suppliers/{supplierIds}", method = RequestMethod.DELETE)
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
	@RequestMapping(value = "/user/v2/{userId}/supplier", method = RequestMethod.POST)
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
	@RequestMapping(value = "/order/v2/{orderId}/factory", method = RequestMethod.GET)
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
	@RequestMapping(value = "/order/v2/{orderId}/audit-factory", method = RequestMethod.GET)
	@ApiOperation(value = "Get Audit Factory Confirmation Info", response = ApiAuditSupplierConfirmInfoBean.class)
	public ResponseEntity<ApiCallResult> getAuditFactoryConfirm(
			@ApiParam(required = true) @PathVariable("orderId") String orderId,
			@ApiParam(required = true) @RequestParam("password") String password) {
		logger.info("getAuditFactoryConfirm ...");
		logger.info("orderId:" + orderId);
		ApiCallResult callResult = new ApiCallResult();
		ApiAuditSupplierConfirmInfoBean auditSupplierBean = new ApiAuditSupplierConfirmInfoBean();
		try {
			ApiAuditOrderBean apiAuditOrderBean = JSON.toJavaObject(
					(JSONObject) auditorService.getOrderDetail("nullUserId", orderId).getContent(),
					ApiAuditOrderBean.class);
			if (null != apiAuditOrderBean
					&& null != apiAuditOrderBean.getOrderGeneralInfo().getSupplierValidateCode()) {
				String validateCode = apiAuditOrderBean.getOrderGeneralInfo().getSupplierValidateCode();
				String pw = MD5.toMD5(validateCode);

				if (pw.equalsIgnoreCase(password)) {
					auditSupplierBean.setDraft(apiAuditOrderBean.getDraft());
					auditSupplierBean.setOrderExtra(apiAuditOrderBean.getOrderExtra());
					auditSupplierBean.setOrderAuditFieldsList(apiAuditOrderBean.getOrderAuditFieldsList());
					auditSupplierBean.setOrderAuditGeneral(apiAuditOrderBean.getOrderAuditGeneral());
					auditSupplierBean.setOrderFactory(apiAuditOrderBean.getOrderFactory());
					auditSupplierBean.setOrderGeneralInfo(apiAuditOrderBean.getOrderGeneralInfo());
					auditSupplierBean.setOrderPrice(apiAuditOrderBean.getOrderPrice());
					auditSupplierBean.setOrderSupplier(apiAuditOrderBean.getOrderSupplier());
					UserBean user = userService.getCustById(auditSupplierBean.getOrderGeneralInfo().getUserId());
					if (null != user && null != user.getRate()) {
						auditSupplierBean.setExpressFee(String.valueOf(user.getRate().getExpressFee()));
					}

					List<FileMetaBean> fileList = myFileService.getFileService().getFileInfoBySrcIdAndFileType(orderId,
							"ORDER_ATT");
					if (null != fileList) {
						List<ApiFileMetaBean> attachmentsList = new ArrayList<ApiFileMetaBean>();
						for (FileMetaBean each : fileList) {
							ApiFileMetaBean bean = new ApiFileMetaBean();
							bean.setFileName(each.getFileName());
							bean.setFileSize(each.getFileSize());
							bean.setFileType(each.getFileType());
							bean.setId(each.getId());
							bean.setSrcId(each.getSrcId());

							attachmentsList.add(bean);
						}
						auditSupplierBean.setAttachmentsList(attachmentsList);
					}
					String newPW = DigestUtils.shaHex(password);
					auditSupplierBean.setUpdateConfirmSupplierPwd(newPW);

					try {
						UserBean u = userService.getCustById(apiAuditOrderBean.getOrderGeneralInfo().getUserId());
						auditSupplierBean.setUserCompanyName(u.getCompany().getName());
						auditSupplierBean.setAllowPostponementBySuppliers(
								u.getPreferences().getBooking().isAllowPostponementBySuppliers());
						auditSupplierBean.setChinaDatetime(parameterService.getChinaTime().getDatetime());
						List<ProductCategoryDtoBean> category = parameterService.getProductCategoryList(false);
						if (null != category) {
							List<ApiProductCategoryBean> list = new ArrayList<ApiProductCategoryBean>();
							for (ProductCategoryDtoBean each : category) {
								ApiProductCategoryBean bean = new ApiProductCategoryBean();
								bean.setId(each.getId());
								bean.setName(each.getName());

								list.add(bean);
							}
							auditSupplierBean.setProductCategoryList(list);
						}
						List<ProductFamilyDtoBean> family = parameterService.getProductFamilyList(false);
						if (null != family) {
							List<ApiProductFamilyBean> productFamilyList = new ArrayList<ApiProductFamilyBean>();
							for (ProductFamilyDtoBean each : family) {
								ApiProductFamilyBean bean = new ApiProductFamilyBean();
								bean.setCategoryId(each.getCategoryId());
								bean.setCategoryName(each.getCategoryName());
								bean.setId(each.getId());
								bean.setName(each.getName());

								productFamilyList.add(bean);
							}
							auditSupplierBean.setProductFamilyList(productFamilyList);
						}
					} catch (Exception e) {
						logger.error(
								"error occur while adding [userCompanyNameChinaDatetime productCategoryList productFamilyList] to result",
								e);
					}
					callResult.setContent(auditSupplierBean);
					return new ResponseEntity<>(callResult, HttpStatus.OK);
				} else {
					logger.info("incorrect pw !   [" + password + "] || should be :" + pw);
					callResult.setMessage("Incorrect password!");
					return new ResponseEntity<>(callResult, HttpStatus.OK);
				}
			} else {
				callResult.setMessage("Get order error!");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("error in getSupplierConfirm", e);
			callResult.setMessage("Internal service error." + e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@RequestMapping(value = "/order/v2/{orderId}/factory", method = RequestMethod.PUT)
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
	@RequestMapping(value = "/order/v2/{orderId}/audit-factory", method = RequestMethod.PUT)
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
				ApiCallResult temp = auditorService.getOrderDetail("nullUserId", orderId);
				String jsonStr = JSON.toJSONString(temp.getContent());
				if (null != jsonStr) {
					ApiAuditOrderBean auditBookingBean = JSON.parseObject(jsonStr, ApiAuditOrderBean.class);
					if (null != auditBookingBean && null != auditBookingBean.getOrderGeneralInfo()
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
	@RequestMapping(value = "/order/v2/{orderId}/supplier", method = RequestMethod.PUT)
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

}
