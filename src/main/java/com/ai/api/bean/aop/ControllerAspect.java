package com.ai.api.bean.aop;

import com.ai.api.service.UserService;
import com.ai.commons.Consts;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
	private static final Logger mainLogger = LoggerFactory.getLogger(ControllerAspect.class);

	@Autowired
	UserService userService; // Service which will do all data

    public Object aroundInController(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
	    String calledMethod = joinPoint.getSignature().toShortString();


	    Object[] args = joinPoint.getArgs();
	    String[] checkUserIdStr = args[0].toString().split(Consts.DASH);
	    //logic for handling super master account access master account data
	    if (checkUserIdStr.length == 2
			    && checkUserIdStr[1].length() == checkUserIdStr[0].length()) {
		    //super master user id should have same length as master account id
		    String superUserId = checkUserIdStr[0];
		    String masterUserId = checkUserIdStr[1];
		    //check the relation is valid
			if (userService.isMasterOfSuperMaster(superUserId, masterUserId)) {
				args[0] = checkUserIdStr[1];
				mainLogger.info("Super master access of user id" + args[0] + " working fine.");
			} else {
				mainLogger.error("Super master access of user id" + args[0] + " is not allowed!");
			}

	    }
	    Object output = joinPoint.proceed(args);
	    //modify passed super master account user id to master user id

        long elapsedTime = System.currentTimeMillis() - start;

	    //log should be delimited by ; to be easily imported to excel for analysis
        logger.info(calledMethod + Consts.SEMICOLON + elapsedTime);
        return output;
    }

}
