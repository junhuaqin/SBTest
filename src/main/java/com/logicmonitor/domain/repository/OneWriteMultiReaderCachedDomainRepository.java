package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.id.IDGenerator;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class OneWriteMultiReaderCachedDomainRepository<T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
        extends CachedDomainRepository<T, CT, IT> {
    public OneWriteMultiReaderCachedDomainRepository(Class<T> clasz, IDGenerator<IT> idGenerator) {
        super(clasz, idGenerator);
    }

    @Override
    protected Node<T, CT, IT> newNode(IT id) {
        return new NonLockNode<>(getDelegatedClass(), id);
    }
}
