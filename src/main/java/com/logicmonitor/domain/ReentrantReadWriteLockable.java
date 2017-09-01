package com.logicmonitor.domain;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public interface ReentrantReadWriteLockable {
    void acquireReadLock();
    void releaseReadLock();

    void acquireWriteLock();
    void releaseWriteLock();
}
