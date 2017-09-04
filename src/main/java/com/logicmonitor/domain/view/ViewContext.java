package com.logicmonitor.domain.view;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public interface ViewContext {
    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T getImmutable(Class<T> clasz, IT id);
}
