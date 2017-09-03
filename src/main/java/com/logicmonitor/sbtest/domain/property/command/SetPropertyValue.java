package com.logicmonitor.sbtest.domain.property.command;

/**
 * Created by Robert Qin on 03/09/2017.
 */
public class SetPropertyValue implements PropertyCommand{
    private final String _value;

    public SetPropertyValue(String value) {
        this._value = value;
    }

    public String getValue() {
        return _value;
    }
}
