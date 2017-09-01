package com.logicmonitor.domain.context;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.Event;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.center.OneWriterMultiReaderDomainCenter;
import com.logicmonitor.domain.repository.Node;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public class OneWriterMultiReaderContext extends AbstractContext {
    private Set<Node<?, ?, ?>> _writingNodes = new LinkedHashSet<>();
    private final OneWriterMultiReaderDomainCenter _center;

    public OneWriterMultiReaderContext(OneWriterMultiReaderDomainCenter store) {
        _center = store;
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    IT save(Class<T> clasz, CT createCommand) {
        Node<T, CT, IT> node = _center.getRepository(clasz).save(createCommand);
        _writingNodes.add(node);
        return node.getID();
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    T get(Class<T> clasz, IT id) {
        try {
            _center.acquireReadLock();
            Node<T, CT, IT> node = findNode(_center, clasz, id);
            return node.getWriting() == null ? node.getCommitted() : node.getWriting();
        }
        finally {
            _center.releaseReadLock();
        }
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    List<Event> process(Class<T> clasz, IT id, CT command) {
        return _makeWriting(findNode(_center, clasz, id)).getWriting().processCommand(command);
    }

    @Override
    public <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    void apply(Class<T> clasz, IT id, Event event) {
        _makeWriting(findNode(_center, clasz, id)).getWriting().applyEvent(event);
    }

    @Override
    public void commit() throws ContextException {
        try {
            _center.acquireWriteLock();
            executeOnAllNodes(_writingNodes, Node::commit);
            _center.commit();
            _clear();
        }
        finally {
            _center.releaseWriteLock();
        }
    }

    @Override
    public void abort() {
        executeOnAllNodes(_writingNodes, Node::abort);
        _center.abort();
    }

    private void _clear() {
        _writingNodes.clear();
    }

    private <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    Node<T, CT, IT> _makeWriting(Node<T, CT, IT> node) {
        if (null == node.getWriting()) {
            node.setWriting(node.getCommitted().copy());
            _writingNodes.add(node);
        }

        return node;
    }
}
