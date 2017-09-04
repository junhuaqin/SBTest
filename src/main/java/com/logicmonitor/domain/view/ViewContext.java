package com.logicmonitor.domain.view;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;

import java.util.Collection;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public interface ViewContext {
    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T getImmutable(Class<T> clasz, IT id);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    Collection<T> getAllImmutable(Class<T> clasz);
}
