package org.motechproject.security.web.controllers;

import org.motechproject.security.model.RoleDto;
import org.motechproject.security.service.MotechRoleService;
import org.motechproject.security.ex.RoleHasUserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.Writer;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * Handle HTTP requests from web clients for Manage Roles user interface.
 */
@Controller
public class RoleController {

    @Autowired
    private MotechRoleService motechRoleService;

    @RequestMapping(value = "/roles", method = RequestMethod.GET)
    @ResponseBody
    public List<RoleDto> getRoles() {
        return motechRoleService.getRoles();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/roles/getrole", method = RequestMethod.POST)
    @ResponseBody
    public RoleDto getRole(@RequestBody String roleName) {
        return motechRoleService.getRole(roleName);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/roles/update", method = RequestMethod.POST)
    public void updateRole(@RequestBody RoleDto role) {
        motechRoleService.updateRole(role);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/roles/delete", method = RequestMethod.POST)
    public void deleteRole(@RequestBody RoleDto role) {
        motechRoleService.deleteRole(role);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/roles/create", method = RequestMethod.POST)
    public void saveRole(@RequestBody RoleDto role) {
        motechRoleService.createRole(role);
    }

    @ExceptionHandler(RoleHasUserException.class)
    public void handleRoleHasUserException(HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_CONFLICT);

        try (Writer writer = response.getWriter()) {
            writer.write("key:security.roleHasUserException");
        }
    }
}
