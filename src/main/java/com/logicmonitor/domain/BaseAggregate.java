package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 03/09/2017.
 */
public abstract class BaseAggregate<T extends ReflectiveCommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
        extends ReflectiveCommandProcessingAggregate<T, CT, IT> {
    protected AggregateStatus _status = AggregateStatus.ALIVE;

    protected void setStatus(AggregateStatus status) {
        _status = status;
    }

    @Override
    public AggregateStatus getStatus() {
        return _status;
    }

    @Override
    public void makeImmutable() {
        _status = AggregateStatus.ALIVE;
    }
}
