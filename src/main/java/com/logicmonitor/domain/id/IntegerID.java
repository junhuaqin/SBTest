package com.logicmonitor.domain.id;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class IntegerID extends ID<Integer> {
    private final Integer _id;

    public IntegerID(int id) {
        _id = id;
    }

    @Override
    public Integer value() {
        return _id;
    }

    @Override
    public String toString() {
        return String.valueOf(value());
    }
}
