package com.ai.api.exception;

public class AIValidationException extends AIException {
    private static final long serialVersionUID = -7592809568401331613L;

    public AIValidationException() {
        super(AIValidationException.class);
    }

    public AIValidationException(String businessMessage, String detailMessage) {
        super(AIValidationException.class, businessMessage, detailMessage);
    }

    public AIValidationException(String businessMessage) {
        super(AIValidationException.class, businessMessage);
    }
}
