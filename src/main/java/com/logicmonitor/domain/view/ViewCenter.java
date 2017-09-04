package com.logicmonitor.domain.view;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;

import java.util.stream.Stream;

/**
 * Created by Robert Qin on 03/09/2017.
 */
public interface ViewCenter {
    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID, E extends View<IT>>
    E getView(ViewContext context, Class<T> aggregateClasz, Class<E> viewClasz, IT id);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID, E extends View<IT>>
    Stream<E> getAllView(ViewContext context, Class<T> aggregateClasz, Class<E> viewClasz);
}
