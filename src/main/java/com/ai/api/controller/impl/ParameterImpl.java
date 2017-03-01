package com.ai.api.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ai.api.bean.ChecklistSampleSize;
import com.ai.api.bean.ChecklistSampleSizeChildren;
import com.ai.api.bean.ChinaTimeBean;
import com.ai.api.bean.CountryBean;
import com.ai.api.bean.DropdownListOptionBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.bean.TextileDropdownListOptionBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.Parameter;
import com.ai.api.service.ParameterService;
import com.ai.api.util.RedisUtil;
import com.ai.commons.StringUtils;
import com.ai.commons.annotation.TokenSecured;
import com.ai.commons.beans.ApiCallResult;
import com.ai.commons.beans.checklist.vo.CKLDefectVO;
import com.ai.commons.beans.checklist.vo.CKLTestVO;
import com.ai.commons.beans.params.ChecklistTestSampleSizeBean;
import com.ai.commons.beans.params.ClassifiedBean;
import com.ai.commons.beans.params.GeoCountryCallingCodeBean;
import com.ai.commons.beans.params.GeoPlanetBean;
import com.ai.commons.beans.params.TextileCategoryBean;
import com.ai.commons.beans.params.product.SysProductTypeBean;
import com.ai.commons.services.FileService;
import com.alibaba.fastjson.JSON;
import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import io.swagger.annotations.Api;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Administrator on 2016/6/21 0021.
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@Api(tags = {"Parameter"}, description = "Parameters APIs")
public class ParameterImpl implements Parameter {

	protected Logger logger = LoggerFactory.getLogger(ParameterImpl.class);

	@Autowired
	ParameterService parameterService;

	@Autowired
	@Qualifier("serviceConfig")
	private ServiceConfig config;

    @Autowired
    @Qualifier("fileService")
    private FileService fileService;


    @Autowired
	com.ai.commons.services.ParameterService commonParamService;

	// @Autowired
	// ApiCallResult callResult;

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/product-categories", method = RequestMethod.GET)
	public ResponseEntity<List<ProductCategoryDtoBean>> getProductCategoryList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

		List<ProductCategoryDtoBean> result = parameterService.getProductCategoryList(refresh);

