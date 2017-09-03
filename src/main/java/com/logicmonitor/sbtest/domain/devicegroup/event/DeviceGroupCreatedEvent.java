package com.logicmonitor.sbtest.domain.devicegroup.event;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class DeviceGroupCreatedEvent implements DeviceGroupEvent {
    private final String _name;

    public DeviceGroupCreatedEvent(final String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}
