package com.ai.api.controller.impl;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ai.api.controller.Dashboard;
import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.customer.DashboardBean;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@Api(tags = { "Dashboard" }, description = "Dashboard APIs")
public class DashboardImpl implements Dashboard {

	@Autowired
	UserService userService;

	@Override
	@TokenSecured
	@RequestMapping(value = "/user/{userId}/dashboard-overview", method = RequestMethod.GET)
	@ApiOperation(value = "Get User Dashboard OverView API", response = DashboardBean.class)
	public ResponseEntity<ApiCallResult> getDashboardOverView(
			@ApiParam(value = "userId", required = true) @PathVariable("userId") String userId,
			@ApiParam(value = "must be in format like 2016-12-01", required = false) @RequestParam(value = "startDate", required = false, defaultValue = "") String startDate,
			@ApiParam(value = "must be in format like 2016-12-01", required = false) @RequestParam(value = "endDate", required = false, defaultValue = "") String endDate)
			throws IOException, AIException {
		if (StringUtils.isEmpty(startDate) || StringUtils.isEmpty(endDate)) {
			Calendar cal = Calendar.getInstance();
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
			endDate = sf.format(cal.getTime());
			cal.add(Calendar.YEAR, -1);
			startDate = sf.format(cal.getTime());
		}
		ApiCallResult result = userService.getDashboardOverView(userId, startDate, endDate);
		if (null == result.getMessage()) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
