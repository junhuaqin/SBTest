package com.logicmonitor.domain.id;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public class IntegerIDGenerator<T extends IntegerID> implements IDGenerator<T> {
    private final AtomicInteger _last;
    private final Function<IntegerID, T> _translator;

    public IntegerIDGenerator(Function<IntegerID, T> translator) {
        this(translator, 0);
    }

    public IntegerIDGenerator(Function<IntegerID, T> translator, Integer last) {
        _translator = translator;
        _last = new AtomicInteger(last);
    }

    @Override
    public void setSeed(T id) {
        _last.set(id.value());
    }

    @Override
    public T create() {
        return _translator.apply(new IntegerID(_last.incrementAndGet()));
    }
}
