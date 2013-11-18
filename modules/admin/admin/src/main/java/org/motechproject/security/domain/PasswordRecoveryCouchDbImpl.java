package org.motechproject.security.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;
import org.motechproject.commons.date.util.DateUtil;

import java.util.Locale;

/**
 * CouchDb implementation of {@link PasswordRecovery}.
 */
@TypeDiscriminator("doc.type == 'PasswordRecovery'")
public class PasswordRecoveryCouchDbImpl extends MotechBaseDataObject implements PasswordRecovery {

    private static final long serialVersionUID = -6849774668390898927L;

    public static final String DOCTYPE = "PasswordRecovery";

    @JsonProperty
    private String token;

    @JsonProperty
    private String username;

    @JsonProperty
    private String email;

    @JsonProperty
    private DateTime expirationDate;

    @JsonProperty
    private Locale locale;

    public PasswordRecoveryCouchDbImpl() {
        super();
        setType(DOCTYPE);
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public DateTime getExpirationDate() {
        return DateUtil.setTimeZoneUTC(expirationDate);
    }

    @Override
    public void setExpirationDate(DateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
