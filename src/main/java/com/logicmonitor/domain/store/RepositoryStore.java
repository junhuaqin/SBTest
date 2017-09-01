package com.logicmonitor.domain.store;

import com.logicmonitor.domain.Command;
import com.logicmonitor.domain.CommandProcessingDomainObject;
import com.logicmonitor.domain.id.ID;

import java.util.List;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public interface RepositoryStore<T extends CommandProcessingDomainObject<T, CT, IT>, CT extends Command, IT extends ID>  {
    void save(IT id, T object);
    T findOne(IT id);
    List<T> findAll();
    void delete(IT id);
}
