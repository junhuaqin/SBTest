package com.logicmonitor.sbtest;

import com.logicmonitor.domain.center.DomainCenter;
import com.logicmonitor.domain.center.OneWriterMultiReaderDomainCenter;
import com.logicmonitor.domain.context.Context;
import com.logicmonitor.domain.id.IntegerIDGenerator;
import com.logicmonitor.domain.repository.CachedDomainRepository;
import com.logicmonitor.domain.repository.OneWriteMultiReaderNodeFactory;
import com.logicmonitor.sbtest.domain.device.Device;
import com.logicmonitor.sbtest.domain.device.DeviceID;
import com.logicmonitor.sbtest.domain.device.command.CreateDevice;
import com.logicmonitor.sbtest.domain.devicegroup.DeviceGroup;
import com.logicmonitor.sbtest.domain.devicegroup.DeviceGroupID;
import com.logicmonitor.sbtest.domain.devicegroup.command.CreateDeviceGroup;
import com.logicmonitor.sbtest.domain.property.Property;
import com.logicmonitor.sbtest.domain.property.PropertyID;
import com.logicmonitor.sbtest.domain.property.command.CreateProperty;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class App {
    public static void main(String[] args) {
        DomainCenter dc = new OneWriterMultiReaderDomainCenter();
        dc.register(Device.class, new CachedDomainRepository<>(Device.class, new IntegerIDGenerator<>(n -> new DeviceID(n.value())), new OneWriteMultiReaderNodeFactory()));
        dc.register(DeviceGroup.class, new CachedDomainRepository<>(DeviceGroup.class, new IntegerIDGenerator<>(n -> new DeviceGroupID(n.value())), new OneWriteMultiReaderNodeFactory()));
        dc.register(Property.class, new CachedDomainRepository<>(Property.class, new IntegerIDGenerator<>(n -> new PropertyID(n.value())), new OneWriteMultiReaderNodeFactory()));
        Context context = dc.getContext();
        DeviceID deviceID = context.save(Device.class, new CreateDevice("localhost"));
        System.out.println(deviceID.value());
        DeviceGroupID deviceGroupID = context.save(DeviceGroup.class, new CreateDeviceGroup("group1"));
        System.out.println(deviceGroupID.value());
        PropertyID propertyID = context.save(Property.class, new CreateProperty("display", "test"));
        System.out.println(propertyID.value());
        context.commit();
        Device device = context.get(Device.class, deviceID);
        System.out.println(device.getName());
        System.out.println(context.get(DeviceGroup.class, deviceGroupID).getName());
        System.out.println(context.get(Property.class, propertyID).getKey());
    }
}
