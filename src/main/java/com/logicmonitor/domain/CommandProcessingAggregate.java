package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

import java.util.List;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public interface CommandProcessingAggregate<T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
        extends Aggregate<T, IT> {
    List<Event> processCommand(CT cmd);
    T applyEvent(Event event);
}
