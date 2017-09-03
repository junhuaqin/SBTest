package com.logicmonitor.domain.view;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.repository.RepositoryContext;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public class SimpleViewCenter implements ViewCenter {
    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID, E extends View<IT>>
    E getView(RepositoryContext context, Class<T> aggregateClasz, Class<E> viewClasz, IT id) {
        try {
            return viewClasz.getConstructor(RepositoryContext.class, aggregateClasz)
                    .newInstance(context, context.get(aggregateClasz, id));
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
