package org.motechproject.admin.security.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;

import java.util.List;
import java.util.Locale;

/**
 * CouchDb implementation of the {@link MotechUser}.
 */
@TypeDiscriminator("doc.type == 'MotechUser'")
public class MotechUserCouchdbImpl extends MotechBaseDataObject implements MotechUser {

    private static final long serialVersionUID = -5558028600313328842L;

    public static final String DOC_TYPE = "MotechUser";

    @JsonProperty
    private String externalId;

    @JsonProperty
    private String userName;

    @JsonProperty
    private String password;

    @JsonProperty
    private String email;

    @JsonProperty
    private List<String> roles;

    @JsonProperty
    private boolean active;

    @JsonProperty
    private String openId;

    @JsonProperty
    private Locale locale;

    public MotechUserCouchdbImpl() {
        super();
        this.setType(DOC_TYPE);
    }

    public MotechUserCouchdbImpl(String userName, String password, String email, String externalId, List<String> roles, String openId, Locale locale) {
        super();
        this.userName = userName == null ? null : userName.toLowerCase();
        this.password = password;
        this.email = email;
        this.externalId = externalId;
        this.roles = roles;
        this.active = true;
        this.openId = openId;
        this.locale = locale;
        this.setType(DOC_TYPE);
    }

    public String getExternalId() {
        return externalId;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getRoles() {
        return roles;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) { return true; }
        if (o == null || getClass() != o.getClass()) { return false; }

        MotechUserCouchdbImpl that = (MotechUserCouchdbImpl) o;

        if (!userName.equals(that.userName)) { return false; }

        return true;
    }

    @Override
    public int hashCode() {
        return userName.hashCode();
    }

}
