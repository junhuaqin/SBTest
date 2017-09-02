package com.logicmonitor.domain.store;

import com.logicmonitor.domain.DomainObject;
import com.logicmonitor.domain.id.ID;

import java.util.Collection;
import java.util.stream.Stream;

/**
 * Created by Robert Qin on 31/08/2017.
 */
public interface RepositoryStore<T extends DomainObject<T, IT>, IT extends ID> {
    void save(StoreEntity<T, IT> entity);
    void saveAll(Collection<StoreEntity<T, IT>> entities);
    StoreEntity<T, IT> findOne(IT id);
    Stream<StoreEntity<T, IT>> findAll();
    Stream<StoreEntity<T, IT>> findAllParallel();
    void delete(IT id);
    void deleteAll(Collection<IT> ids);
}
