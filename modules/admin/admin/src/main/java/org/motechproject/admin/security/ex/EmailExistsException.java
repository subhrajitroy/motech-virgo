package org.motechproject.admin.security.ex;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException() {
    }

    public EmailExistsException(String message) {
        super(message);
    }
}
