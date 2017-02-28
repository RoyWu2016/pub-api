package com.ai.api.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.ai.commons.beans.email.EmailBean;
import com.ai.commons.services.EmailService;

public abstract class APIEmailService {
	
	@Autowired
	private EmailService emailService;
	
	public final boolean sendEmail(String templateId, EmailBean email, Map<String, Object> params){
		return emailService.sendEmail(templateId, email, params);
	}
}
