package org.motechproject.admin.core.repository;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.View;
import org.motechproject.admin.core.domain.StatusMessage;
import org.motechproject.commons.couchdb.dao.MotechBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * CouchDb repository for {@link StatusMessage}s.
 */
@Repository
public class AllStatusMessages extends MotechBaseRepository<StatusMessage> {

    @Autowired
    public AllStatusMessages(@Qualifier("adminDbConnector") CouchDbConnector connector) {
        super(StatusMessage.class, connector);
    }

    /**
     * Retrieves all active messages, meaning all messages with a timeout not in the past.
     * @return all active messages.
     */
    @View(name = "active", map = "function(doc) { if (doc.type === 'StatusMessage') { " +
            "var now = new Date();" +
            "if (doc.timeout && now < Date.parse(doc.timeout)) {" +
                "emit(doc._id);" +
            "}" +
            "}}")
    public List<StatusMessage> getActiveMessages() {
        return queryView("active");
    }
}
