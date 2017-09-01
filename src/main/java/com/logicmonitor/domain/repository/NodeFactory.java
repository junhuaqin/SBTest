package com.logicmonitor.domain.repository;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.id.ID;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public interface NodeFactory {
    <T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>
    Node<T, CT, IT> createNode(Class<T> clasz, IT id);
}
