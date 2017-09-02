package com.logicmonitor.domain.store;

import com.logicmonitor.domain.Aggregate;
import com.logicmonitor.domain.id.ID;

import java.sql.Connection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Created by Robert Qin on 02/09/2017.
 */
public class JDBCStoreContext implements StoreContext{
    private final Map<Class<? extends Aggregate>, SqlMapper<?, ?>> _sqlMappers;
    private final Supplier<Connection> _connProvider;
    private Connection _conn;

    public JDBCStoreContext(Map<Class<? extends Aggregate>, SqlMapper<?, ?>> sqlMappers,
                            Supplier<Connection> connProvider) {
        _sqlMappers = new ConcurrentHashMap<>(sqlMappers);
        _connProvider = connProvider;
    }

    @Override
    public <T extends Aggregate<T, IT>, IT extends ID> RepositoryStore<T, IT> create(Class<T> clasz) {
        return new JDBCRepositoryStore<>(_getConn(), (SqlMapper<T, IT>) _sqlMappers.get(clasz));
    }

    @Override
    public void commit() throws Exception {
        if (null != _conn) {
            _conn.commit();
        }
    }

    @Override
    public void abort() {

    }

    private synchronized Connection _getConn() {
        return Optional.ofNullable(_conn).orElseGet(_connProvider);
    }
}
