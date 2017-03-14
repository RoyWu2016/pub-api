package com.ai.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.bean.SupplierContactInfoBean;
import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.ContactBean;
import com.ai.api.dao.impl.FactoryDaoImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

/**
 * Created by Administrator on 2016/7/4 0004.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:testData.properties")
@ContextConfiguration({"classpath:testDataSource.xml", "classpath:api-config.xml",
        "classpath:spring-controller.xml", "classpath:testConfig.xml"})
@WebAppConfiguration
public class SupplierImplTest {
    @Autowired
    private WebApplicationContext context;

    @Autowired
    FactoryDaoImpl factoryDao;

    @Autowired
    private Environment env;

    private MockMvc mockMvc;

    String userID;
    String compID;
    String login;

    @Before
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

        userID = env.getProperty("userID");
        compID = env.getProperty("compID");
        login = env.getProperty("login");

    }

    @Test
    public void updateSupplierDetailInfo() throws Exception {

        SupplierDetailBean supplierDetailBean = new SupplierDetailBean();
        String supplierId = env.getProperty("supplierId");

        supplierDetailBean.setId(supplierId);
        supplierDetailBean.setEntityName(env.getProperty("suplierEntityName"));
        supplierDetailBean.setCity(env.getProperty("supplierCity"));
        supplierDetailBean.setCountry(env.getProperty("supplierCountry"));
        supplierDetailBean.setAddress(env.getProperty("supplierAddress"));
        supplierDetailBean.setPostcode(env.getProperty("supplierPostCode"));
        supplierDetailBean.setNearestOffice(env.getProperty("supplierNearestOffice"));
        supplierDetailBean.setWebsite(env.getProperty("supplierWebsite"));
        supplierDetailBean.setSalesTurnover(env.getProperty("supplierSalesTurnover"));
        supplierDetailBean.setNoOfEmployees(env.getProperty("supplierNoOfEmployees"));
        supplierDetailBean.setUserId(userID);
        List<String> mainProductLines = new ArrayList<String>();
        mainProductLines.add(env.getProperty("favFamily2"));
        supplierDetailBean.setMainProductLines(mainProductLines);

        SupplierContactInfoBean supplierContactInfoBean = new SupplierContactInfoBean();
        ContactBean main = new ContactBean();
        main.setName(env.getProperty("supplierContactInfoName"));
        main.setPhone(env.getProperty("supplierContactInfoPhone"));
        main.setMobile(env.getProperty("supplierContactInfoMobile"));
        main.setEmail(env.getProperty("supplierContactInfoEmail"));
        supplierContactInfoBean.setMain(main);
        supplierDetailBean.setContactInfo(supplierContactInfoBean);

        List<FileDetailBean> docList = new ArrayList<FileDetailBean>();
        FileDetailBean busLicBean = new FileDetailBean();
        busLicBean.setDocType(env.getProperty("fileBusLicDocType"));
        busLicBean.setId(env.getProperty("fileBusLicDocId"));
        busLicBean.setFileName(env.getProperty("fileBusLicDocFileName"));
        busLicBean.setFileSize(Long.parseLong(env.getProperty("fileBusLicDocFileSize")));
        busLicBean.setUrl(env.getProperty("fileBusLicDocUrl"));
        docList.add(busLicBean);

        FileDetailBean taxCertBean = new FileDetailBean();
        taxCertBean.setDocType(env.getProperty("fileTaxCertDocType"));
        taxCertBean.setId(env.getProperty("fileTaxCertDocId"));
        taxCertBean.setFileName(env.getProperty("fileTaxCertDocFileName"));
        taxCertBean.setFileSize(Long.parseLong(env.getProperty("fileTaxCertDocFileSize")));
        taxCertBean.setUrl(env.getProperty("fileTaxCertDocUrl"));
        docList.add(taxCertBean);

        FileDetailBean isoCertBean = new FileDetailBean();
        isoCertBean.setDocType(env.getProperty("fileIsoCertDocType"));
        isoCertBean.setId(env.getProperty("fileIsoCertDocId"));
        isoCertBean.setFileName(env.getProperty("fileIsoCertDocFileName"));
        isoCertBean.setFileSize(Long.parseLong(env.getProperty("fileIsoCertDocFileSize")));
        isoCertBean.setUrl(env.getProperty("fileIsoCertDocUrl"));
        docList.add(isoCertBean);

        FileDetailBean exportLicBean = new FileDetailBean();
        exportLicBean.setDocType(env.getProperty("fileExportLicDocType"));
        exportLicBean.setId(env.getProperty("fileExportLicDocId"));
        exportLicBean.setFileName(env.getProperty("fileExportLicDocFileName"));
        exportLicBean.setFileSize(Long.parseLong(env.getProperty("fileExportLicDocFileSize")));
        exportLicBean.setUrl(env.getProperty("fileExportLicDocUrl"));
        docList.add(exportLicBean);

        FileDetailBean rohsCertBean = new FileDetailBean();
        rohsCertBean.setDocType(env.getProperty("fileRohsCertDocType"));
        rohsCertBean.setId(env.getProperty("fileRohsCertDocId"));
        rohsCertBean.setFileName(env.getProperty("fileRohsCertDocFileName"));
        rohsCertBean.setFileSize(Long.parseLong(env.getProperty("fileRohsCertDocFileSize")));
        rohsCertBean.setUrl(env.getProperty("fileRohsCertDocUrl"));
        docList.add(rohsCertBean);

        FileDetailBean testReportBean = new FileDetailBean();
        testReportBean.setDocType(env.getProperty("fileTestReportDocType"));
        testReportBean.setId(env.getProperty("fileTestReportDocId"));
        testReportBean.setFileName(env.getProperty("fileTestReportDocFileName"));
        testReportBean.setFileSize(Long.parseLong(env.getProperty("fileTestReportDocFileSize")));
        testReportBean.setUrl(env.getProperty("fileTestReportDocUrl"));
        docList.add(testReportBean);

        FileDetailBean otherDocBean = new FileDetailBean();
        otherDocBean.setDocType(env.getProperty("fileOtherDocDocType"));
        otherDocBean.setId(env.getProperty("fileOtherDocDocId"));
        otherDocBean.setFileName(env.getProperty("fileOtherDocDocFileName"));
        otherDocBean.setFileSize(Long.parseLong(env.getProperty("fileOtherDocDocFileSize")));
        otherDocBean.setUrl(env.getProperty("fileOtherDocDocUrl"));
        docList.add(otherDocBean);
        supplierDetailBean.setQualityDocs(docList);

        String updateSupplierUrl = "/user/" + userID + "/supplier/"+ supplierId;
        //update
        mockMvc.perform(put(updateSupplierUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(supplierDetailBean)))
                .andExpect(status().isOk());

        SupplierDetailBean detailBean = JSON.toJavaObject((JSONObject)factoryDao.getUserSupplierDetailInfoById(userID, supplierId).getContent(),SupplierDetailBean.class);

        Assert.assertEquals(detailBean.getEntityName(),supplierDetailBean.getEntityName());
        Assert.assertEquals(detailBean.getCity(),supplierDetailBean.getCity());
        Assert.assertEquals(detailBean.getPostcode(),supplierDetailBean.getPostcode());
        Assert.assertEquals(detailBean.getCountry(),supplierDetailBean.getCountry());
        Assert.assertEquals(detailBean.getContactInfo().getMain().getName(),supplierDetailBean.getContactInfo().getMain().getName());
        Assert.assertEquals(detailBean.getContactInfo().getMain().getPhone(),supplierDetailBean.getContactInfo().getMain().getPhone());
        Assert.assertEquals(detailBean.getQualityDocs().get(0).getId(),supplierDetailBean.getQualityDocs().get(0).getId());
        Assert.assertEquals(detailBean.getQualityDocs().get(1).getFileName(),supplierDetailBean.getQualityDocs().get(1).getFileName());
        Assert.assertEquals(detailBean.getQualityDocs().get(2).getFileSize(),supplierDetailBean.getQualityDocs().get(2).getFileSize());
        Assert.assertEquals(detailBean.getQualityDocs().get(3).getUrl(),supplierDetailBean.getQualityDocs().get(3).getUrl());
        Assert.assertEquals(detailBean.getQualityDocs().get(4).getDocType(),supplierDetailBean.getQualityDocs().get(4).getDocType());
    }

}
