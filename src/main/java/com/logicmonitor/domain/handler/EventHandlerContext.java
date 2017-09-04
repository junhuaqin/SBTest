package com.logicmonitor.domain.handler;

import com.logicmonitor.domain.Context;
import com.logicmonitor.domain.Event;
import com.logicmonitor.domain.EventEnvelope;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public interface EventHandlerContext<T extends Event, IT extends ID> extends EventEnvelope<T, IT> {
    Context getContext();
}
