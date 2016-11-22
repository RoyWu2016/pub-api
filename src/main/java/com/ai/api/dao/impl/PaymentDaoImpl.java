package com.ai.api.dao.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.ai.api.config.ServiceConfig;
import com.ai.api.dao.PaymentDao;
import com.ai.api.util.FTPUtil;
import com.ai.commons.HttpUtil;
import com.ai.commons.JsonUtil;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.GetRequest;
import com.ai.commons.beans.ServiceCallResult;
import com.ai.commons.beans.payment.api.PaypalInfoBean;
import com.fasterxml.jackson.core.type.TypeReference;

import src.main.java.com.ai.commons.beans.payment.PaymentPaidBean;

/**
 * Project Name : Public-API Package Name : com.ai.api.dao.impl Creation Date :
 * 2016/8/12 16:04 Author : Jianxiong.Cai Purpose : TODO History : TODO
 */
@Component
public class PaymentDaoImpl implements PaymentDao {
	private static final Logger logger = LoggerFactory.getLogger(PaymentDaoImpl.class);

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

	@Override
	public InputStream downloadProformaInvoicePDF(String login, String invoiceId) {
		String url = config.getMwServiceUrl() + "/service/payment/proformaInvoicePDF?login=" + login + "&invoiceId="
				+ invoiceId;
		try {
			GetRequest request = GetRequest.newInstance().setUrl(url);
			logger.info("get!!! Url:" + url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			logger.info("get done!");
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				logger.info("get OK!");
				String remotePath = "/CACHE/";
				String fileName = result.getResponseString();
				// String host = config.getMwFTPHost();
				String host = "";
				int port = 21;
				String username = "";
				String password = "";
				// String username = config.getMwFTPUsername();
				// String password = config.getMwFTPPassword();
				String tempPath = "/tmp";
				logger.info(remotePath);
				logger.info(fileName);
				logger.info(host + ":" + port);
				logger.info(username + " || " + password);
				logger.info(tempPath);
				boolean b = FTPUtil.downloadFile(host, port, username, password, remotePath, fileName, tempPath);
				if (b) {
					logger.info("success downloadFile to /tmp ");
					File tempFile = new File(tempPath + fileName);
					InputStream inputStream = new FileInputStream(tempFile);
					return inputStream;
				} else {
					logger.error("ERROR! fail to download PDF from FTP server. ");
					return null;
				}
			} else {
				logger.error("downloadProformaInvoicePDF from middleware error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				return null;
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			return null;
		}
	}

	@Override
	public ApiCallResult markAsPaid(String userId, String parentId, String companyId, PaymentPaidBean orders) {
		StringBuilder url = new StringBuilder(config.getPsiServiceUrl() + "/payment/api/mark-order-paid");
		url.append("?userId=" + userId)
		.append("&companyId=" + companyId)
		.append("&parentId=" + parentId);
		ApiCallResult temp = new ApiCallResult();
		try {
			logger.info("requesting: " + url.toString());
			ServiceCallResult result = HttpUtil.issuePostRequest(url.toString(), null, orders);
			if (result.getStatusCode() == HttpStatus.OK.value() && result.getReasonPhase().equalsIgnoreCase("OK")) {
				temp.setContent(true);
				return temp;
			} else {
				logger.error("Mark Payment As Paid from psi error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
				temp.setMessage("Mark Payment As Paid from psi error: " + result.getStatusCode() + ", "
						+ result.getResponseString());
			}
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
			temp.setMessage("Exception: " + e.toString());
		}
		return temp;
	}

	@Override
	public List<PaypalInfoBean> getPaypalPayment(String userId, String login, String orders) {
		try {
			String url = config.getMwServiceUrl() + "/service/payment/paypalPayment?userId=" + userId + "&login="
					+ login + "&order_ids_array=" + orders;
			GetRequest request = GetRequest.newInstance().setUrl(url);
			ServiceCallResult result = HttpUtil.issueGetRequest(request);
			return JsonUtil.mapToObject(result.getResponseString(), new TypeReference<List<PaypalInfoBean>>() {
			});
		} catch (Exception e) {
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		return null;
	}
}
