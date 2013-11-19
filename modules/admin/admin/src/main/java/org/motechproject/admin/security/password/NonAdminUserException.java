package org.motechproject.admin.security.password;

public class NonAdminUserException extends Exception {

    public NonAdminUserException() {
        super();
    }

    public NonAdminUserException(String message) {
        super(message);
    }
}
