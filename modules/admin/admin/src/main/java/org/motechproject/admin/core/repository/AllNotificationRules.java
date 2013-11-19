package org.motechproject.admin.core.repository;

import org.ektorp.CouchDbConnector;
import org.motechproject.admin.core.domain.NotificationRule;
import org.motechproject.commons.couchdb.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * CouchDb repository for {@link NotificationRule}s.
 */
@Repository
public class AllNotificationRules extends MotechBaseRepository<NotificationRule> {

    @Autowired
    protected AllNotificationRules(@Qualifier("adminDbConnector") CouchDbConnector db) {
        super(NotificationRule.class, db);
    }
}
