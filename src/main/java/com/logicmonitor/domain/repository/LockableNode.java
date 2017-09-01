package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.id.ID;

import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Robert Qin on 29/08/2017.
 */
public class LockableNode<T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
        extends AbstractNode<T, CT, IT> {
    private ReentrantLock _writeLock = new ReentrantLock();
    private ReentrantReadWriteLock _committedLock = new ReentrantReadWriteLock(true);

    public LockableNode(Class<T> clasz, IT id) {
        super(clasz, id);
    }

    @Override
    public void lockCommittedRead() {
        _committedLock.readLock().lock();
    }

    @Override
    public void unlockCommittedRead() {
        _committedLock.readLock().unlock();
    }

    @Override
    public void lockCommittedWrite() {
        _committedLock.writeLock().lock();
    }

    @Override
    public void unlockCommittedWrite() {
        _committedLock.writeLock().unlock();
    }

    @Override
    public void lockWriting() {
        _writeLock.lock();
    }

    @Override
    public void unlockWriting() {
        _writeLock.unlock();
    }
}
