package org.motechproject.admin.security.service;

import org.motechproject.admin.security.domain.MotechRole;
import org.motechproject.admin.security.domain.MotechRoleCouchdbImpl;
import org.motechproject.admin.security.ex.RoleHasUserException;
import org.motechproject.admin.security.model.RoleDto;
import org.motechproject.admin.security.domain.MotechRole;
import org.motechproject.admin.security.domain.MotechRoleCouchdbImpl;
import org.motechproject.admin.security.domain.MotechUser;
import org.motechproject.admin.security.ex.RoleHasUserException;
import org.motechproject.admin.security.model.RoleDto;
import org.motechproject.admin.security.repository.AllMotechRoles;
import org.motechproject.admin.security.repository.AllMotechUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service to manage roles in Motech
 *
 * @see org.motechproject.admin.security.domain.MotechRole
 */
@Service("motechRoleService")
public class MotechRoleServiceImpl implements MotechRoleService {

    @Autowired
    private AllMotechRoles allMotechRoles;

    @Autowired
    private AllMotechUsers allMotechUsers;

    @Autowired
    private UserContextService userContextsService;

    @Override
    public List<RoleDto> getRoles() {
        List<RoleDto> roles = new ArrayList<>();
        for (MotechRole role : allMotechRoles.getRoles()) {
            roles.add(new RoleDto(role));
        }
        return roles;
    }

    @Override
    public RoleDto getRole(String roleName) {
        MotechRole motechRole = allMotechRoles.findByRoleName(roleName);
        return motechRole != null ? new RoleDto(motechRole) : null;
    }

    @Override
    public void updateRole(RoleDto role) {
        MotechRole motechRole = allMotechRoles.findByRoleName(role.getOriginalRoleName());
        motechRole.setRoleName(role.getRoleName());
        motechRole.setPermissionNames(role.getPermissionNames());
        List<MotechUser> users = (List<MotechUser>) allMotechUsers.findByRole(role.getOriginalRoleName());

        for (MotechUser user : users) {
            List<String> roleList = user.getRoles();
            roleList.remove(role.getOriginalRoleName());
            roleList.add(role.getRoleName());
            allMotechUsers.update(user);
        }

        allMotechRoles.update(motechRole);
        userContextsService.refreshAllUsersContextIfActive();
    }

    @Override
    public void deleteRole(RoleDto role) {
        MotechRole motechRole = allMotechRoles.findByRoleName(role.getRoleName());
        if (motechRole.isDeletable()) {
            List<MotechUser> users = (List<MotechUser>) allMotechUsers.findByRole(role.getRoleName());
            if (!users.isEmpty()) {
                throw new RoleHasUserException("Role cannot be deleted because a user has the role.");
            }
            allMotechRoles.remove(motechRole);
            userContextsService.refreshAllUsersContextIfActive();
        }
    }

    @Override
    public void createRole(RoleDto role) {
        MotechRole motechRole = new MotechRoleCouchdbImpl(role.getRoleName(), role.getPermissionNames(),
                role.isDeletable());
        allMotechRoles.add(motechRole);
        userContextsService.refreshAllUsersContextIfActive();
    }
}
