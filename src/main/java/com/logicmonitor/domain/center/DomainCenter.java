package com.logicmonitor.domain.center;

import com.logicmonitor.domain.ReentrantReadWriteLockable;
import com.logicmonitor.domain.TransactionSupport;
import com.logicmonitor.domain.Context;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public interface DomainCenter extends ReentrantReadWriteLockable, TransactionSupport {
    Context getContext();
}
