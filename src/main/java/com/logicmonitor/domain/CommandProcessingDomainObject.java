package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

import java.util.List;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public interface CommandProcessingDomainObject<T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
        extends DomainObject<T, IT> {
    List<Event> processCommand(CT cmd);
    T applyEvent(Event event);
}
