package com.ai.api.service;

import com.ai.aims.services.dto.order.OrderDTO;

public interface LTEmailService {
	
	public boolean sendEmailAddOrder(OrderDTO order, String userId);
}
