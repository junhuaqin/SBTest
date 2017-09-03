package com.logicmonitor.sbtest.domain.devicegroup;

import com.logicmonitor.domain.AggregateStatus;
import com.logicmonitor.domain.BaseAggregate;
import com.logicmonitor.domain.Event;
import com.logicmonitor.sbtest.domain.devicegroup.command.CreateDeviceGroup;
import com.logicmonitor.sbtest.domain.devicegroup.command.DeviceGroupCommand;
import com.logicmonitor.sbtest.domain.devicegroup.event.DeviceGroupCreatedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class DeviceGroup extends BaseAggregate<DeviceGroup, DeviceGroupCommand, DeviceGroupID> {
    private final DeviceGroupID _id;
    private String _name;

    public DeviceGroup(DeviceGroupID id) {
        this._id = id;
    }

    public DeviceGroup(DeviceGroupID id, String name) {
        this._id = id;
        this._name = name;
    }

    @Override
    public DeviceGroupID getID() {
        return _id;
    }

    @Override
    public DeviceGroup copy() {
        return new DeviceGroup(_id, _name);
    }

    protected List<Event> process(CreateDeviceGroup create) {
        return Collections.singletonList(new DeviceGroupCreatedEvent(create.getName()));
    }

    protected void apply(DeviceGroupCreatedEvent event) {
        this._name = event.getName();
        setStatus(AggregateStatus.CHANGED);
    }

    public String getName() {
        return _name;
    }
}
