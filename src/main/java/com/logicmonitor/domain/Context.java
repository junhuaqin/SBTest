package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.view.View;

import java.util.List;

/**
 * Created by Robert Qin on 29/08/2017.
 */
public interface Context extends TransactionSupport{
    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    IT save(Class<T> clasz, CT createCommand);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T get(Class<T> clasz, IT id);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    List<EventEnvelope<?, IT>> process(Class<T> clasz, IT id, CT command);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID, E extends View<IT>>
    E getView(Class<T> aggregateClasz, Class<E> viewClasz, IT id);
}
