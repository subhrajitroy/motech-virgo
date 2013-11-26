package org.motechproject.tasks.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;
import org.motechproject.commons.couchdb.dao.MotechBaseRepository;
import org.motechproject.tasks.domain.TaskDataProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@View(name = "by_name", map = "function(doc) { if(doc.type === 'TaskDataProvider') emit(doc.name); }")
public class AllTaskDataProviders extends MotechBaseRepository<TaskDataProvider> {

    @Autowired
    public AllTaskDataProviders(@Qualifier("taskDbConnector") final CouchDbConnector connector) {
        super(TaskDataProvider.class, connector);
    }

    public boolean addOrUpdate(TaskDataProvider entity) {
        TaskDataProvider provider = byName(entity.getName());
        boolean exists = provider != null;

        if (exists) {
            provider.setName(entity.getName());
            provider.setObjects(entity.getObjects());

            update(provider);
        } else {
            add(entity);
        }

        return exists;
    }

    public TaskDataProvider byName(String name) {
        List<TaskDataProvider> providers = queryView("by_name", name);
        return providers.isEmpty() ? null : providers.get(0);
    }
}
