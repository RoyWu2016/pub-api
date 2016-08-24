package com.ai.api.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.concurrent.locks.ReentrantLock;

/***************************************************************************
 * <PRE>
 * Project Name    : Public-API
 * <p>
 * Package Name    : com.ai.api.util
 * <p>
 * Creation Date   : 2016/7/18 16:33
 * <p>
 * Author          : Jianxiong Cai
 * <p>
 * Purpose         : TODO
 * <p>
 * <p>
 * History         : TODO
 * <p>
 * </PRE>
 ***************************************************************************/

public class RedisUtil {

	protected static Logger logger = LoggerFactory.getLogger(RedisUtil.class);

	protected static ReentrantLock lockPool = new ReentrantLock();
	protected static ReentrantLock lockJedis = new ReentrantLock();

	private static RedisUtil instance ;
	private static Jedis jedis;
	//private JedisPool pool = null;
	private static JedisPool pool = null;

	public RedisUtil(){
		/*
		try{
//			jedis = new Jedis("202.66.128.138", 6379);
//			jedis.auth("aiitteam");
//			jedis.get("testKey");
			if (pool == null) {
				JedisPoolConfig config = new JedisPoolConfig();
				config.setMaxTotal(500);
				config.setMaxIdle(5);
				config.setMaxWaitMillis(1000 * 100);
				config.setTestOnBorrow(true);
				pool = new JedisPool(config, "202.66.128.138", 6379, 100000,"aiitteam");
			}
			jedis = pool.getResource();
		}catch(Exception e){

		}
		*/
		jedis = this.getJedis();
	}

	/** * 初始化Redis连接池 */
	private static void initialPool(){
		try {
			JedisPoolConfig config = new JedisPoolConfig();
			//config.setMaxTotal(-1);
			//config.setMaxIdle(8);
			//config.setMaxWaitMillis(100000);
			config.setTestOnBorrow(true);
			pool = new JedisPool(config, "202.66.128.138", 6379, 100000,"aiitteam");
		} catch (Exception e) {
			logger.error("First create JedisPool error : "+e);
		}
	}

	/** * 在多线程环境同步初始化 */
	private static synchronized void poolInit() {
		//断言 ，当前锁是否已经锁住，如果锁住了，就啥也不干，没锁的话就执行下面步骤
		assert ! lockPool.isHeldByCurrentThread();
		lockPool.lock();
		try {
			if (pool == null) {
				initialPool();
			}
		}catch(Exception e){
			e.printStackTrace();
		} finally {
			lockPool.unlock();
		}
	}

	/** * 同步获取Jedis实例 * @return Jedis */
	public synchronized static Jedis getJedis() {
		assert ! lockJedis.isHeldByCurrentThread();
		lockJedis.lock();

		if (pool == null) {
			poolInit();
		}
		Jedis jedis2 = null;
		try {
			if (pool != null) {
				jedis2 = pool.getResource();
			}
		} catch (Exception e) {
			logger.error("Get jedis error : "+e);
		}finally{
			returnResource(jedis2);
			lockJedis.unlock();
		}
		return jedis2;
	}

	/** * 释放jedis资源 * @param jedis */
	public static void returnResource(final Jedis jedis) {
		if (jedis != null && pool !=null) {
			pool.returnResource(jedis);
		}
	}


	public static synchronized RedisUtil getInstance() {
		if(instance == null){
			instance = new RedisUtil();
		}
		return instance;
	}

	public boolean exists(String key){
		return jedis.exists(key.trim());
	}
	/**
	 * Set the string value as value of the key. The string can't be longer than 1073741824 bytes (1GB).
	 * @param key
	 * @param value
	 * @return
	 */
	public String set( String key,  String value){
		return jedis.set(key.trim(), value.trim());
	}
	/**
	 * Get the value of the specified key. If the key does not exist null is returned. If the value
	 * stored at key is not a string an error is returned because GET can only handle string values.
	 * @param key
	 * @return
	 */
	public String get(String key){
		if (!this.exists(key.trim())) return null;
		return String.valueOf((jedis.get(key.trim())));
	}

	/**
	 * Remove the specified keys. If a given key does not exist no operation is performed for this
	 * key. The command returns the number of keys removed.
	 * @param key
	 */
	public Long del(String key){
		return jedis.del(key.trim());
	}

	/**
	 *
	 * Set the specified hash field to the specified value.
	 * If key does not exist, a new key holding a hash is created.
	 * @param key
	 * @param fieId
	 * @param value
	 * @return
	 */
	public Long hset(String key,String fieId,String value){
			return jedis.hset(key.trim(),fieId.trim(),value.trim());
	}

	/**
	 * If key holds a hash, retrieve the value associated to the specified field.
	 * If the field is not found or the key does not exist, a special 'nil' value is returned.
	 * @param key
	 * @param fieId
	 * @return
	 */
	public String hget(String key,String fieId){
		if (!this.exists(key.trim())) return null;
		return jedis.hget(key.trim(),fieId.trim());
	}

	/**
	 * Remove the specified field from an hash stored at key.
	 * If the field was present in the hash it is deleted and 1 is returned,
	 * otherwise 0 is returned and no operation is performed.
	 * @param key
	 */
	public Long hdel(String key,String fieId){
		return jedis.hdel(key.trim(),fieId.trim());
	}

	private void flushAll(){
		jedis.flushAll();
	}






	public static void main(String[] args) {
		RedisUtil ru = RedisUtil.getInstance();
		ru.set("testKey", "helloWord!");
//		ru.flushAll();
		System.out.println(ru.get("testKey"));
	}
}