		if (result == null) {
			logger.error("Product category list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/product-families", method = RequestMethod.GET)
	public ResponseEntity<List<ProductFamilyDtoBean>> getProductFamilyList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

		List<ProductFamilyDtoBean> result = parameterService.getProductFamilyList(refresh);

		if (result == null) {
			logger.error("Product family list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/parameter/countries-with-calling-country-code", method = RequestMethod.GET)
	public ResponseEntity<List<CountryBean>> getCountryList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("Get countries-with-calling-country-code ...");
		List<GeoCountryCallingCodeBean> result = parameterService.getCountryList(refresh);
		if (result == null) {
			logger.error("country list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<CountryBean> countryBeanList = new ArrayList<CountryBean>();
		for (GeoCountryCallingCodeBean each : result) {
			CountryBean bean = new CountryBean();
			bean.setCode(each.getCallingCode());
			bean.setIsoCode(each.getAbbreviation());
			bean.setLabel(each.getCountry());
			bean.setValue(each.getCountryId());

			countryBeanList.add(bean);
		}
		return new ResponseEntity<>(countryBeanList, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/checklist-test-sample-size-list", method = RequestMethod.GET)
	public ResponseEntity<List<ChecklistSampleSize>> getTestSampleSizeList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

		Map<String, List<ChecklistTestSampleSizeBean>> resultMap = parameterService.getTestSampleSizeList(refresh);

		if (resultMap == null) {
			logger.error("TestSampleSizeList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<ChecklistSampleSize> result = this.changeMap2Bean(resultMap);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/public-tests", method = RequestMethod.GET)
	public ResponseEntity<List<CKLTestVO>> getChecklistPublicTestList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("get checklistPublicTests");
		List<CKLTestVO> result = parameterService.getChecklistPublicTestList(refresh);

		if (result == null) {
			logger.error("checklistTestList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/public-defects", method = RequestMethod.GET)
	public ResponseEntity<List<CKLDefectVO>> getChecklistPublicDefectList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("get checklistPublicDefects");
		List<CKLDefectVO> result = parameterService.getChecklistPublicDefectList(refresh);

		if (result == null) {
			logger.error("checklistTestList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/product-types", method = RequestMethod.GET)
	public ResponseEntity<Map<String, Object>> getProductTypeList(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysProductTypeBean> sysProductTypeBeanList = parameterService.getProductTypeList(refresh);
		if (sysProductTypeBeanList != null) {
			map.put("success", true);
			map.put("data", sysProductTypeBeanList);
			return new ResponseEntity<>(map, HttpStatus.OK);
		} else {
			logger.error("TestSampleSizeList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/textile-product-categories", method = RequestMethod.GET)
	public ResponseEntity<List<TextileDropdownListOptionBean>> getTextileProductCategories(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("get getTextileProductCategory");
		List<TextileCategoryBean> result = parameterService.getTextileProductCategories(refresh);
		if (result == null) {
			logger.error("getTextileProductCategory not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<TextileDropdownListOptionBean> list = new ArrayList<TextileDropdownListOptionBean>();
		for (TextileCategoryBean each : result) {
			TextileDropdownListOptionBean bean = new TextileDropdownListOptionBean();
			bean.setLabel(each.getTextileCategory());
			bean.setValue(each.getTextileCategory());
			bean.setMeasurementPoints(each.getNumberOfInspection());

			list.add(bean);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	private List<ChecklistSampleSize> changeMap2Bean(Map<String, List<ChecklistTestSampleSizeBean>> map) {
		List<ChecklistSampleSize> list = new ArrayList<ChecklistSampleSize>();
		for (Map.Entry<String, List<ChecklistTestSampleSizeBean>> entry : map.entrySet()) {
			ChecklistSampleSize bean = new ChecklistSampleSize();
			bean.setLabel(entry.getKey());
			List<ChecklistSampleSizeChildren> children = new ArrayList<>();
			for (ChecklistTestSampleSizeBean b : entry.getValue()) {
				ChecklistSampleSizeChildren child = new ChecklistSampleSizeChildren();
				child.setValue(b.getValue());
				child.setLabel(b.getText());
				children.add(child);
			}
			bean.setChildren(children);
			list.add(bean);
		}
		return list;
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/ai-offices", method = RequestMethod.GET)
	public ResponseEntity<List<DropdownListOptionBean>> getAiOffices(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("get getAiOffices");
		List<ClassifiedBean> result = parameterService.getAiOffices(refresh);
		if (result == null) {
			logger.error("getAiOffices not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<DropdownListOptionBean> list = new ArrayList<DropdownListOptionBean>();
		for (ClassifiedBean each : result) {
			DropdownListOptionBean bean = new DropdownListOptionBean();
			bean.setLabel(each.getKey());
			bean.setValue(each.getValue());

			list.add(bean);
		}
		return new ResponseEntity<>(list, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/server-time", method = RequestMethod.GET)
	public ResponseEntity<ChinaTimeBean> getChinaTime() {
		// TODO Auto-generated method stub
		ChinaTimeBean chinaTimeBean = parameterService.getChinaTime();
		return new ResponseEntity<>(chinaTimeBean, HttpStatus.OK);
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/parameter/continents", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getContinents(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("invoking: /parameter/continents?refresh=" + refresh);
		ApiCallResult callResult = new ApiCallResult();
		List<DropdownListOptionBean> result = null;
		if (!refresh) {
			logger.info("try to getContinents from redis ...");
			String jsonString = RedisUtil.get("continentCache");
			result = JSON.parseArray(jsonString, DropdownListOptionBean.class);
		}
		if (null == result) {
			logger.info("Can not find from redis try to get from commonParamService...");
			List<GeoPlanetBean> conts = commonParamService.listContinents();
			List<DropdownListOptionBean> temp = new ArrayList<DropdownListOptionBean>();
			if (conts != null) {
				for (GeoPlanetBean each : conts) {
					DropdownListOptionBean bean = new DropdownListOptionBean();
					bean.setLabel(each.getName());
					bean.setValue(each.getId());
					temp.add(bean);
				}
				logger.info("saving getContinents into redis");
				RedisUtil.set("continentCache", JSON.toJSONString(temp), RedisUtil.HOUR * 24);
				callResult.setContent(temp);
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("Get continets list error!");
				return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.info("getContinents from redis successfully");
			callResult.setContent(result);
			return new ResponseEntity<>(callResult, HttpStatus.OK);
		}
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/parameter/countries", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getAllCountries(
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("invoking: /parameter/all-countries?refresh=" + refresh);
		ApiCallResult callResult = new ApiCallResult();
		List<DropdownListOptionBean> result = null;
		if (!refresh) {
			logger.info("try to getAllCountries from redis ...");
			String jsonString = RedisUtil.get("allCountriesCache");
			result = JSON.parseArray(jsonString, DropdownListOptionBean.class);
		}
		if (null == result) {
			logger.info("Can not find from redis try to get from commonParamService...");
			List<GeoPlanetBean> conts = commonParamService.getAllCountries();
			List<DropdownListOptionBean> temp = new ArrayList<DropdownListOptionBean>();
			if (conts != null) {
				for (GeoPlanetBean each : conts) {
					DropdownListOptionBean bean = new DropdownListOptionBean();
					bean.setLabel(each.getName());
					bean.setValue(each.getId());
					temp.add(bean);
				}
				logger.info("saving getAllCountries into redis");
				RedisUtil.set("allCountriesCache", JSON.toJSONString(temp), RedisUtil.HOUR * 24);
				callResult.setContent(temp);
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("Get getAllCountries list error!");
				return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.info("getAllCountries from redis successfully");
			callResult.setContent(result);
			return new ResponseEntity<>(callResult, HttpStatus.OK);
		}
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/parameter/continent/{continentId}/countries", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getCountriesByContinentId(@PathVariable("continentId") String continentId,
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("invoking: /parameter/continent/" + continentId + "/countries?refresh=" + refresh);
		ApiCallResult callResult = new ApiCallResult();
		List<DropdownListOptionBean> result = null;
		if (!refresh) {
			logger.info("try to getCountries from redis ...");
			String jsonString = RedisUtil.hget("countriesByContinentIdCache", continentId);
			result = JSON.parseArray(jsonString, DropdownListOptionBean.class);
		}
		if (null == result) {
			logger.info("Can not find from redis try to get from commonParamService...");
			List<GeoPlanetBean> conts = commonParamService.getCountriesOfContinent(continentId);
			List<DropdownListOptionBean> temp = new ArrayList<DropdownListOptionBean>();
			if (conts != null) {
				for (GeoPlanetBean each : conts) {
					DropdownListOptionBean bean = new DropdownListOptionBean();
					bean.setLabel(each.getName());
					bean.setValue(each.getId());
					temp.add(bean);
				}
				logger.info("saving getContinents into redis");
				RedisUtil.hset("countriesByContinentIdCache", continentId, JSON.toJSONString(temp),
						RedisUtil.HOUR * 24);
				callResult.setContent(temp);
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("Get country list error!");
				return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.info("getCountries from redis successfully");
			callResult.setContent(result);
			return new ResponseEntity<>(callResult, HttpStatus.OK);
		}
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/parameter/country/{countryId}/provinces", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getProvincesByCountryId(@PathVariable("countryId") String countryId,
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("invoking: /parameter/country/" + countryId + "/provinces?refresh=" + refresh);
		ApiCallResult callResult = new ApiCallResult();
		List<DropdownListOptionBean> result = null;
		if (!refresh) {
			logger.info("try to getProvinces from redis ...");
			String jsonString = RedisUtil.hget("provincesByCountryIdCache", countryId);
			result = JSON.parseArray(jsonString, DropdownListOptionBean.class);
		}
		if (null == result) {
			logger.info("Can not find from redis try to get from commonParamService...");
			List<GeoPlanetBean> conts = commonParamService.getProvincesOfCountry(countryId);
			List<DropdownListOptionBean> temp = new ArrayList<DropdownListOptionBean>();
			if (conts != null) {
				for (GeoPlanetBean each : conts) {
					DropdownListOptionBean bean = new DropdownListOptionBean();
					bean.setLabel(each.getName());
					bean.setValue(each.getId());
					temp.add(bean);
				}
				logger.info("saving getProvinces into redis");
				RedisUtil.hset("provincesByCountryIdCache", countryId, JSON.toJSONString(temp), RedisUtil.HOUR * 24);
				callResult.setContent(temp);
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("Get province list error!");
				return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.info("getProvinces from redis successfully");
			callResult.setContent(result);
			return new ResponseEntity<>(callResult, HttpStatus.OK);
		}
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/parameter/province/{provinceId}/cities", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getCitiesByProvinceId(@PathVariable("provinceId") String provinceId,
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("invoking: /parameter/province/" + provinceId + "/cities?refresh=" + refresh);
		ApiCallResult callResult = new ApiCallResult();
		List<DropdownListOptionBean> result = null;
		if (!refresh) {
			logger.info("try to getCitiesByProvincName from redis ...");
			String jsonString = RedisUtil.hget("citiesByProvinceIdCache", provinceId);
			result = JSON.parseArray(jsonString, DropdownListOptionBean.class);
		}
		if (null == result) {
			logger.info("Can not find from redis try to get from commonParamService...");
			List<GeoPlanetBean> conts = commonParamService.getCitiesOfProvince(provinceId);
			List<DropdownListOptionBean> temp = new ArrayList<DropdownListOptionBean>();
			if (conts != null) {
				for (GeoPlanetBean each : conts) {
					DropdownListOptionBean bean = new DropdownListOptionBean();
					bean.setLabel(each.getName());
					bean.setValue(each.getId());
					temp.add(bean);
				}
				logger.info("saving getCitiesByProvincName into redis");
				RedisUtil.hset("citiesByProvinceIdCache", provinceId, JSON.toJSONString(temp), RedisUtil.HOUR * 24);
				callResult.setContent(temp);
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("Get city list error!");
				return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.info("getCitiesByProvincName from redis successfully");
			callResult.setContent(result);
			return new ResponseEntity<>(callResult, HttpStatus.OK);
		}
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/parameter/country/{countryId}/cities", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getCitiesByCountryId(@PathVariable("countryId") String countryId,
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("invoking: /parameter/country/" + countryId + "/cities?refresh=" + refresh);
		ApiCallResult callResult = new ApiCallResult();
		List<DropdownListOptionBean> result = null;
		if (!refresh) {
			logger.info("try to getCitiesByCountryId from redis ...");
			String jsonString = RedisUtil.hget("citiesByCountryIdCache", countryId);
			result = JSON.parseArray(jsonString, DropdownListOptionBean.class);
		}
		if (null == result) {
			logger.info("Can not find from redis try to get from commonParamService...");
			List<GeoPlanetBean> conts = commonParamService.getCitiesOfCountry(countryId);
			List<DropdownListOptionBean> temp = new ArrayList<DropdownListOptionBean>();
			if (conts != null) {
				for (GeoPlanetBean each : conts) {
					DropdownListOptionBean bean = new DropdownListOptionBean();
					bean.setLabel(each.getName());
					bean.setValue(each.getId());
					temp.add(bean);
				}
				logger.info("saving getCitiesByCountryId into redis");
				RedisUtil.hset("citiesByCountryIdCache", countryId, JSON.toJSONString(temp), RedisUtil.HOUR * 24);
				callResult.setContent(temp);
				return new ResponseEntity<>(callResult, HttpStatus.OK);
			} else {
				callResult.setMessage("Get city list error!");
				return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.info("getCitiesByCountryId from redis successfully");
			callResult.setContent(result);
			return new ResponseEntity<>(callResult, HttpStatus.OK);
		}
	}

	@Override
	@RequestMapping(value = "/parameter/sic/{sicId}/base64", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> getSaleImage(@PathVariable("sicId") String sicId,
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

		logger.info("getSaleImage  sicId:[" + sicId + "]  | refresh:[" + refresh + "]");
		ApiCallResult callResult = new ApiCallResult();
		try {
			String fileStr = null;
			if (!refresh) {
				logger.info("try to get saleImage from redis...");
				fileStr = RedisUtil.hget("SaleImage", sicId);
			}
			if (StringUtils.isBlank(fileStr)) {
				logger.info("get saleImage from parameterService ...");
				fileStr = parameterService.getSaleImage(sicId);
				if (StringUtils.isBlank(fileStr)) {
					logger.error("saleImage is null!");
					callResult.setMessage("saleImage is null!");
					return new ResponseEntity<>(callResult, HttpStatus.OK);
				}
				fileStr = "data:image/jpg;base64," + fileStr;
				RedisUtil.hset("SaleImage", sicId, fileStr, RedisUtil.HOUR * 24 * 14);
			}
			callResult.setContent(fileStr);
			return new ResponseEntity<>(callResult, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("error!!", e);
			callResult.setMessage(e.toString());
			return new ResponseEntity<>(callResult, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@RequestMapping(value = "/parameter/{userName}/is-aca-user", method = RequestMethod.GET)
	public ResponseEntity<ApiCallResult> isACAUser(@PathVariable("userName") String userName) {
		logger.info("invoke: " + "/parameter/" + userName + "/is-aca-user");
		logger.info("check if userName exist: " + userName);
		ApiCallResult result = new ApiCallResult();
		boolean isUsrNameExist = parameterService.checkIfUserNameExist(userName);
		if(isUsrNameExist) {
			boolean isAcaUser = parameterService.isACAUser(userName);
			result.setContent(isAcaUser);
		}else {
			result.setMessage(userName + " does not exist.");;
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/parameter/resources/{resourceName}/base64", method = RequestMethod.GET)
	public ResponseEntity<String> getCommonImagesBase64(@PathVariable("resourceName") String resourceName,
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("invoke: " + "/parameter/resources/" + resourceName + "/base64");
//		ApiCallResult callResult = new ApiCallResult();
		String fileStr = null;
		String path = config.getExcleLoggoCommonSource() + File.separator + resourceName;
		logger.info("Found the logo resource: " + path);
		if (!refresh) {
			logger.info("try to getCommonImagesBase64 from redis ...");
			fileStr = RedisUtil.hget("resourceCache", resourceName);
		}
		if (StringUtils.isBlank(fileStr)) {
			try {
				InputStream is = new FileInputStream(path);
				byte[] bytes = IOUtils.toByteArray(is);
				fileStr = Base64.encode(bytes);
				RedisUtil.hset("resourceCache", resourceName, fileStr, RedisUtil.HOUR * 24 * 90);
				logger.info("save to redis resourceCache successfully: " + resourceName);
//				callResult.setContent(fileStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
//				callResult.setMessage("Exception: " + e.toString());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.info("getCommonImagesBase64 from redis successfully");
//			callResult.setContent(fileStr);
		}
		return new ResponseEntity<>(fileStr, HttpStatus.OK);
	}

//	@Override
//	@RequestMapping(value = "/parameter/audit-preview/{fieldId}/base64", method = RequestMethod.GET)
//	public ResponseEntity<ApiCallResult> getAuditPreviewImagesBase64(@PathVariable("fieldId") String fieldId) {
//		logger.info("invoke: " + "/parameter/audit-preview/" + fieldId + "/base64");
//		ApiCallResult result = new ApiCallResult();
//        try {
//
//            String value = RedisUtil.hget("auditPreviewImages", fieldId);
//            List<String> ids = JSON.parseArray(value, String.class);
//            if (ids.size() > 0) {
////                for (String id : ids) {
////                    fileService.getFile(id);
////                }
//                result.setContent(ids);
//            } else {
//                result.setMessage("null result!!!!!!");
//            }
//        }catch (Exception e){
//
//        }
//		return new ResponseEntity<>(result, HttpStatus.OK);
//	}

}
