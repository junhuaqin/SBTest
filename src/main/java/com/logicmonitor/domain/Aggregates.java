package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

import java.util.List;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class Aggregates {
    public static <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T applyEventsToAggregate(T aggregate, List<Event> events) {
        events.forEach(aggregate::applyEvent);
        return aggregate;
    }
}
