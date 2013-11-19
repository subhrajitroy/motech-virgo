package org.motechproject.admin.security.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;

import java.util.List;

/**
 * Represent Motech user roles, with persistence in CouchDB.
 */
@TypeDiscriminator("doc.type == 'MotechRole'")
public class MotechRoleCouchdbImpl extends MotechBaseDataObject implements MotechRole {

    private static final long serialVersionUID = 7042718621913820992L;

    public static final String DOC_TYPE = "MotechRole";

    @JsonProperty
    private String roleName;

    @JsonProperty
    private List<String> permissionNames;

    @JsonProperty
    private boolean deletable;

    public MotechRoleCouchdbImpl() {
        super();
        this.setType(DOC_TYPE);
    }

    public MotechRoleCouchdbImpl(String roleName, List<String> permissionNames, boolean deletable) {
        super();
        this.roleName = roleName;
        this.permissionNames = permissionNames;
        this.setType(DOC_TYPE);
        this.deletable = deletable;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<String> getPermissionNames() {
        return permissionNames;
    }

    public void setPermissionNames(List<String> permissionNames) {
        this.permissionNames = permissionNames;
    }

    public boolean isDeletable() {
        return deletable;
    }

    public void setDeletable(boolean deletable) {
        this.deletable = deletable;
    }

    @Override
    public void removePermission(String permissionName) {
        if (permissionNames != null) {
            permissionNames.remove(permissionName);
        }
    }

    @Override
    public boolean hasPermission(String permissionName) {
        if (permissionNames != null) {
            return permissionNames.contains(permissionName);
        }
        return false;
    }


}
