package com.ai.api.exception;

/**
 * Exception which should be thrown when somebody does not have a Permission to access to a
 * Doc/File.
 *
 * @author Bastien Lardy
 */
public class AIPermissionException extends AIException {
    private static final long serialVersionUID = 3356504373462395870L;

    public AIPermissionException() {
        super(AIPermissionException.class);
    }

    public AIPermissionException(String businessMessage, String detailMessage) {
        super(AIPermissionException.class, businessMessage, detailMessage);
    }

    public AIPermissionException(String businessMessage) {
        super(AIPermissionException.class, businessMessage);
    }

    public AIPermissionException(Boolean readAccess, Boolean writeAccess, String login,
                                 String docuid, String serverName) {

        super(AIPermissionException.class,
                (readAccess == true) ? "No permission access to this document."
                        : "You cannot access to this document.", "The user " + login
                        + " does not have permission on the document " + docuid + " on the server "
                        + serverName + ". ReadAccess: " + readAccess + ", WriteAccess: " + writeAccess);
    }

}
