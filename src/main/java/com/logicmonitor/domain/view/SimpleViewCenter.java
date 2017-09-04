package com.logicmonitor.domain.view;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public class SimpleViewCenter implements ViewCenter {
    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID, E extends View<IT>>
    E getView(ViewContext context, Class<T> aggregateClasz, Class<E> viewClasz, IT id) {
        try {
            T aggregate = context.getImmutable(aggregateClasz, id);
            return null == aggregate ? null :
                    viewClasz.getConstructor(ViewContext.class, aggregateClasz)
                    .newInstance(context, aggregate);
        }
        catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
