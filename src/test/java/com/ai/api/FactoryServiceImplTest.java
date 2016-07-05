package com.ai.api;

import com.ai.api.bean.FileDetailBean;
import com.ai.api.bean.SupplierContactInfoBean;
import com.ai.api.bean.SupplierDetailBean;
import com.ai.api.bean.legacy.ContactBean;
import com.ai.api.dao.impl.FactoryDaoImpl;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Administrator on 2016/7/4 0004.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:testData.properties")
@ContextConfiguration({"classpath:testDataSource.xml", "classpath:api-config.xml",
        "classpath:spring-controller.xml", "classpath:testConfig.xml"})
@WebAppConfiguration
public class FactoryServiceImplTest {
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
        String supplierId = "05BE534DC73C8B5848257B73002015BB";
        SupplierDetailBean supplierDetailBean = new SupplierDetailBean();
        supplierDetailBean.setId(supplierId);
        supplierDetailBean.setEntityName("UnitTestEntityName");
        //supplierDetailBean.setChineseName();
        supplierDetailBean.setCity("mumbai");
        supplierDetailBean.setCountry("India");
        supplierDetailBean.setAddress("Unit no.702,Tower no.1 seepz++ Andheri(E) Mumbai- 400096");
        supplierDetailBean.setPostcode("400096");
        supplierDetailBean.setNearestOffice("India Office");
        supplierDetailBean.setWebsite("www.patdiam.com");
        supplierDetailBean.setSalesTurnover("5-25 M$");
        supplierDetailBean.setNoOfEmployees("50 - 250");
        supplierDetailBean.setUserId(userID);
        List<String> mainProductLines = new ArrayList<String>();
        mainProductLines.add("bigCat3_s4");
        supplierDetailBean.setMainProductLines(mainProductLines);

        SupplierContactInfoBean supplierContactInfoBean = new SupplierContactInfoBean();
        ContactBean main = new ContactBean();
        main.setName("Pradip Bane");
        main.setPhone("+ 91 22 28293455/56");
        main.setMobile("+ 91 9821715164");
        main.setEmail("mktg@patdiam.com");
        supplierContactInfoBean.setMain(main);
        supplierDetailBean.setContactInfo(supplierContactInfoBean);

        List<FileDetailBean> docList = new ArrayList<FileDetailBean>();
        FileDetailBean busLicBean = new FileDetailBean();
        busLicBean.setDocType("BUS_LIC");
        busLicBean.setId("15df6547-cb8d-4085-8e32-b832c2f11a92");
        busLicBean.setFileName("Bussiness Licence.jpg");
        busLicBean.setFilesize(1042745);
        busLicBean.setUrl("http://202.66.128.138:8092/file-service/getFile?id=15df6547-cb8d-4085-8e32-b832c2f11a92");
        docList.add(busLicBean);

        FileDetailBean isoCertBean = new FileDetailBean();
        isoCertBean.setDocType("ISO_CERT");
        docList.add(isoCertBean);

        FileDetailBean exportLicBean = new FileDetailBean();
        exportLicBean.setDocType("EXPORT_LIC");
        exportLicBean.setId("560233c8-815a-432a-85c0-c367354189a3");
        exportLicBean.setFileName("EXPORT LICENCE.jpg");
        exportLicBean.setFilesize(326370);
        exportLicBean.setUrl("http://202.66.128.138:8092/file-service/getFile?id=560233c8-815a-432a-85c0-c367354189a3");
        docList.add(exportLicBean);

        FileDetailBean rohsCertBean = new FileDetailBean();
        rohsCertBean.setDocType("ROHS_CERT");
        docList.add(rohsCertBean);

        FileDetailBean testReportBean = new FileDetailBean();
        testReportBean.setDocType("TEST_REPORT");
        docList.add(testReportBean);

        FileDetailBean otherDocBean = new FileDetailBean();
        otherDocBean.setDocType("OTHER_DOC");
        otherDocBean.setId("635e94c1-5178-44a9-9777-eefa4fa82dfe");
        otherDocBean.setFileName("PJ VAT2.jpg");
        otherDocBean.setFilesize(350729);
        otherDocBean.setUrl("http://202.66.128.138:8092/file-service/getFile?id=635e94c1-5178-44a9-9777-eefa4fa82dfe");
        docList.add(otherDocBean);
        supplierDetailBean.setQualityDocs(docList);

        String updateSupplierUrl = "/user/" + userID + "/supplier/"+ supplierId;
        //update
        mockMvc.perform(put(updateSupplierUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(supplierDetailBean)))
                .andExpect(status().isOk());

        supplierDetailBean.setEntityName("Supplier's Initial Name");
        Assert.assertEquals(true,factoryDao.updateSupplierDetailInfo(supplierDetailBean));

    }

}
