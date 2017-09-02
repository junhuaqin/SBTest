package com.logicmonitor.domain.store;

import com.logicmonitor.domain.Aggregate;
import com.logicmonitor.domain.TransactionSupport;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public interface StoreContext extends TransactionSupport {
    <T extends Aggregate<T, IT>, IT extends ID>
    RepositoryStore<T, IT> create(Class<T> clasz);
}
