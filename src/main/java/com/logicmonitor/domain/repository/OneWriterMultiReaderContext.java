package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.Event;
import com.logicmonitor.domain.center.OneWriterMultiReaderDomainCenter;
import com.logicmonitor.domain.center.RepositoryManager;
import com.logicmonitor.domain.id.ID;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public class OneWriterMultiReaderContext extends AbstractContext {
    private Set<Node<?, ?, ?>> _writingNodes = new LinkedHashSet<>();
    private final Map<Class<? extends CommandProcessingAggregate>, List<ClaszNodeHolder<?, ?, ?>>> _newNodes = new ConcurrentHashMap<>();
    private final Map<Class<? extends CommandProcessingAggregate>, List<ClaszNodeHolder<?, ?, ?>>> _removedNodes = new ConcurrentHashMap<>();
    private final OneWriterMultiReaderDomainCenter _center;
    private final RepositoryManager _repositories;

    public OneWriterMultiReaderContext(OneWriterMultiReaderDomainCenter store, RepositoryManager repositories) {
        _center = store;
        _repositories = repositories;
    }

    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    IT save(Class<T> clasz, CT createCommand) {
        Node<T, CT, IT> node = _repositories.getRepository(clasz).save(createCommand);
        _writingNodes.add(node);
        _newNodes.computeIfAbsent(clasz, k -> new LinkedList<>()).add(new ClaszNodeHolder<>(clasz, node));
        return node.getID();
    }

    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T get(Class<T> clasz, IT id) {
        try {
            _center.acquireReadLock();
            Node<T, CT, IT> node = findNode(_repositories, clasz, id);
            return node.getWriting() == null ? node.getCommitted() : node.getWriting();
        }
        finally {
            _center.releaseReadLock();
        }
    }

    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    List<Event> process(Class<T> clasz, IT id, CT command) {
        return get(clasz, id).processCommand(command);
    }

    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID> T applyAndGet(Class<T> clasz, IT id, Event event) {
        T aggreate = _makeWriting(findNode(_repositories, clasz, id)).getWriting();
        aggreate.applyEvent(event);
        return aggreate;
    }

    @Override
    public void commit() throws ContextException {
        try {
            _center.acquireWriteLock();
            executeOnAllNodes(_writingNodes, Node::commit);
            _newNodes.forEach((k, v) ->
                v.forEach(ClaszNodeHolder::add2Repository)
            );

            _removedNodes.forEach((k, v) ->
                v.forEach(ClaszNodeHolder::removeFromRepository)
            );

            _center.commit();
            _clear();
        }
        catch (Exception e) {
            throw new ContextException(e);
        }
        finally {
            _center.releaseWriteLock();
        }
    }

    @Override
    public void abort() {
        executeOnAllNodes(_writingNodes, Node::abort);
        _center.abort();
        _clear();
    }

    protected  <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    Node<T, CT, IT> findNode(RepositoryManager manager, Class<T> clasz, IT id) {
        List<ClaszNodeHolder<?, ?, ?>> nodes = _newNodes.get(clasz);
        if (null != nodes) {
            Optional newNode = nodes.stream()
                    .filter(n -> n._node.getID().equals(id))
                    .map(n -> n._node)
                    .findFirst();
            if (newNode.isPresent()) {
                return (Node<T, CT, IT>) newNode.get();
            }
        }
        
        return super.findNode(manager, clasz, id);
    }

    private void _clear() {
        _writingNodes.clear();
        _newNodes.clear();
        _removedNodes.clear();
    }

    private <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    Node<T, CT, IT> _makeWriting(Node<T, CT, IT> node) {
        if (null == node.getWriting()) {
            node.setWriting(node.getCommitted().copy());
            _writingNodes.add(node);
        }

        return node;
    }

    private class ClaszNodeHolder<T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID> {
        private Class<T> _clasz;
        private Node<T, CT, IT> _node;

        ClaszNodeHolder(Class<T> clasz, Node<T, CT, IT> node) {
            _clasz = clasz;
            _node = node;
        }

        void add2Repository() {
            _repositories.getRepository(_clasz).add(_node);
        }

        void removeFromRepository() {
            _repositories.getRepository(_clasz).remove(_node.getID());
        }
    }
}
