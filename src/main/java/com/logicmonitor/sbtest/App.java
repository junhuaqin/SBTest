package com.logicmonitor.sbtest;

import com.logicmonitor.domain.Context;
import com.logicmonitor.domain.EventEnvelope;
import com.logicmonitor.domain.center.DomainCenter;
import com.logicmonitor.domain.center.OneWriterMultiReaderDomainCenter;
import com.logicmonitor.domain.id.IntegerIDGenerator;
import com.logicmonitor.sbtest.domain.JDBCConnProvider;
import com.logicmonitor.sbtest.domain.device.Device;
import com.logicmonitor.sbtest.domain.device.DeviceID;
import com.logicmonitor.sbtest.domain.device.DeviceSqlMapper;
import com.logicmonitor.sbtest.domain.device.DeviceView;
import com.logicmonitor.sbtest.domain.device.command.CreateDevice;
import com.logicmonitor.sbtest.domain.devicegroup.DeviceGroup;
import com.logicmonitor.sbtest.domain.devicegroup.DeviceGroupID;
import com.logicmonitor.sbtest.domain.devicegroup.DeviceGroupSqlMapper;
import com.logicmonitor.sbtest.domain.devicegroup.command.CreateDeviceGroup;
import com.logicmonitor.sbtest.domain.property.Property;
import com.logicmonitor.sbtest.domain.property.PropertyID;
import com.logicmonitor.sbtest.domain.property.PropertySqlMapper;
import com.logicmonitor.sbtest.domain.property.command.CreateProperty;
import com.logicmonitor.sbtest.domain.property.command.SetPropertyValue;
import com.logicmonitor.sbtest.domain.property.event.PropertyValueChangedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class App {
    public static void main(String[] args) throws Exception {
        JDBCConnProvider provider = new JDBCConnProvider();
        provider.initConnectionPool(Collections.emptyMap());
        String dbName = "sbtest";
        DomainCenter dc = OneWriterMultiReaderDomainCenter.builder()
                .withAggregate(Device.class, new IntegerIDGenerator<>(n -> new DeviceID(n.value())), new DeviceSqlMapper(dbName))
                .withAggregate(DeviceGroup.class, new IntegerIDGenerator<>(n -> new DeviceGroupID(n.value())), new DeviceGroupSqlMapper(dbName))
                .withAggregate(Property.class, new IntegerIDGenerator<>(n -> new PropertyID(n.value())), new PropertySqlMapper(dbName))
                .withConnProvider(provider::getConn)
                .build();

        Context context = dc.getContext();
        DeviceID deviceID = context.save(Device.class, new CreateDevice("localhost"));
        System.out.println(deviceID.value());
        DeviceGroupID deviceGroupID = context.save(DeviceGroup.class, new CreateDeviceGroup("group1"));
        System.out.println(deviceGroupID.value());
        PropertyID propertyID = context.save(Property.class, new CreateProperty("display", "test"));
        System.out.println(propertyID.value());
        System.out.println(context.get(Property.class, propertyID).getValue());
        List<EventEnvelope<?, PropertyID>> envelopes = context.process(Property.class, propertyID, new SetPropertyValue("test2"));
        new App().propertyChanged((EventEnvelope<PropertyValueChangedEvent, PropertyID>) envelopes.get(0));
        System.out.println(context.get(Property.class, propertyID).getValue());
        try {
            context.commit();
        }
        catch (Exception e) {
            context.abort();
            e.printStackTrace();
        }

        System.out.println(context.getView(Device.class, DeviceView.class, new DeviceID(1)).getName());

        Device device = context.get(Device.class, deviceID);
        System.out.println(device.getName());
        System.out.println(context.get(DeviceGroup.class, deviceGroupID).getName());
        System.out.println(context.get(Property.class, propertyID).getKey());
    }

    public void propertyChanged(EventEnvelope<PropertyValueChangedEvent, PropertyID> envelope) {
        System.out.println(envelope.getEntityID().value() + ":" + envelope.getEvent().getNewValue());
    }
}
