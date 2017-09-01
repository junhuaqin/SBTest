package com.logicmonitor.sbtest.domain.property.command;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class CreateProperty implements PropertyCommand {
    private final String _key;
    private final String _value;

    public CreateProperty(final String key, final String value) {
        _key = key;
        _value = value;
    }

    public String getKey() {
        return _key;
    }

    public String getValue() {
        return _value;
    }
}
