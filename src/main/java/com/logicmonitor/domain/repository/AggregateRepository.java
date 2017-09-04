package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.*;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public interface AggregateRepository<T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
        extends ReentrantReadWriteLockable, TransactionSupport {
    Class<T> getAggregateClass();
    Node<T, CT, IT> save(CT command);
    Node<T, CT, IT> saveImmutable(ID id, CommandProcessingAggregate<?,?,?> aggregate);
    Node<T, CT, IT> find(IT id);
    void remove(IT id);
    void add(Node<T, CT, IT> node);

    IT generateID();
}
