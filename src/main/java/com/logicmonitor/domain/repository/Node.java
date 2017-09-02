package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public interface Node<T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID> {
    IT getID();
    Class<T> getDelegateClass();

    T getCommitted();
    T getWriting();

    void setCommitted(T object);
    void setWriting(T object);
    void commit();
    void abort();

    void lockCommittedRead();
    void unlockCommittedRead();

    void lockCommittedWrite();
    void unlockCommittedWrite();

    void lockWriting();
    void unlockWriting();
}
