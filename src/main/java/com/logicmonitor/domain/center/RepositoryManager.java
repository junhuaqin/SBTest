package com.logicmonitor.domain.center;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingAggregate;
import com.logicmonitor.domain.id.ID;
import com.logicmonitor.domain.id.IDGenerator;
import com.logicmonitor.domain.repository.AggregateRepository;

import java.util.Collection;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public interface RepositoryManager {
    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    void register(Class<T> clasz, IDGenerator<IT> idGenerator);
    <T extends CommandProcessingAggregate<T, CT, IT>, CT extends Command, IT extends ID>
    AggregateRepository<T, CT, IT> getRepository(Class<T> clasz);

    Collection<AggregateRepository<?, ?, ?>> getAll();
}
