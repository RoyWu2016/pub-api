package com.ai.api.controller.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.ai.api.bean.ChecklistSampleSize;
import com.ai.api.bean.ChecklistSampleSizeChildren;
import com.ai.api.bean.ChinaTimeBean;
import com.ai.api.bean.CountryBean;
import com.ai.api.bean.DropdownListOptionBean;
import com.ai.api.bean.ProductCategoryDtoBean;
import com.ai.api.bean.ProductFamilyDtoBean;
import com.ai.api.bean.TextileDropdownListOptionBean;
import com.ai.api.config.ServiceConfig;
import com.ai.api.controller.ParameterV2;
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
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@SuppressWarnings({ "unchecked", "rawtypes" })
@RestController
@Api(tags = { "Parameter V2" }, description = "Parameters V2 APIs")
public class ParameterV2Impl implements ParameterV2 {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());

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

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/server-time", method = RequestMethod.GET)
	@ApiOperation(value = "Get China Time API", response = ChinaTimeBean.class)
	public ResponseEntity<ApiCallResult> getChinaTime() {
		// TODO Auto-generated method stub
		ApiCallResult rest = new ApiCallResult();
		ChinaTimeBean chinaTimeBean = parameterService.getChinaTime();
		rest.setContent(chinaTimeBean);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/public-tests", method = RequestMethod.GET)
	@ApiOperation(value = "Get Checklist Public Test List API", response = CKLTestVO.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getChecklistPublicTestList(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		ApiCallResult rest = new ApiCallResult();
		List<CKLTestVO> result = parameterService.getChecklistPublicTestList(refresh);
		if (result == null) {
			logger.error("checklistTestList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		rest.setContent(result);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/public-defects", method = RequestMethod.GET)
	@ApiOperation(value = "Get Checklist Public Defect List API", response = CKLDefectVO.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getChecklistPublicDefectList(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		ApiCallResult rest = new ApiCallResult();
		List<CKLDefectVO> result = parameterService.getChecklistPublicDefectList(refresh);
		if (result == null) {
			logger.error("checklistTestList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		rest.setContent(result);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/product-categories", method = RequestMethod.GET)
	@ApiOperation(value = "Get ProductCategory List API", response = ProductCategoryDtoBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getProductCategoryList(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		ApiCallResult rest = new ApiCallResult();
		List<ProductCategoryDtoBean> result = parameterService.getProductCategoryList(refresh);
		if (result == null) {
			logger.error("Product category list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		rest.setContent(result);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/product-families", method = RequestMethod.GET)
	@ApiOperation(value = "Get ProductFamily List API", response = ProductFamilyDtoBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getProductFamilyList(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		ApiCallResult rest = new ApiCallResult();
		List<ProductFamilyDtoBean> result = parameterService.getProductFamilyList(refresh);
		if (result == null) {
			logger.error("Product family list not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		rest.setContent(result);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/parameter/v2/countries-with-calling-country-code", method = RequestMethod.GET)
	@ApiOperation(value = "Get Country List API", response = CountryBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getCountryList(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		ApiCallResult rest = new ApiCallResult();
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
		rest.setContent(result);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/checklist-test-sample-size-list", method = RequestMethod.GET)
	@ApiOperation(value = "Get Test Sample Size List API", response = ChecklistSampleSize.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getTestSampleSizeList(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		ApiCallResult rest = new ApiCallResult();
		Map<String, List<ChecklistTestSampleSizeBean>> resultMap = parameterService.getTestSampleSizeList(refresh);
		if (resultMap == null) {
			logger.error("TestSampleSizeList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<ChecklistSampleSize> result = this.changeMap2Bean(resultMap);
		rest.setContent(result);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/product-types", method = RequestMethod.GET)
	@ApiOperation(value = "Get ProductCategory List API", response = SysProductTypeBean.class, responseContainer = "Map")
	public ResponseEntity<ApiCallResult> getProductTypeList(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		ApiCallResult rest = new ApiCallResult();
		Map<String, Object> map = new HashMap<String, Object>();
		List<SysProductTypeBean> sysProductTypeBeanList = parameterService.getProductTypeList(refresh);
		if (sysProductTypeBeanList != null) {
			map.put("success", true);
			map.put("data", sysProductTypeBeanList);
			rest.setContent(map);
			return new ResponseEntity<>(rest, HttpStatus.OK);
		} else {
			logger.error("TestSampleSizeList not found");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/textile-product-categories", method = RequestMethod.GET)
	@ApiOperation(value = "Get Textile Product Category List API", response = TextileDropdownListOptionBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getTextileProductCategories(
			@ApiParam(value = "true or false", required = false) 
			@RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		logger.info("get getTextileProductCategory");
		ApiCallResult rest = new ApiCallResult();
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
		rest.setContent(list);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	@TokenSecured
	@RequestMapping(value = "/parameter/v2/ai-offices", method = RequestMethod.GET)
	@ApiOperation(value = "Get AiOffice List API", response = DropdownListOptionBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getAiOffices(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		// TODO Auto-generated method stub
		ApiCallResult rest = new ApiCallResult();
		List<ClassifiedBean> result = parameterService.getAiOffices(refresh);
		if (result == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		List<DropdownListOptionBean> list = new ArrayList<DropdownListOptionBean>();
		for (ClassifiedBean each : result) {
			DropdownListOptionBean bean = new DropdownListOptionBean();
			bean.setLabel(each.getKey());
			bean.setValue(each.getValue());

			list.add(bean);
		}
		rest.setContent(list);
		return new ResponseEntity<>(rest, HttpStatus.OK);
	}

	@Override
	// @TokenSecured
	@RequestMapping(value = "/parameter/v2/continents", method = RequestMethod.GET)
	@ApiOperation(value = "Get Continent List API", response = DropdownListOptionBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getContinents(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
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
	@RequestMapping(value = "/parameter/v2/country/{countryId}/cities", method = RequestMethod.GET)
	@ApiOperation(value = "Get Cities By CountryId API", response = DropdownListOptionBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getCitiesByCountryId(
			@ApiParam(value = "countryId", required = true) @PathVariable("countryId") String countryId,
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
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
	// @TokenSecured
	@RequestMapping(value = "/parameter/v2/country/{countryId}/provinces", method = RequestMethod.GET)
	@ApiOperation(value = "Get Provinces By CountryId API", response = DropdownListOptionBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getProvincesByCountryId(
			@ApiParam(value = "countryId", required = true) @PathVariable("countryId") String countryId,
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
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
	@RequestMapping(value = "/parameter/v2/continent/{continentId}/countries", method = RequestMethod.GET)
	@ApiOperation(value = "Get Countries By ContinentId API", response = DropdownListOptionBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getCountriesByContinentId(
			@ApiParam(value = "continentId", required = true) @PathVariable("continentId") String continentId,
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
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
	@RequestMapping(value = "/parameter/v2/province/{provinceId}/cities", method = RequestMethod.GET)
	@ApiOperation(value = "Get Cities By ProvinceId API", response = DropdownListOptionBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getCitiesByProvinceId(
			@ApiParam(value = "provinceId", required = true) @PathVariable("provinceId") String provinceId,
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
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
	@RequestMapping(value = "/parameter/v2/countries", method = RequestMethod.GET)
	@ApiOperation(value = "Get AllCountries List API", response = DropdownListOptionBean.class, responseContainer = "List")
	public ResponseEntity<ApiCallResult> getAllCountries(
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
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
	@RequestMapping(value = "/parameter/v2/sic/{sicId}/base64", method = RequestMethod.GET)
	@ApiOperation(value = "Get Sale Image API", response = String.class)
	public ResponseEntity<ApiCallResult> getSaleImage(
			@ApiParam(value = "sicId", required = true) @PathVariable("sicId") String sicId,
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {

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
	@RequestMapping(value = "/parameter/v2/{userName}/is-aca-user", method = RequestMethod.GET)
	@ApiOperation(value = "Check User Is ACA-User Or Not API", response = boolean.class)
	public ResponseEntity<ApiCallResult> isACAUser(
			@ApiParam(value = "userName", required = true) @PathVariable("userName") String userName) {
		logger.info("invoke: " + "/parameter/" + userName + "/is-aca-user");
		logger.info("check if userName exist: " + userName);
		ApiCallResult result = new ApiCallResult();
		boolean isUsrNameExist = parameterService.checkIfUserNameExist(userName);
		if (isUsrNameExist) {
			boolean isAcaUser = parameterService.isACAUser(userName);
			result.setContent(isAcaUser);
		} else {
			result.setMessage(userName + " does not exist.");
			;
		}
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@Override
	@RequestMapping(value = "/parameter/v2/resources/{resourceName}/base64", method = RequestMethod.GET)
	@ApiOperation(value = "Get Common Image Base64 String API", response = String.class)
	public ResponseEntity<ApiCallResult> getCommonImagesBase64(
			@ApiParam(value = "resourceName", required = true) @PathVariable("resourceName") String resourceName,
			@ApiParam(value = "true or false", required = false, defaultValue = "false") @RequestParam(value = "refresh", defaultValue = "false") boolean refresh) {
		logger.info("invoke: " + "/parameter/resources/" + resourceName + "/base64");
		ApiCallResult callResult = new ApiCallResult();
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
				// callResult.setContent(fileStr);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				// callResult.setMessage("Exception: " + e.toString());
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			logger.info("getCommonImagesBase64 from redis successfully");
			// callResult.setContent(fileStr);
		}
		callResult.setContent(fileStr);
		return new ResponseEntity<>(callResult, HttpStatus.OK);
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

}
