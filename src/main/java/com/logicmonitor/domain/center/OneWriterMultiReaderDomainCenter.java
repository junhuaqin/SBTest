package com.logicmonitor.domain.center;

import com.logicmonitor.domain.Aggregate;
import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.Context;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.id.IDGenerator;
import com.logicmonitor.domain.repository.AggregateRepository;
import com.logicmonitor.domain.repository.OneWriterMultiReaderContext;
import com.logicmonitor.domain.store.JDBCStoreContext;
import com.logicmonitor.domain.store.SqlMapper;
import com.logicmonitor.domain.store.StoreContextFactory;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class OneWriterMultiReaderDomainCenter implements DomainCenter {
    private final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock(true);
    private final StoreContextFactory _storeContextFactory;
    private final RepositoryManager _repositories;

    private OneWriterMultiReaderDomainCenter(final Builder builder) {
        this._storeContextFactory = () -> new JDBCStoreContext(builder._sqlMappers,
                                                               builder._connProvider);
        this._repositories = builder._repositories;
    }

    public static Builder builder() {
        return new Builder();
    }

    private OneWriterMultiReaderDomainCenter init() throws Exception {
        for (AggregateRepository<?, ?, ?> repository : _repositories.getAll()) {
            this._storeContextFactory.create()
                    .get(repository.getAggregateClass())
                    .findAll()
                    .forEach(n -> repository.saveImmutable(n.getId(), n.getAggregate()));
        }

        return this;
    }

    @Override
    public Context getContext() {
        return new Coordinator(new OneWriterMultiReaderContext(this, _repositories), _storeContextFactory.create());
    }

    @Override
    public void acquireReadLock() {
        _lock.readLock().lock();
    }

    @Override
    public void releaseReadLock() {
        _lock.readLock().unlock();
    }

    @Override
    public void acquireWriteLock() {
        _lock.writeLock().lock();
    }

    @Override
    public void releaseWriteLock() {
        _lock.writeLock().lock();
    }

    @Override
    public void commit() throws Exception{
        for (AggregateRepository repository: _repositories.getAll()) {
            repository.commit();
        }
    }

    @Override
    public void abort() {
        _repositories.getAll().forEach(AggregateRepository::abort);
    }

    public static class Builder {
        private Map<Class<? extends Aggregate>, SqlMapper<?, ?>> _sqlMappers = new HashMap<>();
        private Supplier<Connection> _connProvider;
        private RepositoryManager _repositories = new SimpleRepositoryManager();

        public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
        Builder withAggregate(Class<T> clasz, IDGenerator<IT> idGenerator, SqlMapper<T, IT> sqlMapper) {
            _repositories.register(clasz, idGenerator);
            _sqlMappers.put(clasz, sqlMapper);
            return this;
        }

        public Builder withConnProvider(Supplier<Connection> provider) {
            _connProvider = provider;
            return this;
        }

        public OneWriterMultiReaderDomainCenter build() throws Exception {
            return new OneWriterMultiReaderDomainCenter(this).init();
        }
    }
}
