package com.logicmonitor.sbtest.domain.device.command;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class CreateDevice implements DeviceCommand {
    private final String _name;

    public CreateDevice(final String name) {
        _name = name;
    }

    public String getName() {
        return _name;
    }
}
