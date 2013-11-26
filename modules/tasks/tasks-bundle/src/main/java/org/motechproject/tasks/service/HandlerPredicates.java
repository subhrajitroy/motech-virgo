package org.motechproject.tasks.service;

import org.apache.commons.collections.Predicate;
import org.motechproject.event.listener.annotations.MotechListenerEventProxy;
import org.motechproject.tasks.domain.Task;

/**
 * Utility class defining filters over some collections.
 */
final class HandlerPredicates {
    private HandlerPredicates() {
    }

    public static Predicate activeTasks() {
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return object instanceof Task && ((Task) object).isEnabled() && ((Task) object).hasRegisteredChannel();
            }
        };
    }

    public static Predicate withServiceName(final String serviceName) {
        return new Predicate() {
            @Override
            public boolean evaluate(Object object) {
                return object instanceof MotechListenerEventProxy && ((MotechListenerEventProxy) object).getIdentifier().equalsIgnoreCase(serviceName);
            }
        };
    }
}
