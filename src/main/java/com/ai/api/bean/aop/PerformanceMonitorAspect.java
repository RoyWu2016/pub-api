package com.ai.api.bean.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class PerformanceMonitorAspect {

	private static final Logger logger = LoggerFactory.getLogger(PerformanceMonitorAspect.class);

	@Pointcut("execution(* com.ai.api.service.impl.UserServiceImpl.*(..))")
//	@Pointcut("execution(* com.ai.api.controller.impl..*(..))")
//	@Pointcut("within (com.ai.api.controller.impl..*)")

//	@Pointcut(" execution(* com.ai.api..controller..*(..))" )
//	@Pointcut("execution(* com.kb.SimpleCalculator.*(..))")
	public void pointcutName() {
	}

	@Around("pointcutName()")//applying pointcut on before advice
	public Object myAdvice(ProceedingJoinPoint jp) {
		System.out.println("Around -- Before the method " + jp.getSignature().getName() + " execution");
		logger.error("======================================== sssss ==================");
		Object[] args = null;
		Object value = null;
		try {
			args = jp.getArgs();
//			if((Integer)args[0] == 10){
			value = jp.proceed();
//			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
		logger.error("======================================== aaaaa ==================");
		System.out.println("Around -- After the method " + jp.getSignature().getName() + " execution");
		return value;
	}

}
