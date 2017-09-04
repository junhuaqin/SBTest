package com.logicmonitor.domain.handler;

import com.logicmonitor.domain.Context;
import com.logicmonitor.domain.Event;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public class EventHandlerContextImpl<T extends Event, IT extends ID>
        implements EventHandlerContext<T, IT> {
    private final Context _context;
    private final T _event;
    private final IT _id;

    public EventHandlerContextImpl(Context context, T event, IT id) {
        this._context = context;
        this._event = event;
        this._id = id;
    }

    @Override
    public Context getContext() {
        return _context;
    }

    @Override
    public T getEvent() {
        return _event;
    }

    @Override
    public IT getEntityID() {
        return _id;
    }
}
