package org.motechproject.admin.security.repository;

import org.motechproject.admin.security.domain.MotechPermission;
import org.motechproject.admin.security.domain.MotechPermission;

import java.util.List;

/**
 * Interface for the permission repository, used for persisting permission objects.
 */
public interface AllMotechPermissions {

    void add(MotechPermission permission);

    MotechPermission findByPermissionName(String permissionName);

    List<MotechPermission> getPermissions();

    void delete(MotechPermission permission);
}
