package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/order/{orderId}/supplier", method = RequestMethod.PUT)
	public ResponseEntity<Boolean> supplierConfirm(
			@PathVariable("userId") String userId, 
			@PathVariable("orderId") String orderId,
			@RequestParam("inspectionDate") String inspectionDateString,
			@RequestParam("containerReadyDate") String containReadyTime,
			@RequestBody OrderFactoryBean orderFactoryBean) throws IOException, AIException {
		if (factoryService.supplierConfirmOrder(orderId,inspectionDateString,containReadyTime,orderFactoryBean)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
