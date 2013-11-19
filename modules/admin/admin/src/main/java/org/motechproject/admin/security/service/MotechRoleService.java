package org.motechproject.admin.security.service;

import org.motechproject.admin.security.model.RoleDto;

import java.util.List;

public interface MotechRoleService {

    List<RoleDto> getRoles();

    RoleDto getRole(String roleName);

    void updateRole(RoleDto role);

    void deleteRole(RoleDto role);

    void createRole(RoleDto role);
}
