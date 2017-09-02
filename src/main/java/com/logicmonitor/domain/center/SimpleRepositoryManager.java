package com.logicmonitor.domain.center;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.id.IDGenerator;
import com.logicmonitor.domain.repository.CachedAggregateRepository;
import com.logicmonitor.domain.repository.AggregateRepository;
import com.logicmonitor.domain.repository.OneWriteMultiReaderNodeFactory;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public class SimpleRepositoryManager implements RepositoryManager{
    private final Map<Class<? extends CommandProcessingAggregate>, AggregateRepository<?,?,?>> _repositories = new ConcurrentHashMap<>();

    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    void register(Class<T> clasz, IDGenerator<IT> idGenerator) {
        _repositories.put(clasz, new CachedAggregateRepository<>(clasz, idGenerator, new OneWriteMultiReaderNodeFactory()));
    }

    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    AggregateRepository<T, CT, IT> getRepository(Class<T> clasz) {
        return (AggregateRepository<T, CT, IT>)_repositories.get(clasz);
    }

    @Override
    public Collection<AggregateRepository<?, ?, ?>> getAll() {
        return _repositories.values();
    }
}
