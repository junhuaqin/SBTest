package com.logicmonitor.sbtest.domain.device.event;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class DeviceCreatedEvent implements DeviceEvent {
    private final String _name;

    public DeviceCreatedEvent(final String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}
