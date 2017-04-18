package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.controller.SupplicerV2;
import com.ai.api.exception.AIException;
import com.ai.api.service.FactoryService;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.supplier.SupplierSearchResultBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings({ "rawtypes", "unchecked" })
@RestController
@Api(tags = { "SupplierV2" }, description = "Supplier V2 APIs")
public class SupplicerV2Impl implements SupplicerV2 {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	@Qualifier("apiFactoryService")
	FactoryService factoryService;

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
		return new ResponseEntity<>(rest,HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
