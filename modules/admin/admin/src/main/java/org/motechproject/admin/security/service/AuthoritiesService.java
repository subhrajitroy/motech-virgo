package org.motechproject.admin.security.service;

import org.motechproject.admin.security.domain.MotechUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

/**
 * Service interface to retrieve authorities(permissions) for a given MotechUser
 */
public interface AuthoritiesService {

    List<GrantedAuthority> authoritiesFor(MotechUser user);
}
