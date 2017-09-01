package com.logicmonitor.sbtest.domain.property.event;

import com.logicmonitor.sbtest.domain.property.PropertyID;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class PropertyCreatedEvent implements PropertyEvent {
    private final PropertyID _id;
    private final String _key;
    private final String _value;

    public PropertyCreatedEvent(final PropertyID id, final String key, final String value) {
        this._id = id;
        this._key = key;
        this._value = value;
    }

    public PropertyID getId() {
        return _id;
    }

    public String getKey() {
        return _key;
    }

    public String getValue() {
        return _value;
    }
}
