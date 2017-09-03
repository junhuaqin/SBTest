package com.logicmonitor.sbtest.domain.property.event;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class PropertyCreatedEvent implements PropertyEvent {
    private final String _key;
    private final String _value;

    public PropertyCreatedEvent(final String key, final String value) {
        this._key = key;
        this._value = value;
    }

    public String getKey() {
        return _key;
    }

    public String getValue() {
        return _value;
    }
}
