package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class NonLockNode<T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
        extends AbstractNode<T, CT, IT> {
    public NonLockNode(Class<T> clasz, IT id) {
        super(clasz, id);
    }

    @Override
    public void lockCommittedRead() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlockCommittedRead() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lockCommittedWrite() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlockCommittedWrite() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void lockWriting() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void unlockWriting() {
        throw new UnsupportedOperationException();
    }
}
