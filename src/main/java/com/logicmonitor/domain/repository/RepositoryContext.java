package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.*;
import com.logicmonitor.domain.id.ID;

import java.util.List;

/**
 * Created by Robert Qin on 03/09/2017.
 */
public interface RepositoryContext extends TransactionSupport{
    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    IT save(Class<T> clasz, CT createCommand);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T get(Class<T> clasz, IT id);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    List<Event> process(Class<T> clasz, IT id, CT command);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T applyAndGet(Class<T> clasz, IT id, Event event);
}
