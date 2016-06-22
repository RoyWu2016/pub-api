package com.ai.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.bean.AqlAndSamplingSizeBean;
import com.ai.api.bean.BillingBean;
import com.ai.api.bean.BookingPreferenceBean;
import com.ai.api.bean.CompanyBean;
import com.ai.api.bean.ContactInfoBean;
import com.ai.api.bean.CustomAQLBean;
import com.ai.api.bean.MainBean;
import com.ai.api.bean.MinQuantityToBeReadyBean;
import com.ai.api.dao.impl.CompanyDaoImpl;
import com.ai.api.dao.impl.CustomerDaoImpl;
import com.ai.api.service.UserService;
import com.ai.commons.beans.customer.ContactBean;
import com.ai.commons.beans.customer.CrmCompanyBean;
import com.ai.commons.beans.customer.ExtraBean;
import com.ai.commons.beans.customer.OrderBookingBean;
import com.ai.commons.beans.customer.ProductFamilyBean;
import com.ai.commons.beans.user.GeneralUserBean;
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

	@Autowired
	CustomerDaoImpl customerDao;


	private MockMvc mockMvc;

	String userID;
	String compID;
	String login;

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
	public void getUserProfile() throws Exception {

		mockMvc.perform(get("/user/" + userID + "/profile"))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json;charset=UTF-8"));


	}

	/**
	 * test update user company info
	 * @throws Exception
	 */
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
	public void updateUserProfileContact() throws Exception {
		MainBean main = new MainBean();
		main.setSalutation(env.getProperty("mainContactSalutation"));
		main.setFamilyName(env.getProperty("mainContactFamilyName"));
		main.setGivenName(env.getProperty("mainContactGivenName"));
		main.setEmail(env.getProperty("mainContactEmail"));
		main.setPosition(env.getProperty("mainContactPosition"));
		main.setPhoneNumber(env.getProperty("mainContactPhoneNumber"));
		main.setMobileNumber(env.getProperty("mainContactMobileNumber"));

		BillingBean billing = new BillingBean();
		billing.setIsSameAsMainContact("true");

		ContactInfoBean newContact = new ContactInfoBean();
		newContact.setMain(main);
		newContact.setBilling(billing);

		String updateCompUrl = "/user/" + userID + "/profile/contactInfo";
		//update
		mockMvc.perform(put(updateCompUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newContact)))
				.andExpect(status().isOk());

		//check if updated
		GeneralUserBean general = customerDao.getGeneralUser(userID);
		ContactBean contact = companyDao.getCompanyContact(compID);

		Assert.assertEquals(newContact.getMain().getSalutation(), general.getFollowName());
		Assert.assertEquals(newContact.getMain().getFamilyName(), general.getLastName());
		Assert.assertEquals(newContact.getMain().getGivenName(), general.getFirstName());
		Assert.assertEquals(newContact.getMain().getEmail(), general.getPersonalEmail());
		Assert.assertEquals(newContact.getMain().getPosition(), contact.getMainPosition());
		Assert.assertEquals(newContact.getMain().getPhoneNumber(), general.getLandline());
		Assert.assertEquals(newContact.getMain().getMobileNumber(), general.getMobile());

		Assert.assertEquals(newContact.getMain().getSalutation(), contact.getAccountingGender());
		Assert.assertEquals(newContact.getMain().getFamilyName(), contact.getAccountingName());
		Assert.assertEquals(newContact.getMain().getGivenName(), contact.getAccountingGivenName());
		Assert.assertEquals(newContact.getMain().getEmail(), contact.getAccountingEmail());
	}

	@Test
	public void updateProfileBookingPreferredFamily() throws Exception {
		List<String> newFamililes = new ArrayList<>();
		newFamililes.add(env.getProperty("favFamily1"));
		newFamililes.add(env.getProperty("favFamily2"));
		newFamililes.add(env.getProperty("favFamily3"));

		String updateCompUrl = "/user/" + userID + "/profile/preference/booking/preferredProductFamilies";
		//update
		mockMvc.perform(put(updateCompUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newFamililes)))
				.andExpect(status().isOk());

		//check if updated
	    ProductFamilyBean family = companyDao.getCompanyProductFamily(compID);
		Assert.assertEquals(newFamililes.get(0), family.getRelevantCategoryInfo().get(0).getFavFamily());
		Assert.assertEquals(newFamililes.get(1), family.getRelevantCategoryInfo().get(1).getFavFamily());
		Assert.assertEquals(newFamililes.get(2), family.getRelevantCategoryInfo().get(2).getFavFamily());

	}

	@Test
	public void updateProfileBookingPreference() throws Exception {
		BookingPreferenceBean newPref = new BookingPreferenceBean();
		newPref.setUseQuickFormByDefault(env.getProperty("strFalse"));
		newPref.setShouldSendRefSampleToFactory(env.getProperty("strTrue"));
		newPref.setIsPoMandatory(env.getProperty("strTrue"));

		MinQuantityToBeReadyBean[] minQuantityToBeReadyBean = new MinQuantityToBeReadyBean[5];
		MinQuantityToBeReadyBean minQuantityToBeReadyBean1 = new MinQuantityToBeReadyBean();
		minQuantityToBeReadyBean1.setServiceType("PSI");
		minQuantityToBeReadyBean1.setMinQty(70);

		MinQuantityToBeReadyBean minQuantityToBeReadyBean2 = new MinQuantityToBeReadyBean();
		minQuantityToBeReadyBean2.setServiceType("DUPRO");
		minQuantityToBeReadyBean2.setMinQty(70);

		MinQuantityToBeReadyBean minQuantityToBeReadyBean3 = new MinQuantityToBeReadyBean();
		minQuantityToBeReadyBean3.setServiceType("IPC");
		minQuantityToBeReadyBean3.setMinQty(70);

		MinQuantityToBeReadyBean minQuantityToBeReadyBean4 = new MinQuantityToBeReadyBean();
		minQuantityToBeReadyBean4.setServiceType("CLC");
		minQuantityToBeReadyBean4.setMinQty(70);

		MinQuantityToBeReadyBean minQuantityToBeReadyBean5 = new MinQuantityToBeReadyBean();
		minQuantityToBeReadyBean5.setServiceType("PM");
		minQuantityToBeReadyBean5.setMinQty(70);

		minQuantityToBeReadyBean[0] = minQuantityToBeReadyBean1;
		minQuantityToBeReadyBean[1] = minQuantityToBeReadyBean2;
		minQuantityToBeReadyBean[2] = minQuantityToBeReadyBean3;
		minQuantityToBeReadyBean[3] = minQuantityToBeReadyBean4;
		minQuantityToBeReadyBean[4] = minQuantityToBeReadyBean5;
		newPref.setMinQuantityToBeReady(minQuantityToBeReadyBean);

		AqlAndSamplingSizeBean aqlAndSamplingSizeBean = new AqlAndSamplingSizeBean();
		aqlAndSamplingSizeBean.setCanModify(env.getProperty("strTrue"));
		aqlAndSamplingSizeBean.setCustomDefaultSampleLevel("II");
		aqlAndSamplingSizeBean.setUseCustomAQL(env.getProperty("strTrue"));
		newPref.setAqlAndSamplingSize(aqlAndSamplingSizeBean);

		CustomAQLBean aql = new CustomAQLBean();
		aql.setCriticalDefects("3");
		aql.setMajorDefects("3");
		aql.setMinorDefects("3");
		aql.setMaxMeasurementDefects("3");
		newPref.getAqlAndSamplingSize().setCustomAQL(aql);

		String updateCompUrl = "/user/" + userID + "/profile/preference/booking";
		//update
		mockMvc.perform(put(updateCompUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(newPref)))
				.andExpect(status().isOk());

		//check if updated
		OrderBookingBean pref = companyDao.getCompanyOrderBooking(compID);
		ExtraBean extra = companyDao.getCompanyExtra(compID);

		Assert.assertEquals(pref.getSendSampleToFactory(), "Yes");
		Assert.assertEquals(pref.getPoCompulsory(), "Yes");
		Assert.assertEquals(extra.getIsDetailedBookingForm(), "No");

	}


	@Test
	public void updateUserProfilePassword() throws Exception {

		String currentPwd = env.getProperty("currentPwd");
		String newPwd =env.getProperty("newPwd");

		Map<String, String> pwdMap = new HashMap<String,String>();
		pwdMap.put("current", currentPwd);
		pwdMap.put("new",newPwd);

		String updateUserPasswordUrl = "/user/" + userID + "/profile/password";

		GeneralUserBean general = customerDao.getGeneralUser(userID);

		//update
		mockMvc.perform(put(updateUserPasswordUrl)
				.contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(pwdMap)))
				.andExpect(status().isOk());

		//Assert.assertEquals(DigestUtils.shaHex(MD5.toMD5(newPwd)), general.getPassword());

		//change password back
		HashMap<String, String> secPwdMap = new HashMap<String,String>();
		secPwdMap.put("current", newPwd);
		secPwdMap.put("new",currentPwd);

		customerDao.updateGeneralUserPassword(userID,secPwdMap);
	}

	@Test
	public void testAdd() {
		double result = 3 + 3;
		assert (result == 6);
	}


}