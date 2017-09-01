package com.logicmonitor.domain;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public interface ReentrantReadWriteLockable {
    void lockRead();
    void unlockRead();

    void lockWrite();
    void unlockWrite();
}
