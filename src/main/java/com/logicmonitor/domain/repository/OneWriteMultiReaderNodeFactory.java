package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public class OneWriteMultiReaderNodeFactory implements NodeFactory {
    @Override
    public <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    Node<T, CT, IT> createNode(Class<T> clasz, IT id) {
        return new NonLockNode<>(clasz, id);
    }
}
