package com.logicmonitor.sbtest.domain.device.command;

import com.logicmonitor.sbtest.domain.property.PropertyID;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public class AddPropertyCommand implements DeviceCommand {
    private final PropertyID _id;
    private final String _key;
    private final String _value;

    public AddPropertyCommand(PropertyID id, String key, String value) {
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
