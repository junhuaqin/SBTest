package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public abstract class AbstractNode<T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
        implements Node<T, CT, IT> {
    private final Class<T> _clasz;
    private final IT _id;
    private T _committed;
    private T _writing;

    public AbstractNode(Class<T> clasz, IT id) {
        _clasz = clasz;
        _id = id;
    }

    @Override
    public IT getID() {
        return _id;
    }

    @Override
    public Class<T> getDelegateClass() {
        return _clasz;
    }

    @Override
    public T getCommitted() {
        return _committed;
    }

    @Override
    public T getWriting() {
        return _writing;
    }

    @Override
    public void setCommitted(T object) {
        _committed = object;
    }

    @Override
    public void setWriting(T object) {
        _writing = object;
    }

    @Override
    public void commit() {
        _committed = _writing;
        _writing = null;
    }

    @Override
    public void abort() {
        _writing = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractNode<?, ?, ?> that = (AbstractNode<?, ?, ?>) o;

        return _id != null ? _id.equals(that._id) : that._id == null;
    }

    @Override
    public int hashCode() {
        return _id != null ? _id.hashCode() : 0;
    }
}
