package com.ai.api.exception;

public class AIDateException extends AIException {
    private static final long serialVersionUID = -7026214214790018100L;

    public AIDateException() {
        super(AIDateException.class);
    }

    public AIDateException(String businessMessage, String detailMessage) {
        super(AIDateException.class, businessMessage, detailMessage);
    }

    public AIDateException(String businessMessage) {
        super(AIDateException.class, businessMessage);
    }

}
