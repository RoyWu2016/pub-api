package com.ai.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;

import com.ai.api.bean.CompanyBean;
import com.ai.api.dao.impl.CompanyDaoImpl;
import com.ai.api.exception.AIException;
import com.ai.api.service.UserService;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.CrmCompanyBean;
import com.ai.commons.beans.customer.GeneralUserViewBean;
import com.ai.commons.beans.customer.OrderBookingBean;
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
 * Created by Allen Zhang on 5/17/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@TestPropertySource("classpath:testData.properties")
@ContextConfiguration({"classpath:testDataSource.xml", "classpath:api-config.xml",
		"classpath:spring-controller.xml", "classpath:testConfig.xml"})
@WebAppConfiguration
public class UserServiceImplTest {
	@Autowired
	private WebApplicationContext context;

	@Autowired
	CompanyDaoImpl companyDao;

	private MockMvc mockMvc;

	String userID;
	String compID;
	String login;

	GeneralUserViewBean generalUserViewBean;
	ContactBean contactBean;
	OrderBookingBean orderBookingBean;

	@Autowired
	UserService userService;

	@Autowired
	private Environment env;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();

		userID = env.getProperty("userID");
		compID = env.getProperty("compID");
		login = env.getProperty("login");

	}

	@Test
	public void getUserList() throws Exception {
		mockMvc.perform(get("/userdemo/"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"))
				.andExpect(jsonPath("$[0]['id']").exists())
				.andExpect(jsonPath("$[0]['name']").exists())
				.andExpect(jsonPath("$[0]['name']").value("Sam"))
		;
	}

	@Test
	public void getUserProfile() throws Exception {

		mockMvc.perform(get("/user/" + login + "/profile"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));


	}

	@Test
	public void updateUserProfileCompany() throws Exception {
		CompanyBean newComp = new CompanyBean();
		newComp.setIndustry(env.getProperty("compIndustry"));
		newComp.setCountry(env.getProperty("compCountry"));
		newComp.setAddress(env.getProperty("compAddress"));
		newComp.setCity(env.getProperty("compCity"));
		newComp.setPostcode(env.getProperty("compPostCode"));

		String updateCompUrl = "/user/" + userID + "/profile/company";
		//update
		mockMvc.perform(put(updateCompUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newComp)))
				.andExpect(status().isOk());

		//check if updated
		CrmCompanyBean comp = companyDao.getCrmCompany(compID);
		Assert.assertEquals(comp.getIndustry(), newComp.getIndustry());
		Assert.assertEquals(comp.getAddress1(), newComp.getAddress());
		Assert.assertEquals(comp.getCity(), newComp.getCity());
		Assert.assertEquals(comp.getCountryRegion(), newComp.getCountry());

	}

	@Test
	public void getProfileContactUpdate_test() throws IOException, AIException {
		// UserServiceImpl userService = new UserServiceImpl();
		userService.updateContact(generalUserViewBean, contactBean, "002F7C45A47FC2E3C1256F81006893B");
	}

	@Test
	public void getProfileBookingPreferenceUpdate_test() throws IOException, AIException {
		//UserServiceImpl userService = new UserServiceImpl();
		userService.updateBookingPreference(orderBookingBean, "002F7C45A47FC2E3C1256F81006893B");
	}

	@Test
	public void testAdd() {
		double result = 3 + 3;
		assert (result == 6);
	}


}