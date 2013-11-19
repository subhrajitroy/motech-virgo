package org.motechproject.admin.security.service;

import org.motechproject.admin.security.domain.MotechUserProfile;
import org.motechproject.admin.security.model.UserDto;
import org.motechproject.admin.security.domain.MotechUserProfile;
import org.motechproject.admin.security.model.UserDto;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Locale;

/**
 * Service interface that defines APIs to retrieve and manage user details
 */
public interface MotechUserService {

    void register(String username, String password, String email, String externalId, List<String> roles, Locale locale);

    @PreAuthorize("hasRole('addUser')")
    void register(String username, String password, String email, String externalId, List<String> roles, Locale locale, boolean isActive, String openId);

    @PreAuthorize("hasRole('activateUser')")
    void activateUser(String username);

    MotechUserProfile retrieveUserByCredentials(String username, String password);

    MotechUserProfile changePassword(String username, String oldPassword, String newPassword);

    boolean hasUser(String username);

    @PreAuthorize("hasRole('manageUser')")
    List<MotechUserProfile> getUsers();

    @PreAuthorize("hasRole('editUser')")
    UserDto getUser(String userName);

    UserDto getUserByEmail(String email);

    Locale getLocale(String userName);

    @PreAuthorize("hasRole('manageUser')")
    List<MotechUserProfile> getOpenIdUsers();

    void updateUserDetailsWithoutPassword(UserDto user);

    void updateUserDetailsWithPassword(UserDto user);

    @PreAuthorize("hasRole('deleteUser')")
    void deleteUser(UserDto user);

    void sendLoginInformation(String userName, String password);

    void setLocale(String userName, Locale locale);

    List<String> getRoles(String userName);

}
