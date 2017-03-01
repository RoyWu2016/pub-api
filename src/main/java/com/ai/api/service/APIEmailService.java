package com.ai.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.ai.commons.beans.email.EmailBean;
import com.ai.commons.services.EmailService;

public abstract class APIEmailService {
	
	@Autowired
	private EmailService emailService;
	
	@Value("${aca.base.url}")
	private String acaUrl;
	
	public final boolean sendEmail(String templateId, EmailBean email, Map<String, Object> params){
		params.put("url", acaUrl);
		return emailService.sendEmail(templateId, email, params);
	}
}
