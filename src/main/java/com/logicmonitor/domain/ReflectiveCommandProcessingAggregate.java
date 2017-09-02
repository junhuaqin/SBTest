package com.logicmonitor.domain;

import com.logicmonitor.domain.id.ID;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Robert Qin on 30/08/2017.
 */
public abstract class ReflectiveCommandProcessingAggregate<T extends ReflectiveCommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
       implements CommandProcessingAggregate<T, CT, IT> {
    @Override
    public List<Event> processCommand(CT cmd) {
        try {
            Method method = getClass().getDeclaredMethod("process", cmd.getClass());
            method.setAccessible(true);
            return (List<Event>) method.invoke(this, cmd);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getCause());
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T applyEvent(Event event) {
        try {
            Method method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);
        } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
        return (T)this;
    }
}
