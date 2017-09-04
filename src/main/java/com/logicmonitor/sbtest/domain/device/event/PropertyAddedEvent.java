package com.logicmonitor.sbtest.domain.device.event;

import com.logicmonitor.sbtest.domain.property.PropertyID;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public class PropertyAddedEvent implements DeviceEvent{
    private final PropertyID _id;
    private final String _key;

    public PropertyAddedEvent(PropertyID id, String key) {
        this._id = id;
        this._key = key;
    }

    public PropertyID getId() {
        return _id;
    }

    public String getKey() {
        return _key;
    }
}
