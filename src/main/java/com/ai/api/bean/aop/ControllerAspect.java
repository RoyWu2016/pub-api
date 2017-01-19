package com.ai.api.bean.aop;

import com.ai.commons.Consts;
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
public class ControllerAspect {

    private static Logger logger = LoggerFactory.getLogger("CONTROLLER_LOGGER");

	private StringBuilder sb = new StringBuilder();


    public Object aroundInController(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
	    String calledMethod = joinPoint.getSignature().toShortString();

        Object output = joinPoint.proceed();
//        Object[] args = joinPoint.getArgs();
        long elapsedTime = System.currentTimeMillis() - start;

	    //log should be delimited by ; to be easily imported to excel for analysis
        logger.info(sb.append(calledMethod).
		        append(Consts.SEMICOLON).
		        append(elapsedTime).toString());
        return output;
    }

}
