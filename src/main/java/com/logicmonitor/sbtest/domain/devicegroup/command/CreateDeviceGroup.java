package com.logicmonitor.sbtest.domain.devicegroup.command;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class CreateDeviceGroup implements DeviceGroupCommand{
    private final String _name;

    public CreateDeviceGroup(final String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}
