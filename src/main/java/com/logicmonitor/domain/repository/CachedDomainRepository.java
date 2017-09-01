package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.DomainObjects;
import com.logicmonitor.domain.Event;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.id.IDGenerator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class CachedDomainRepository<T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
       implements DomainRepository<T, CT, IT> {
    private Class<T> _clasz;
    private final IDGenerator<IT> _idGenerator;
    private final NodeFactory _nodeFactory;
    private final Map<IT, Node<T, CT, IT>> _repository = new ConcurrentHashMap<>();
    private final Map<IT, Node<T, CT, IT>> _newNodes = new ConcurrentHashMap<>();
    private final Set<IT> _removedNodes = new ConcurrentSkipListSet<>();
    private final ReentrantReadWriteLock _lock = new ReentrantReadWriteLock(true);

    public CachedDomainRepository(Class<T> clasz, IDGenerator<IT> idGenerator, NodeFactory factory) {
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
        DomainObjects.applyEventsToMutableDomainObject(domainObject, events);
        Node<T, CT, IT> node = _nodeFactory.createNode(_clasz, id);
        node.setWriting(domainObject);
        _newNodes.put(node.getID(), node);

        return node;
    }

    @Override
    public Node<T, CT, IT> find(IT id) {
        return _newNodes.getOrDefault(id, _repository.get(id));
    }

    @Override
    public void remove(IT id) {
        _removedNodes.add(id);
    }

    @Override
    public IT generateID() {
        return _idGenerator.create();
    }

    @Override
    public void lockRead() {
        _lock.readLock().lock();
    }

    @Override
    public void unlockRead() {
        _lock.readLock().unlock();
    }

    @Override
    public void lockWrite() {
        _lock.writeLock().lock();
    }

    @Override
    public void unlockWrite() {
        _lock.writeLock().lock();
    }

    @Override
    public void commit() {
        _repository.putAll(_newNodes);
        _newNodes.clear();

        _removedNodes.forEach(_repository::remove);
        _removedNodes.clear();
    }

    @Override
    public void abort() {
        _newNodes.clear();
        _removedNodes.clear();
    }
}
