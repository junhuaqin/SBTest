package com.logicmonitor.domain.view;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public class SimpleViewCenter implements ViewCenter {
    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID, E extends View<IT>>
    E getView(ViewContext context, Class<T> aggregateClasz, Class<E> viewClasz, IT id) {
        try {
            T aggregate = context.getImmutable(aggregateClasz, id);
            return null == aggregate ? null : _createView(context, viewClasz, aggregateClasz, aggregate);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID, E extends View<IT>>
    Stream<E> getAllView(ViewContext context, Class<T> aggregateClasz, Class<E> viewClasz) {
        try {
            Collection<T> aggregates = context.getAllImmutable(aggregateClasz);
            List<E> views = new LinkedList<>();
            for (T aggregate : aggregates) {
                views.add(_createView(context, viewClasz, aggregateClasz, aggregate));
            }

            return views.stream();
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID, E extends View<IT>>
    E _createView(ViewContext context, Class<E> viewClasz, Class<T> aggregateClasz, T aggregate) {
        try {
            return viewClasz.getConstructor(ViewContext.class, aggregateClasz)
                            .newInstance(context, aggregate);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
