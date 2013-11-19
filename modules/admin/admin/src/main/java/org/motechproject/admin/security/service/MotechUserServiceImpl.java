package org.motechproject.admin.security.service;

import org.motechproject.admin.security.authentication.MotechPasswordEncoder;
import org.motechproject.admin.security.domain.MotechUser;
import org.motechproject.admin.security.domain.MotechUserCouchdbImpl;
import org.motechproject.admin.security.domain.MotechUserProfile;
import org.motechproject.admin.security.email.EmailSender;
import org.motechproject.admin.security.model.UserDto;
import org.motechproject.admin.security.repository.AllMotechUsers;
import org.motechproject.admin.security.authentication.MotechPasswordEncoder;
import org.motechproject.admin.security.domain.MotechUser;
import org.motechproject.admin.security.domain.MotechUserCouchdbImpl;
import org.motechproject.admin.security.domain.MotechUserProfile;
import org.motechproject.admin.security.email.EmailSender;
import org.motechproject.admin.security.model.UserDto;
import org.motechproject.admin.security.repository.AllMotechUsers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Implementation of MotechUserService. Allows to search and manage users.
 *
 * @see MotechUserService
 */
@Service("motechUserService")
public class MotechUserServiceImpl implements MotechUserService {

    @Autowired
    private AllMotechUsers allMotechUsers;

    @Autowired
    private MotechPasswordEncoder passwordEncoder;

    @Autowired
    private EmailSender emailSender;

    @Autowired
    private UserContextService userContextsService;

    @Override
    public void register(String username, String password, String email, String externalId, List<String> roles, Locale locale) {
        this.register(username, password, email, externalId, roles, locale, true, "");
    }

    @Override
    public void register(String username, String password, String email, String externalId, List<String> roles, Locale locale, boolean isActive, String openId) {
        if (isBlank(username) || isBlank(password)) {
            throw new IllegalArgumentException("Username or password cannot be empty");
        }

        String encodePassword = passwordEncoder.encodePassword(password);
        MotechUserCouchdbImpl user = new MotechUserCouchdbImpl(username, encodePassword, email, externalId, roles, openId, locale);
        user.setActive(isActive);
        allMotechUsers.add(user);
    }

    @Override
    public void activateUser(String username) {
        MotechUser motechUser = allMotechUsers.findByUserName(username);
        if (motechUser != null) {
            motechUser.setActive(true);
            allMotechUsers.update(motechUser);
        }
    }

    @Override
    public MotechUserProfile retrieveUserByCredentials(String username, String password) {
        MotechUser user = allMotechUsers.findByUserName(username);
        if (user != null && passwordEncoder.isPasswordValid(user.getPassword(), password)) {
            return new MotechUserProfile(user);
        }
        return null;
    }

    @Override
    public MotechUserProfile changePassword(String username, String oldPassword, String newPassword) {
        MotechUser motechUser = allMotechUsers.findByUserName(username);
        if (motechUser != null && passwordEncoder.isPasswordValid(motechUser.getPassword(), oldPassword)) {
            motechUser.setPassword(passwordEncoder.encodePassword(newPassword));
            allMotechUsers.update(motechUser);
            return new MotechUserProfile(motechUser);
        }
        return null;
    }

    @Override
    public boolean hasUser(String username) {
        return allMotechUsers.findByUserName(username) != null;
    }

    @Override
    public List<MotechUserProfile> getUsers() {
        List<MotechUserProfile> users = new ArrayList<>();
        for (MotechUser user : allMotechUsers.getUsers()) {
            users.add(new MotechUserProfile(user));
        }
        return users;
    }

    @Override
    public UserDto getUser(String userName) {
        MotechUser user = allMotechUsers.findByUserName(userName);
        return new UserDto(user);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        MotechUser user = allMotechUsers.findUserByEmail(email);
        return user == null ? null : new UserDto(user);
    }

    @Override
    public Locale getLocale(String userName) {
        return allMotechUsers.findByUserName(userName).getLocale();
    }

    @Override
    public List<MotechUserProfile> getOpenIdUsers() {
        List<MotechUserProfile> users = new ArrayList<>();
        for (MotechUser user : allMotechUsers.getOpenIdUsers()) {
            users.add(new MotechUserProfile(user));
        }
        return users;
    }

    @Override
    public void updateUserDetailsWithoutPassword(UserDto user) {
        MotechUser motechUser = allMotechUsers.findByUserName(user.getUserName());
        motechUser.setEmail(user.getEmail());
        motechUser.setActive(user.isActive());
        motechUser.setPassword(user.getPassword());
        motechUser.setRoles(user.getRoles());
        motechUser.setLocale(user.getLocale());
        allMotechUsers.update(motechUser);
        userContextsService.refreshUserContextIfActive(motechUser.getUserName());

    }

    @Override
    public void updateUserDetailsWithPassword(UserDto user) {
        MotechUser motechUser = allMotechUsers.findByUserName(user.getUserName());
        motechUser.setEmail(user.getEmail());
        motechUser.setActive(user.isActive());
        if (!"".equals(user.getPassword())) {
            motechUser.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        motechUser.setRoles(user.getRoles());
        motechUser.setLocale(user.getLocale());
        allMotechUsers.update(motechUser);
        userContextsService.refreshUserContextIfActive(motechUser.getUserName());

    }

    @Override
    public void deleteUser(UserDto user) {
        MotechUser motechUser = allMotechUsers.findByUserName(user.getUserName());
        allMotechUsers.remove(motechUser);
    }

    @Override
    public void sendLoginInformation(String userName, String password) {
        MotechUser user = allMotechUsers.findByUserName(userName);
        emailSender.sendLoginInfo(user, password);
    }

    @Override
    public void setLocale(String userName, Locale locale) {
        MotechUser user = allMotechUsers.findByUserName(userName);
        user.setLocale(locale);
        updateUserDetailsWithoutPassword(new UserDto(user));
    }

    @Override
    public List<String> getRoles(String userName) {
        return allMotechUsers.findByUserName(userName).getRoles();
    }

}

