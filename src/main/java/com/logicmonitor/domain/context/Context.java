package com.logicmonitor.domain.context;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.Event;
import com.logicmonitor.domain.TransactionSupport;
import com.logicmonitor.domain.id.ID;

import java.util.List;

/**
 * Created by Robert Qin on 29/08/2017.
 */
public interface Context extends TransactionSupport{
    class ContextException extends RuntimeException {
        public ContextException() {
            super();
        }

        public ContextException(Throwable cause) {
            super(cause);
        }

        public ContextException(String message) {
            super(message);
        }

        public ContextException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    IT save(Class<T> clasz, CT createCommand);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    T get(Class<T> clasz, IT id);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    List<Event> process(Class<T> clasz, IT id, CT command);

    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    void apply(Class<T> clasz, IT id, Event event);

    void commit() throws ContextException;
}
