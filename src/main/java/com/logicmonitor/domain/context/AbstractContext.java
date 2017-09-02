package com.logicmonitor.domain.context;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.center.RepositoryManager;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.repository.Node;

import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Created by Robert Qin on 01/09/2017.
 */
public abstract class AbstractContext implements Context {
    private static final int _PARALLEL_LIMIT = 100;
    protected void executeOnAllNodes(Set<Node<?, ?, ?>> nodes, Consumer<Node<?, ?, ?>> operator) {
        Objects.requireNonNull(nodes);
        Objects.requireNonNull(operator);

        Stream<Node<?, ?, ?>> stream;
        if (nodes.size() > _PARALLEL_LIMIT) {
            stream = nodes.parallelStream();
        }
        else {
            stream = nodes.stream();
        }

        stream.forEach(operator);
    }

    protected  <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    Node<T, CT, IT> findNode(RepositoryManager manager, Class<T> clasz, IT id) {
        return manager.getRepository(clasz).find(id);
    }
}
