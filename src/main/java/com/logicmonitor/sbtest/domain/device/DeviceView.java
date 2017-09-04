package com.logicmonitor.sbtest.domain.device;

import com.logicmonitor.domain.view.AbstractView;
import com.logicmonitor.domain.view.ViewContext;
import com.logicmonitor.sbtest.domain.device.command.DeviceCommand;

/**
 * Created by Robert Qin on 04/09/2017.
 */
public class DeviceView extends AbstractView<Device, DeviceCommand, DeviceID> {

    public DeviceView(ViewContext context, Device device) {
        super(context, device);
    }

    @Override
    public DeviceID getID() {
        return getAggregate().getID();
    }

    public String getName() {
        return getAggregate().getName();
    }
}
