package com.logicmonitor.sbtest.domain.devicegroup.event;

import com.logicmonitor.sbtest.domain.devicegroup.DeviceGroupID;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class DeviceGroupCreatedEvent implements DeviceGroupEvent {
    private final DeviceGroupID _id;
    private final String _name;

    public DeviceGroupCreatedEvent(final DeviceGroupID id, final String name) {
        _id = id;
        _name = name;
    }

    public DeviceGroupID getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }
}
