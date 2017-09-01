package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

import java.util.List;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class DomainObjects {
    public static <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    T applyEventsToMutableDomainObject(T object, List<Event> events) {
        events.forEach(object::applyEvent);
        return object;
    }
}
