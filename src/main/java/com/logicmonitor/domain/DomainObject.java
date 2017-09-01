package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public interface DomainObject<T, IT extends ID> {
    IT getID();
    T copy();
}
