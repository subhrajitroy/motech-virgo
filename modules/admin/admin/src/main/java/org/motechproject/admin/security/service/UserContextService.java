package org.motechproject.admin.security.service;

/**
 * Interface to refresh user context (all or specified username)
 */
public interface UserContextService {

    void refreshAllUsersContextIfActive();

    void refreshUserContextIfActive(String userName);
}
