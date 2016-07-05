package com.ai.api.controller.impl;

import java.io.IOException;
import java.util.List;

import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.FactorySearchBean;
import com.ai.api.controller.Supplier;
import com.ai.api.exception.AIException;
import com.ai.api.service.FactoryService;
import com.ai.commons.annotation.TokenSecured;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by Administrator on 2016/6/29 0029.
 */
@RestController
public class SupplierImpl implements Supplier {

    @Autowired
    FactoryService factoryService;

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/suppliers", method = RequestMethod.GET)
    public ResponseEntity<List<FactorySearchBean>> getUserSupplierById(@PathVariable("userId") String userId)
            throws IOException, AIException {
        System.out.println("get user's suppliers by userId: " + userId);

        List<FactorySearchBean> result = factoryService.getSuppliersByUserId(userId);

        if(result!=null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/supplier/{supplierId}", method = RequestMethod.GET)
    public ResponseEntity<SupplierDetailBean> getUserSupplierDetailInfoById(@PathVariable("userId") String userId, @PathVariable("supplierId") String supplierId)
            throws IOException, AIException {
        System.out.println("get user's supplier detail by supplierId: " + supplierId);

        SupplierDetailBean result = factoryService.getUserSupplierDetailInfoById(userId, supplierId);

        if(result!=null){
            return new ResponseEntity<>(result, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/supplier/{supplierId}", method = RequestMethod.PUT)
    public ResponseEntity<Boolean> updateUserSupplierDetailInfo(@PathVariable("userId") String userId,
                                                            @PathVariable("supplierId") String supplierId,
                                                            @RequestBody SupplierDetailBean supplierDetailBean)
            throws IOException, AIException {
        System.out.println("updating supplier detail info for user: " + userId);
        if (factoryService.updateSupplierDetailInfo(supplierDetailBean)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    @TokenSecured
    @RequestMapping(value = "/user/{userId}/supplier/{supplierId}", method = RequestMethod.DELETE)
    public ResponseEntity<Boolean> deleteSupplier(@PathVariable("userId") String userId,
                                                                @PathVariable("supplierId") String supplierId)
            throws IOException, AIException {
        System.out.println("deleting supplier for user: " + userId);
        if (factoryService.deleteSupplier(supplierId)) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
