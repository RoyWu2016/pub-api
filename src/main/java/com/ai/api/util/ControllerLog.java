package com.ai.api.util;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * Project Name    : Public-API
 * Package Name    : com.ai.api.util
 * Creation Date   : 2017/1/16 11:32
 * Author          : Jianxiong.Cai
 * Purpose         : TODO
 * History         : TODO
 */
public class ControllerLog {

    private static Logger logger = LoggerFactory.getLogger("CONTROLLER_LOGGER");

    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object output = joinPoint.proceed();
        String calledMethod = joinPoint.getSignature().toShortString();
//        Object[] args = joinPoint.getArgs();
        long elapsedTime = System.currentTimeMillis() - start;
        logger.info(calledMethod+"-------------"+elapsedTime);
        return output;
    }

}
