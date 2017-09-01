package com.logicmonitor.sbtest.domain.device.event;

import com.logicmonitor.sbtest.domain.device.DeviceID;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class DeviceCreatedEvent implements DeviceEvent {
    private final DeviceID _id;
    private final String _name;

    public DeviceCreatedEvent(final DeviceID id, final String name) {
        _id = id;
        _name = name;
    }

    public DeviceID getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }
}
