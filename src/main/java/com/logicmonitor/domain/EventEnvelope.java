package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 03/09/2017.
 */
public interface EventEnvelope<T extends Event, IT extends ID> {
    T getEvent();
    IT getEntityID();
}
