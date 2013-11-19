package org.motechproject.admin.security.service;

import org.motechproject.admin.security.ex.UserNotFoundException;
import org.motechproject.admin.security.ex.InvalidTokenException;
import org.motechproject.admin.security.ex.UserNotFoundException;
import org.motechproject.admin.security.password.NonAdminUserException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface PasswordRecoveryService {

    void passwordRecoveryRequest(String email) throws UserNotFoundException;

    void resetPassword(String token, String password, String passwordConfirmation) throws InvalidTokenException;

    void cleanUpExpiredRecoveries();

    boolean validateToken(String token);

    void oneTimeTokenOpenId(String email) throws UserNotFoundException, NonAdminUserException;

    void validateTokenAndLoginUser(String token, HttpServletRequest request, HttpServletResponse response) throws IOException;
}
