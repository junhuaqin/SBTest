package com.logicmonitor.domain.center;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.id.IDGenerator;
import com.logicmonitor.domain.repository.CachedDomainRepository;
import com.logicmonitor.domain.repository.DomainRepository;
import com.logicmonitor.domain.repository.OneWriteMultiReaderNodeFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public class SimpleRepositoryManager implements RepositoryManager{
    private final Map<Class<? extends CommandProcessingDomainObject>, DomainRepository<?,?,?>> _repositories = new ConcurrentHashMap<>();

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    void register(Class<T> clasz, IDGenerator<IT> idGenerator) {
        _repositories.put(clasz, new CachedDomainRepository<>(clasz, idGenerator, new OneWriteMultiReaderNodeFactory()));
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    DomainRepository<T, CT, IT> getRepository(Class<T> clasz) {
        return (DomainRepository<T, CT, IT>)_repositories.get(clasz);
    }

    @Override
    public Collection<DomainRepository<?, ?, ?>> getAll() {
        return _repositories.values();
    }
}
