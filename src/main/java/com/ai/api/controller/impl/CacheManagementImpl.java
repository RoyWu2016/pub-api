package com.ai.api.controller.impl;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ai.api.controller.CacheManagement;
import com.ai.api.util.RedisUtil;
import com.ai.commons.annotation.TokenSecured;
@RestController
public class CacheManagementImpl implements CacheManagement {
	

	@Override
	@RequestMapping(value = "/cache/key/{keyName}", method = RequestMethod.GET)
	public String getCacheByKey(@PathVariable final String keyName) {
		return RedisUtil.get(keyName);
	}

	@Override
	@RequestMapping(value = "/cache/key/{keyName}/delete", method = RequestMethod.GET)
	public String deleteCacheByKey(@PathVariable final String keyName) {
		long val = RedisUtil.del(keyName);
		if(val>0){
			return "cache "+keyName+" deleted";
		}else{
			return "keyName does not exist";
		}
	}

	@Override
	@RequestMapping(value = "/hashed-cache/hash/{hashName}/key/{keyName}", method = RequestMethod.GET)
	public String getHashedCacheByHashAndKey(@PathVariable final String hashName,@PathVariable final String keyName) {
		return RedisUtil.hget(hashName, keyName);
	}

	@Override
	@RequestMapping(value = "/hashed-cache/hash/{hashName}/key/{keyName}/delete", method = RequestMethod.GET)
	public String deleteHashedCacheByHashAndKey(@PathVariable final String hashName, @PathVariable final String keyName) {
		long val = RedisUtil.hdel(hashName, keyName);
		if(val>0){
			return "hashed cache "+hashName+" key "+keyName+" deleted";
		}else{
			return "hashed cache "+hashName+" key "+keyName+" not found";
		}
	}

}
