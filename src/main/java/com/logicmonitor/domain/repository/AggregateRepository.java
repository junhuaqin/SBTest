package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.ReentrantReadWriteLockable;
import com.logicmonitor.domain.TransactionSupport;
import com.logicmonitor.domain.id.ID;

import java.util.Collection;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public interface AggregateRepository<T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
        extends ReentrantReadWriteLockable, TransactionSupport {
    Class<T> getAggregateClass();
    Node<T, CT, IT> save(CT command);
    Node<T, CT, IT> saveImmutable(ID id, CommandProcessingAggregate<?,?,?> aggregate);
    Node<T, CT, IT> find(IT id);
    Collection<Node<T, CT, IT>> findAll();
    void remove(IT id);
    void add(Node<T, CT, IT> node);

    IT generateID();
}
