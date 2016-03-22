package com.ai.api.exception;

/**
 * AIAuthException is used for any authentication exception
 *
 * @see AIException
 */
public class AIAuthException extends AIException {

    private static final long serialVersionUID = 1971573718811036845L;

    public AIAuthException() {
        super(AIAuthException.class);
    }

    public AIAuthException(String businessMessage) {
        super(AIAuthException.class, businessMessage);
    }
}
