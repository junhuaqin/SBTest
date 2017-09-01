package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.*;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public interface DomainRepository<T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
        extends ReentrantReadWriteLockable, TransactionSupport {
    Node<T, CT, IT> save(CT command);
    Node<T, CT, IT> find(IT id);
    void remove(IT id);

    IT generateID();
}
