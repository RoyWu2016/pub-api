package com.ai.api.controller;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.exception.AIException;
import com.ai.commons.beans.ServiceCallResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
public interface Factory {
    ResponseEntity<List<FactorySearchBean>> getUserSupplierById(String userId) throws IOException, AIException;

    ResponseEntity<SupplierDetailBean> getUserSupplierDetailInfoById(String userId, String supplierId) throws IOException, AIException;
}
