package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.Aggregates;
import com.logicmonitor.domain.Event;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.id.IDGenerator;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class CachedAggregateRepository<T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
       implements AggregateRepository<T, CT, IT> {
    private Class<T> _clasz;
    private final IDGenerator<IT> _idGenerator;
    private final NodeFactory _nodeFactory;
    private final Map<IT, Node<T, CT, IT>> _repository = new ConcurrentHashMap<>();
    private final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock(true);

    public CachedAggregateRepository(Class<T> clasz, IDGenerator<IT> idGenerator, NodeFactory factory) {
        _clasz = clasz;
        _idGenerator = idGenerator;
        _nodeFactory = factory;
    }

    @Override
    public Node<T, CT, IT> save(CT command) {
        IT id = generateID();
        T domainObject;
        try {
            domainObject = _clasz.getConstructor(id.getClass()).newInstance(id);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        List<Event> events = domainObject.processCommand(command);
        Aggregates.applyEventsToAggregate(domainObject, events);
        Node<T, CT, IT> node = _nodeFactory.createNode(_clasz, id);
        node.setWriting(domainObject);

        return node;
    }

    @Override
    public Node<T, CT, IT> find(IT id) {
        return _repository.get(id);
    }

    @Override
    public void remove(IT id) {
        _repository.remove(id);
    }

    @Override
    public void add(Node<T, CT, IT> node) {
        _repository.put(node.getID(), node);
    }

    @Override
    public IT generateID() {
        return _idGenerator.create();
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
    }

    @Override
    public void abort() {
    }
}
