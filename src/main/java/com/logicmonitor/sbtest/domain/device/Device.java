package com.logicmonitor.sbtest.domain.device;

import com.logicmonitor.domain.AggregateStatus;
import com.logicmonitor.domain.BaseAggregate;
import com.logicmonitor.domain.Event;
import com.logicmonitor.sbtest.domain.device.command.CreateDevice;
import com.logicmonitor.sbtest.domain.device.command.DeviceCommand;
import com.logicmonitor.sbtest.domain.device.event.DeviceCreatedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class Device extends BaseAggregate<Device, DeviceCommand, DeviceID> {
    private final DeviceID _id;
    private String _name;

    public Device(DeviceID id) {
        _id = id;
    }

    public Device(DeviceID id, String name) {
        this._id = id;
        this._name = name;
    }

    @Override
    public DeviceID getID() {
        return _id;
    }

    @Override
    public Device copy() {
        return new Device(_id, _name);
    }

    protected List<Event> process(CreateDevice create) {
        return Collections.singletonList(new DeviceCreatedEvent(_id, create.getName()));
    }

    protected void apply(DeviceCreatedEvent event) {
        this._name = event.getName();
        setStatus(AggregateStatus.CHANGED);
    }

    public String getName() {
        return _name;
    }
}
