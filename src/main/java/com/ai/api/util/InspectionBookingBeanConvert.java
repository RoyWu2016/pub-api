package com.ai.api.util;

import com.ai.commons.StringUtils;
import com.ai.commons.beans.psi.*;

import java.util.List;

/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.util
 * Creation Date   : 2016/11/25 10:05
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class InspectionBookingBeanConvert {

    public static InspectionBookingBean po2vo(InspectionBookingBean po){

        InspectionOrderBookingBean order = po.getOrder();
        List<InspectionProductBookingBean> products = po.getProducts();

        OrderSupplierBean supplier = order.getOrderSupplier();
        String supplierMGREmailEmail = supplier.getSupplierMGREmail();
        supplier.setSupplierMGREmailList(StringUtils.toList(supplierMGREmailEmail,","));
        String supplierOtherEmail = supplier.getSupplierOtherEmail();
        supplier.setSupplierOtherEmailList(StringUtils.toList(supplierOtherEmail,","));
        String supplierProductLine = supplier.getSupplierProductLines();
        supplier.setSupplierProductLinesList(StringUtils.toList(supplierProductLine,";"));

        OrderFactoryBean factory = order.getOrderFactory();
        String factoryMGREmail = factory.getFactoryMGREmail();
        factory.setFactoryMGREmailList(StringUtils.toList(factoryMGREmail,","));
        String factoryOtherEmail = factory.getFactoryOtherEmail();
        factory.setFactoryOtherEmailList(StringUtils.toList(factoryOtherEmail,","));
        String factoryProductLines = factory.getFactoryProductLines();
        factory.setFactoryProductLinesList(StringUtils.toList(factoryProductLines,";"));

        OrderGeneralInfoBean orderGeneralInfo = order.getOrderGeneralInfo();
        boolean allowChangeInspectionDate = StringUtils.isTrue(orderGeneralInfo.getAllowChangeInspectionDate());
        orderGeneralInfo.setAllowChangeInspectionDateBoolean(allowChangeInspectionDate);

        OrderExtraBean orderExtra = order.getOrderExtra();
        boolean isReInspection = StringUtils.isTrue(orderExtra.getIsReInspection());
        orderExtra.setIsReInspectionBoolean(isReInspection);

        for (InspectionProductBookingBean p:products){
            ProductSpecBean productSpec = p.getProductSpec();
            boolean performDropTest = StringUtils.isTrue(productSpec.getPerformDropTest());
            productSpec.setPerformDropTestBoolean(performDropTest);
            ProductChecklistBean checklist = p.getProductChecklistBean();
            boolean performClientTestsOnly =  StringUtils.isTrue(checklist.getPerformClientTestsOnly());
            checklist.setPerformClientTestsOnlyBoolean(performClientTestsOnly);
        }
        return po;
    }

    public static InspectionBookingBean vo2po(InspectionBookingBean vo){

        InspectionOrderBookingBean order = vo.getOrder();
        List<InspectionProductBookingBean> products = vo.getProducts();

        OrderSupplierBean supplier = order.getOrderSupplier();
        List<String> supplierMGREmailEmail = supplier.getSupplierMGREmailList();
        supplier.setSupplierMGREmail(StringUtils.join(supplierMGREmailEmail,","));
        List<String> supplierOtherEmail = supplier.getSupplierOtherEmailList();
        supplier.setSupplierOtherEmail(StringUtils.join(supplierOtherEmail,","));
        List<String> supplierProductLine = supplier.getSupplierProductLinesList();
        supplier.setSupplierProductLines(StringUtils.join(supplierProductLine,";"));

        OrderFactoryBean factory = order.getOrderFactory();
        List<String> factoryMGREmail = factory.getFactoryMGREmailList();
        factory.setFactoryMGREmail(StringUtils.join(factoryMGREmail,","));
        List<String> factoryOtherEmail = factory.getFactoryOtherEmailList();
        factory.setFactoryOtherEmail(StringUtils.join(factoryOtherEmail,","));
        List<String> factoryProductLines = factory.getFactoryProductLinesList();
        factory.setFactoryProductLines(StringUtils.join(factoryProductLines,";"));

        OrderGeneralInfoBean orderGeneralInfo = order.getOrderGeneralInfo();
        orderGeneralInfo.setAllowChangeInspectionDate(orderGeneralInfo.isAllowChangeInspectionDateBoolean()?"Y":"N");

        OrderExtraBean orderExtra = order.getOrderExtra();
        orderExtra.setIsReInspection(orderExtra.getIsReInspectionBoolean()?"Yes":"No");

        for (InspectionProductBookingBean p:products){
            ProductSpecBean productSpec = p.getProductSpec();
            productSpec.setPerformDropTest(productSpec.isPerformDropTestBoolean()?"Yes":"No");
            ProductChecklistBean checklist = p.getProductChecklistBean();
            checklist.setPerformClientTestsOnly(checklist.isPerformClientTestsOnlyBoolean()?"Yes":"No");
        }
        return vo;
    }
}
