package com.logicmonitor.domain.view;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.repository.RepositoryContext;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public abstract class AbstractView<T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
        implements View<IT> {
    private RepositoryContext _context;
    private T _aggregate;

    public AbstractView(RepositoryContext context, T aggregate) {
        this._context = context;
        this._aggregate = aggregate;
    }

    protected RepositoryContext getContext() {
        return _context;
    }

    protected T getAggregate() {
        return _aggregate;
    }
}
