package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.*;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 03/09/2017.
 */
public interface RepositoryContext extends Context {
    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T applyAndGet(Class<T> clasz, IT id, Event event);
}
