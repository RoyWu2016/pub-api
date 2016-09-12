package com.ai.api.controller;

public interface CacheManagement {
	
	String getCacheByKey(String keyName);
	String deleteCacheByKey(String keyName);
	String getHashedCacheByHashAndKey(String hashName, String keyName);
	String deleteHashedCacheByHashAndKey(String hashName, String keyName);
}
