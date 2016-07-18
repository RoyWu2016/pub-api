package com.ai.api.exception;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AIException extends Exception {
    private static final long serialVersionUID = 7771754331477529507L;
    private static Logger LOGGER = null;

    public AIException() {
        super();
        LoggerFactory.getLogger(AIException.class);
    }

    public AIException(Class<?> exceptionClass) {
        super();
        LOGGER = LoggerFactory.getLogger(exceptionClass);
    }

    public AIException(Class<?> exceptionClass, String businessMessage) {
        super(businessMessage);
        LOGGER = LoggerFactory.getLogger(exceptionClass);
        LOGGER.error(businessMessage);
    }

    public AIException(String businessMessage) {
        super(businessMessage);
        LOGGER = LoggerFactory.getLogger(AIException.class);
        LOGGER.error(businessMessage);
    }

    public AIException(String businessMessage, String detailMessage) {
        super(businessMessage);
        LOGGER = LoggerFactory.getLogger(AIException.class);
        LOGGER.error(businessMessage);
        LOGGER.error(detailMessage);
    }

    public AIException(Class<?> exceptionClass, String businessMessage, String detailMessage) {
        super(businessMessage);
        LOGGER = LoggerFactory.getLogger(exceptionClass);
        LOGGER.error(businessMessage);
        LOGGER.error(detailMessage);
    }

    /**
     * AI exception
     *
     * @param exceptionClass Customer class where the exception occurred
     * @param msg            Exception message
     * @param e              Throwable object of the exception thrown
     */
    public AIException(Class<?> exceptionClass, String msg, Throwable e) {
        super(msg, e);
        LOGGER = LoggerFactory.getLogger(exceptionClass);
        LOGGER.error(ExceptionUtils.getStackTrace(e));
        e.printStackTrace();
    }
}
