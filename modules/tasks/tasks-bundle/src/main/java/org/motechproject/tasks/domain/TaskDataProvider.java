package org.motechproject.tasks.domain;

import org.ektorp.support.TypeDiscriminator;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;

/**
 * The <code>TaskDataProvider</code> class cointains all informations about data providers used in tasks
 */


@TypeDiscriminator("doc.type == 'TaskDataProvider'")
public class TaskDataProvider extends MotechBaseDataObject {
    private static final long serialVersionUID = -5427486904165895928L;

    private String name;
    private List<TaskDataProviderObject> objects;

    public TaskDataProvider() {
        this(null, new ArrayList<TaskDataProviderObject>());
    }

    public TaskDataProvider(String name, List<TaskDataProviderObject> objects) {
        this.name = name;
        this.objects = objects == null ? new ArrayList<TaskDataProviderObject>() : objects;
    }

    public boolean containsProviderObject(String type) {
        boolean found = false;

        for (TaskDataProviderObject object : getObjects()) {
            if (equalsIgnoreCase(object.getType(), type)) {
                found = true;
                break;
            }
        }

        return found;
    }

    public boolean containsProviderObjectLookup(String type, String lookupField) {
        TaskDataProviderObject providerObject = getProviderObject(type);

        if (providerObject != null) {
            for (LookupFieldsParameter lookup : providerObject.getLookupFields()) {
                if (lookup.getDisplayName().equals(lookupField)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean containsProviderObjectField(String type, String fieldKey) {
        TaskDataProviderObject providerObject = getProviderObject(type);

        if (providerObject != null) {
            for (FieldParameter fieldParameter : providerObject.getFields()) {
                if (fieldParameter.getFieldKey().equals(fieldKey)) {
                    return true;
                }
            }
        }
        return false;
    }

    public TaskDataProviderObject getProviderObject(String type) {
        TaskDataProviderObject found = null;

        for (TaskDataProviderObject object : getObjects()) {
            if (equalsIgnoreCase(object.getType(), type)) {
                found = object;
                break;
            }
        }

        return found;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TaskDataProviderObject> getObjects() {
        return objects;
    }

    public void setObjects(List<TaskDataProviderObject> objects) {
        this.objects.clear();

        if (objects != null) {
            this.objects.addAll(objects);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final TaskDataProvider other = (TaskDataProvider) obj;

        return Objects.equals(this.name, other.name) && Objects.equals(this.objects, other.objects);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, objects);
    }

    @Override
    public String toString() {
        return String.format("TaskDataProvider{name='%s', objects=%s}", name, objects);
    }

    public String getKeyType(String key) {
        String type = "UNKNOWN";

        for (TaskDataProviderObject object : getObjects()) {
            for (FieldParameter fieldParameter : object.getFields()) {
                if (equalsIgnoreCase(fieldParameter.getFieldKey(), key)) {
                    type = fieldParameter.getType().toString();
                    break;
                }
            }
        }

        return type;
    }
}
