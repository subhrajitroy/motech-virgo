package org.motechproject.admin.security.web.form;

public class ResetForm {

    public static final String PASSWORD = "password";
    public static final String PASSWORD_CONFIRMATION = "passwordConfirmation";

    private String token;
    private String password;
    private String passwordConfirmation;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
