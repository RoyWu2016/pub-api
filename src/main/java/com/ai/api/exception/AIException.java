package com.ai.api.exception;

import org.apache.log4j.Logger;

import java.util.Arrays;

public class AIException extends Exception {
    private static final long serialVersionUID = 7771754331477529507L;
    private static Logger LOGGER = null;

    public AIException() {
        super();
        Logger.getLogger(AIException.class);
    }

    public AIException(Class<?> exceptionClass) {
        super();
        LOGGER = Logger.getLogger(exceptionClass);
    }

    public AIException(Class<?> exceptionClass, String businessMessage) {
        super(businessMessage);
        LOGGER = Logger.getLogger(exceptionClass);
        LOGGER.error(businessMessage);
    }

    public AIException(String businessMessage) {
        super(businessMessage);
        LOGGER = Logger.getLogger(AIException.class);
        LOGGER.error(businessMessage);
    }

    public AIException(String businessMessage, String detailMessage) {
        super(businessMessage);
        LOGGER = Logger.getLogger(AIException.class);
        LOGGER.error(businessMessage);
        LOGGER.error(detailMessage);
    }

    public AIException(Class<?> exceptionClass, String businessMessage, String detailMessage) {
        super(businessMessage);
        LOGGER = Logger.getLogger(exceptionClass);
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
        LOGGER = Logger.getLogger(exceptionClass);
        LOGGER.error(e.fillInStackTrace());
        LOGGER.error(Arrays.toString(e.getStackTrace()));
        LOGGER.error("------------print------------");
        e.printStackTrace();
    }
}
