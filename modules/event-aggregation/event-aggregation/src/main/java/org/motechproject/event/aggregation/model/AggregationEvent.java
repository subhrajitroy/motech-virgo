package org.motechproject.event.aggregation.model;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.aggregation.model.event.AggregatedEventResult;
import org.motechproject.event.aggregation.model.event.EventStrings;
import org.motechproject.event.aggregation.model.rule.AggregationRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AggregationEvent {

    private MotechEvent event;

    public AggregationEvent(AggregationRule aggregationRule, Aggregation aggregation) {
        Map<String, Object> params = new HashMap<>();
        params.put(EventStrings.ORIGINAL_SUBJECT, aggregationRule.getSubscribedTo());
        params.put(EventStrings.AGGREGATED_EVENTS, aggregation.getEvents());
        event = new MotechEvent(aggregationRule.getPublishAs(), params);
    }

    public MotechEvent toMotechEvent() {
        return event;
    }

    public String getOriginalSubject() {
        return (String) event.getParameters().get(EventStrings.ORIGINAL_SUBJECT);
    }

    public List<AggregatedEventResult> getAggregatedEvents() {
        return (List<AggregatedEventResult>) event.getParameters().get(EventStrings.AGGREGATED_EVENTS);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AggregationEvent)) {
            return false;
        }

        AggregationEvent that = (AggregationEvent) o;

        if (!event.equals(that.event)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return event.hashCode();
    }
}
