package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ai.api.bean.UserBean;
import com.ai.api.service.OrderService;
import com.ai.api.service.ParameterService;
import com.ai.api.service.UserService;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.psi.InspectionBookingBean;
import com.ai.userservice.common.util.MD5;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
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
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.controller.Supplier;
import com.ai.api.exception.AIException;
import com.ai.api.service.FactoryService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.psi.OrderFactoryBean;
import com.ai.commons.beans.supplier.SupplierSearchResultBean;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
@RestController
public class SupplierImpl implements Supplier {
	private static final Logger logger = LoggerFactory.getLogger(SupplierImpl.class);

	@Autowired
	FactoryService factoryService;

    @Autowired
    OrderService orderService;

	@Autowired
	UserService userService;

	@Autowired
	ApiCallResult callResult;

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
	public ResponseEntity<Map<String, String>> updateUserSupplierDetailInfo(@PathVariable("userId") String userId,
			@PathVariable("supplierId") String supplierId, @RequestBody SupplierDetailBean supplierDetailBean)
			throws IOException, AIException {
		System.out.println("updating supplier detail info for user: " + userId);
		// Map<String, String> result = new HashMap<String,String>();
		if (factoryService.updateSupplierDetailInfo(supplierDetailBean)) {
			// result.put("success","true");
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/suppliers/{supplierIds}", method = RequestMethod.DELETE)
	public ResponseEntity<Boolean> deleteSuppliers(@PathVariable("userId") String userId,
			@PathVariable("supplierIds") String supplierIds) throws IOException, AIException {
		System.out.println("deleting supplier for user: " + userId);
		if (factoryService.deleteSuppliers(supplierIds)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
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
	@RequestMapping(value = "/order/{orderId}/supplier", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getSupplierConfirm(@PathVariable("orderId") String orderId,
																  @RequestParam("password")String password) {
		try {
			logger.info("getSupplierConfirm ...");
			logger.info("orderId:"+orderId);
			InspectionBookingBean orderBean = orderService.getOrderDetail("nullUserId", orderId);
			if (orderBean != null && orderBean.getOrder().getOrderGeneralInfo().getSupplierValidateCode() != null) {
				String validateCode = orderBean.getOrder().getOrderGeneralInfo().getSupplierValidateCode();
                String pw = MD5.toMD5(validateCode);
                if (pw.equalsIgnoreCase(password)){
                    String newPW = DigestUtils.shaHex(password);
                    JSONObject object = JSON.parseObject(JSON.toJSONString(orderBean, SerializerFeature.WriteMapNullValue));
                    object.put("updateConfirmSupplierPwd",newPW);

                    UserBean u = userService.getCustById(orderBean.getOrder().getOrderGeneralInfo().getUserId());
                    object.put("userCompanyName",u.getCompany().getName());

                    object.put("ChinaDatetime",parameterService.getChinaTime().getDatetime());

					callResult.setContent(object.toJSONString());
                    return new ResponseEntity<>(callResult, HttpStatus.OK);
                }
                logger.info("incorrect pw !   ["+ password +"] || should be :"+pw);
			} else {
				callResult.setMessage("Get supplier confirm error!");
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} catch (Exception e) {
			logger.error("error in getSupplierConfirm",e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Override
	@RequestMapping(value = "/order/{orderId}/supplier", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> updateSupplierConfirm(@PathVariable("orderId") String orderId,
															@RequestParam("password")String password,
															@RequestParam("inspectionDate") String inspectionDateString,
															@RequestParam("containerReadyDate") String containReadyTime,
															@RequestBody OrderFactoryBean orderFactoryBean) {
		try {
			logger.info("updateSupplierConfirm ...");
			logger.info("orderId:"+orderId);
			InspectionBookingBean orderBean = orderService.getOrderDetail("nullUserId", orderId);
			if (orderBean != null) {
				String validateCode = orderBean.getOrder().getOrderGeneralInfo().getSupplierValidateCode();
				String pw = DigestUtils.shaHex(MD5.toMD5(validateCode));
				if (pw.equalsIgnoreCase(password)){
				    boolean b = factoryService.supplierConfirmOrder(orderId,inspectionDateString,containReadyTime,orderFactoryBean);
					if (b) {
                        logger.info("confirm succeed !");
						return new ResponseEntity<>(HttpStatus.OK);
					} else {
						logger.info("failed confirming order !");
						return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
					}
				}
				logger.info("incorrect pw !   ["+ password +"] || should be :"+pw);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			logger.info("can not get order by id:"+orderId);
		} catch (Exception e) {
			logger.error("error in updateSupplierConfirm",e);
			e.printStackTrace();
		}
		return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
