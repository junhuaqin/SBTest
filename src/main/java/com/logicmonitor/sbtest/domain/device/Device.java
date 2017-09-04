package com.logicmonitor.sbtest.domain.device;

import com.google.common.collect.ImmutableSet;
import com.logicmonitor.domain.AggregateStatus;
import com.logicmonitor.domain.BaseAggregate;
import com.logicmonitor.domain.Event;
import com.logicmonitor.sbtest.domain.device.command.AddPropertyCommand;
import com.logicmonitor.sbtest.domain.device.command.CreateDevice;
import com.logicmonitor.sbtest.domain.device.command.DeviceCommand;
import com.logicmonitor.sbtest.domain.device.event.DeviceCreatedEvent;
import com.logicmonitor.sbtest.domain.device.event.PropertyAddedEvent;
import com.logicmonitor.sbtest.domain.property.PropertyID;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class Device extends BaseAggregate<Device, DeviceCommand, DeviceID> {
    private final DeviceID _id;
    private String _name;
    private Set<PropertyID> _properties;

    public Device(DeviceID id) {
        _id = id;
    }

    public Device(DeviceID id, String name, Set<PropertyID> properties) {
        this._id = id;
        this._name = name;
        this._properties = new HashSet<>(properties);
    }

    @Override
    public DeviceID getID() {
        return _id;
    }

    @Override
    public Device copy() {
        return new Device(_id, _name, this._properties);
    }

    protected List<Event> process(CreateDevice create) {
        return Collections.singletonList(new DeviceCreatedEvent(create.getName()));
    }

    protected List<Event> process(AddPropertyCommand addPropertyCmd) {
        return Collections.singletonList(new PropertyAddedEvent(addPropertyCmd.getId(), addPropertyCmd.getKey()));
    }

    protected void apply(DeviceCreatedEvent event) {
        this._name = event.getName();
        this._properties = new ConcurrentSkipListSet<>();
        setStatus(AggregateStatus.CHANGED);
    }

    protected void apply(PropertyAddedEvent event) {
        this._properties.add(event.getId());
    }

    public String getName() {
        return _name;
    }

    public Set<PropertyID> getProperties() {
        return _properties.isEmpty() ? Collections.EMPTY_SET : ImmutableSet.copyOf(_properties);
    }
}
