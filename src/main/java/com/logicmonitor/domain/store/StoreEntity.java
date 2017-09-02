package com.logicmonitor.domain.store;

import com.logicmonitor.domain.DomainObject;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class StoreEntity<T extends DomainObject<T, IT>, IT extends ID> {
    private final IT _id;
    private final T _object;

    public StoreEntity(IT id, T object) {
        this._id = id;
        this._object = object;
    }

    public IT getId() {
        return _id;
    }

    public T getObject() {
        return _object;
    }
}
