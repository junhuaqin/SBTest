package com.logicmonitor.sbtest.domain.property.event;

/**
 * Created by Robert Qin on 03/09/2017.
 */
public class PropertyValueChangedEvent implements PropertyEvent {
    private final String _newValue;

    public PropertyValueChangedEvent(String newValue) {
        this._newValue = newValue;
    }

    public String getNewValue() {
        return _newValue;
    }
}
