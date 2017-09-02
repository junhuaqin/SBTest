package com.logicmonitor.domain.center;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.id.IDGenerator;
import com.logicmonitor.domain.repository.DomainRepository;

import java.util.Collection;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public interface RepositoryManager {
    <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    void register(Class<T> clasz, IDGenerator<IT> idGenerator);
    <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    DomainRepository<T, CT, IT> getRepository(Class<T> clasz);

    Collection<DomainRepository<?, ?, ?>> getAll();
}
