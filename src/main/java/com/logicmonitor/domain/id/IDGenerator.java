package com.logicmonitor.domain.id;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public interface IDGenerator<T extends ID> {
    void setSeed(T id);
    T create();
}
