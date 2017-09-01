package com.logicmonitor.domain.center;

import com.logicmonitor.domain.*;
import com.logicmonitor.domain.context.Context;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.repository.DomainRepository;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public interface DomainCenter extends ReentrantReadWriteLockable, TransactionSupport {
    Context getContext();
    <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    void register(Class<T> clasz, DomainRepository<T, CT, IT> repository);
    <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    DomainRepository<T, CT, IT> getRepository(Class<T> clasz);
}
