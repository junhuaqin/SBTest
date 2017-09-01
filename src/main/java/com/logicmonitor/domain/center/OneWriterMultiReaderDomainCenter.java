package com.logicmonitor.domain.center;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.context.Context;
import com.logicmonitor.domain.context.OneWriterMultiReaderContext;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.repository.DomainRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class OneWriterMultiReaderDomainCenter implements DomainCenter {
    private final Map<Class<? extends CommandProcessingDomainObject>, DomainRepository> _repositories = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock(true);

    @Override
    public Context getContext() {
        return new OneWriterMultiReaderContext(this);
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    void register(Class<T> clasz, DomainRepository<T, CT, IT> repository) {
        _repositories.put(clasz, repository);
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    DomainRepository<T, CT, IT> getRepository(Class<T> clasz) {
        return _repositories.get(clasz);
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
    public void commit() {
        _repositories.forEach((k, v) -> v.commit());
    }

    @Override
    public void abort() {
        _repositories.forEach((k, v) -> v.abort());
    }
}
