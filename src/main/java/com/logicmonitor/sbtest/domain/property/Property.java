package com.logicmonitor.sbtest.domain.property;

import com.logicmonitor.domain.AggregateStatus;
import com.logicmonitor.domain.BaseAggregate;
import com.logicmonitor.domain.Event;
import com.logicmonitor.sbtest.domain.property.command.CreateProperty;
import com.logicmonitor.sbtest.domain.property.command.DeprecatePropertyCommand;
import com.logicmonitor.sbtest.domain.property.command.PropertyCommand;
import com.logicmonitor.sbtest.domain.property.command.SetPropertyValue;
import com.logicmonitor.sbtest.domain.property.event.PropertyCreatedEvent;
import com.logicmonitor.sbtest.domain.property.event.PropertyDeprecatedEvent;
import com.logicmonitor.sbtest.domain.property.event.PropertyValueChangedEvent;

import java.util.Collections;
import java.util.List;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class Property extends BaseAggregate<Property, PropertyCommand, PropertyID> {
    private final PropertyID _id;
    private String _key;
    private String _value;

    public Property(PropertyID id) {
        this._id = id;
    }

    public Property(PropertyID id, String key, String value) {
        this._id = id;
        this._key = key;
        this._value = value;
    }

    @Override
    public PropertyID getID() {
        return _id;
    }

    @Override
    public Property copy() {
        return new Property(_id, _key, _value);
    }

    protected List<Event> process(CreateProperty create) {
        return Collections.singletonList(new PropertyCreatedEvent(create.getKey(), create.getValue()));
    }

    protected List<Event> process(SetPropertyValue setValue) {
        return Collections.singletonList(new PropertyValueChangedEvent(setValue.getValue()));
    }

    protected List<Event> process(DeprecatePropertyCommand cmd) {
        return Collections.singletonList(new PropertyDeprecatedEvent());
    }

    protected void apply(PropertyCreatedEvent event) {
        this._key = event.getKey();
        this._value = event.getValue();
        setStatus(AggregateStatus.CHANGED);
    }

    protected void apply(PropertyValueChangedEvent event) {
        this._value = event.getNewValue();
        setStatus(AggregateStatus.CHANGED);
    }

    protected void apply(PropertyDeprecatedEvent event) {
        setStatus(AggregateStatus.DEAD);
    }

    public String getKey() {
        return _key;
    }

    public String getValue() {
        return _value;
    }
}
