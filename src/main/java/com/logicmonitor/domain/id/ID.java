package com.logicmonitor.domain.id;

/**
 * Created by Robert Qin on 29/08/2017.
 */
public abstract class ID<T extends Comparable> implements Comparable<ID>{
    public abstract T value();

    @Override
    public int hashCode() {
        return value().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }

        if (obj instanceof ID) {
            ID id = (ID)obj;
            return value().equals(id.value());
        }

        return false;
    }

    @Override
    public int compareTo(ID o) {
        return value().compareTo(o.value());
    }
}
