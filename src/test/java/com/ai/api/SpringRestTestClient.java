package com.ai.api;

import com.ai.api.model.UserDemoBean;
import com.ai.api.model.UserProfileBookingPreference;
import com.ai.api.model.UserProfileCompanyRequest;
import com.ai.api.model.UserProfileContactRequest;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;


//@PropertySource("classpath:classfields.properties")
public class SpringRestTestClient {

	public static final String REST_SERVICE_URI = "http://localhost:8888/api";

//	@Value("${Company.nameCN}")
//	static String name;

	/* GET */
	@SuppressWarnings("unchecked")
	private static void listAllUsers(){
		System.out.println("Testing listAllUsers API-----------");
		
		RestTemplate restTemplate = new RestTemplate();
		List<LinkedHashMap<String, Object>> usersMap = restTemplate.getForObject(REST_SERVICE_URI+"/user/", List.class);
		
		if(usersMap!=null){
			for(LinkedHashMap<String, Object> map : usersMap){
	            System.out.println("User : id="+map.get("id")+", Name="+map.get("name")+", Age="+map.get("age")+", Salary="+map.get("salary"));;
	        }
		}else{
			System.out.println("No user exist----------");
		}
	}
	
	/* GET */
	private static void getUser(){
		System.out.println("Testing getUser API----------");
		RestTemplate restTemplate = new RestTemplate();
        UserDemoBean user = restTemplate.getForObject(REST_SERVICE_URI+"/user/1", UserDemoBean.class);
        System.out.println(user);
	}
	
	/* POST */
    private static void createUser() {
		System.out.println("Testing create User API----------");
    	RestTemplate restTemplate = new RestTemplate();
        UserDemoBean user = new UserDemoBean(0,"Sarah",51,134);
        URI uri = restTemplate.postForLocation(REST_SERVICE_URI+"/user/", user, UserDemoBean.class);
        System.out.println("Location : "+uri.toASCIIString());
    }

    /* PUT */
    private static void updateUser() {
		System.out.println("Testing update User API----------");
        RestTemplate restTemplate = new RestTemplate();
        UserDemoBean user  = new UserDemoBean(1,"Tomy",33, 70000);
        restTemplate.put(REST_SERVICE_URI+"/user/1", user);
        System.out.println(user);
    }

    /* DELETE */
    private static void deleteUser() {
		System.out.println("Testing delete User API----------");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(REST_SERVICE_URI+"/user/3");
    }


    /* DELETE */
    private static void deleteAllUsers() {
		System.out.println("Testing all delete Users API----------");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(REST_SERVICE_URI+"/user/");
    }

	/* Profile Company UPDATE */

	private static void getProfileUpdate_test() {
		System.out.println("Testing Company UPDATE API----------");
		RestTemplate restTemplate = new RestTemplate();

		UserProfileCompanyRequest userProfileCompanyRequest = new UserProfileCompanyRequest();

		userProfileCompanyRequest.setNameCN("Prakash");
		userProfileCompanyRequest.setIndustry("Lue Info Services");
		userProfileCompanyRequest.setCountry("INDIA");
		userProfileCompanyRequest.setAddress("PATNA");
		userProfileCompanyRequest.setCity("PATNA");
		userProfileCompanyRequest.setPostcode("800020");


		restTemplate.put(REST_SERVICE_URI + "/user/002F7C45A47FC2E3C1256F81006893B1/profile/company", userProfileCompanyRequest);
		System.out.println(userProfileCompanyRequest);
	}

	/* Profile Contact UPDATE */

	private static void getProfileContactUpdate_test() {
		System.out.println("Testing Profile Contact Update API----------");
		RestTemplate restTemplate = new RestTemplate();

		UserProfileContactRequest userProfileContactRequest = new UserProfileContactRequest();

		userProfileContactRequest.setMainSalutation("ER.");
		userProfileContactRequest.setMainFamilyName("C Prakash");
		userProfileContactRequest.setMainGivenName("LIServices");
		userProfileContactRequest.setMainPosition("S java developer");
		userProfileContactRequest.setMainEmail("p@gml.com");
		userProfileContactRequest.setMainPhoneNumber("440905");
		userProfileContactRequest.setMainMobileNumber("8434323334");
		userProfileContactRequest.setBillingSalutation("DR.");
		userProfileContactRequest.setBillingFamilyName("Rahul");
		userProfileContactRequest.setBillingGivenName("Saksena");
		userProfileContactRequest.setBillingEmail("r@gml.com");
		userProfileContactRequest.setBillinIsSameAsMainContact("true");


		restTemplate.put(REST_SERVICE_URI + "/user/002F7C45A47FC2E3C1256F81006893B1/profile/contactInfo", userProfileContactRequest);
		System.out.println(userProfileContactRequest);
	}


   /* ProfileBookingPreference UPDATE */

	private static void getProfileBookingPreferenceUpdate_test() {
		System.out.println("Testing Profile Booking Preference Update API----------");
		RestTemplate restTemplate = new RestTemplate();

		UserProfileBookingPreference userProfileBookingPreference = new UserProfileBookingPreference();

		userProfileBookingPreference.setShouldSendRefSampleToFactory("NO");

		userProfileBookingPreference.setIsPoMandatory("NO");

		userProfileBookingPreference.setMinQuantityToBeReadyPsiPercentage(9);

		userProfileBookingPreference.setMinQuantityToBeReadyDuproPercentage(9);

		userProfileBookingPreference.setMinQuantityToBeReadyIpcPercentage(9);

		userProfileBookingPreference.setMinQuantityToBeReadyClcPercentage(9);

		userProfileBookingPreference.setMinQuantityToBeReadyPmPercentage(9);

		userProfileBookingPreference.setAleCanModify("No");

		userProfileBookingPreference.setAqlCustomDefaultSampleLevel("NO");

		userProfileBookingPreference.setUseCustomAQL("No");

		userProfileBookingPreference.setCustomAQLCriticalDefects("Not allowed");

		userProfileBookingPreference.setCustomAQLMajorDefects("300");

		userProfileBookingPreference.setCustoMAQLMinorDefects("20");

		userProfileBookingPreference.setCustoMAQLMaxMeasurementDefects("Not allowed");

		restTemplate.put(REST_SERVICE_URI + "/user/281539258F3217F4E050A8C0060063A0/profile/preference/booking/", userProfileBookingPreference);
		System.out.println(userProfileBookingPreference);
	}



    public static void main(String args[]){
		//listAllUsers();
		//getUser();
//		createUser();
//		listAllUsers();
//		updateUser();
//		listAllUsers();
//		deleteUser();
//		listAllUsers();
//		deleteAllUsers();
//		listAllUsers();
		getProfileUpdate_test();
		getProfileContactUpdate_test();
		getProfileBookingPreferenceUpdate_test();
	}
}






