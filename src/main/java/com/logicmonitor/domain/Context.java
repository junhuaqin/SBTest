package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

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

    void commit() throws Exception;
}
